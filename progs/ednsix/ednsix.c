#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <errno.h>
#include "config.h"

/* DNS packet header */
struct dns_hdr 
{
	unsigned short id;

	/* recursion desired */	
	unsigned char rd : 1;
	/* truncation */
	unsigned char tc : 1;
	/* authoritative answer */
	unsigned char aa : 1;
	/* opcode */
	unsigned char op : 4;
	/* question (0) / response (1) */
	unsigned char qr : 1;
	/* response code */
	unsigned char rcode : 4;
	/* reversed */
	unsigned char zero : 3;
	/* recursion available */
	unsigned char ra : 1;

	unsigned short qdcount;
	unsigned short ancount;
	unsigned short nscount;
	unsigned short arcount;
} __attribute__((packed));

struct dns_question
{
	unsigned short type;
	unsigned short class;
} __attribute__((packed));

struct dns_res
{
	unsigned short type;
	unsigned short class;
	unsigned int   ttl;
	unsigned short rdlength;
} __attribute__((packed));

/* Forward the DNS datagram to the upstream servers */
static int forward_dns(char *buffer, ssize_t buffer_len, char *dgram, ssize_t dgram_len)
{
	int sfd;
	ssize_t ret_len;
	struct sockaddr_in addr;

	sfd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if (sfd == -1)
		return -1;

	addr.sin_family      = AF_INET;
	addr.sin_addr.s_addr = target_resolvers[0];
	addr.sin_port        = htons(53);

	if (-1 == sendto(sfd, dgram, dgram_len, 0, 
					 (struct sockaddr *) &addr, sizeof(addr)))
		return -1;

	if (-1 == (ret_len = recv(sfd, buffer, buffer_len, 0)))
		return -1;

	// TODO: should really check packet ID in the answer

	if (-1 == close(sfd))
		return -1;

	return ret_len;
}

static int parse_ipv6(const char *address_str, 
					  unsigned char *addr, char *mask)
{
	const char *slash = strchr(address_str, '/');
	if (NULL == slash)
		return -1;

	*mask = strtol(slash + 1, NULL, 10);
	if (errno != 0)
		return -1;
	
	char *buffer = malloc(slash - address_str + 1);
	memcpy(buffer, address_str, slash - address_str);
	buffer[slash - address_str] = '\0';

	if (0 == inet_pton(AF_INET6, buffer, addr)) {
		free(buffer);
		return -1;
	}

	free(buffer);
	return 0;
}

static size_t dns_strlen(unsigned char *ptr)
{
	size_t l = 0;

	if (*ptr & 0xc0)
		return 2;

	while (*ptr)
	{
		++l;
		++ptr;
	}

	if (*++ptr & 0xc0)
		l += 2;

	return l + 1; /* include zero byte */
}

/* Query an A racord for a domain */
static int ask_v4_address(unsigned char *domain, unsigned int *addr)
{
	char buffer[512];
	memset(buffer, 0, 512);

	struct dns_hdr *hdr = (struct dns_hdr *) buffer;
	hdr->id = htons(rand());
	hdr->rd = 1;

	hdr->qdcount = htons(1);

	void *ptr = buffer + sizeof(struct dns_hdr);

	size_t l = dns_strlen(domain);
	memcpy(ptr, domain, l);
	ptr += l;

	struct dns_question *q = (struct dns_question *) ptr;
	q->type = htons(1);
	q->class = htons(1);
	ptr += sizeof(struct dns_question);

	if (-1 == forward_dns(buffer, 512, buffer, ptr - (void *) buffer + 1))
		return -1;

	if (ntohs(hdr->ancount) < 1)
		return -1;

	ptr += dns_strlen((unsigned char *) ptr);
	struct dns_res *ans = (struct dns_res *) ptr;

	if (ntohs(ans->rdlength) != 4) /* IPv4 address must be 4 bytes */
		return -1;

	ptr += sizeof(struct dns_res);

	*addr = *((unsigned int *) ptr);
	return 0;
}

int main()
{
	unsigned char v6_prefix[16];
	char mask;

	if (-1 == parse_ipv6(nat64_prefix, v6_prefix, &mask)) {
		fprintf(stderr, "Invalid NAT64 prefix specified\n");
		exit(1);
	}
	if (96 != mask) {
		fprintf(stderr, "NAT64 prefix must be a /96\n");
		exit(1);
	}

	int udpsock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if (-1 == udpsock) {
		perror("Failed to setup socket");
		exit(1);
	}

	struct sockaddr_in addr;
	addr.sin_family       = AF_INET;
	addr.sin_addr.s_addr  = htonl(listen_address);
	addr.sin_port         = htons(listen_port);
	if (-1 == bind(udpsock, (struct sockaddr *) &addr, sizeof(addr))) {
		perror("Failed to bind to target port");
		exit(1);
	}

	char dgram[512];
	ssize_t dgram_len;

	struct dns_hdr *dgram_hdr = (struct dns_hdr *) dgram;

	struct sockaddr_in srcaddr;
	socklen_t src_len = sizeof(srcaddr);

	while ((dgram_len = recvfrom(udpsock, dgram, sizeof(dgram), 0,
								 (struct sockaddr *) &srcaddr, &src_len)) != -1)
	{
		/* Do some very simple validation on the request */
		if (ntohs(dgram_hdr->qdcount) != 1)
			abort(); // TODO: send invalid query response

		/* Process request */
		char upstream_answer[512];
		ssize_t upstream_answer_len;

		struct dns_hdr *upstream_hdr = (struct dns_hdr *) upstream_answer;

		if (-1 == (upstream_answer_len = forward_dns(upstream_answer, 512, dgram, dgram_len))) {
			perror("Failed to forward DNS query");
			continue;
		}

		char downstream_answer[512];

		struct dns_hdr *downstream_hdr = (struct dns_hdr *) downstream_answer;

		void *downstream_ptr = (void *) downstream_answer;
		void *upstream_ptr   = (void *) upstream_answer;

		memcpy(downstream_ptr, upstream_ptr, sizeof(struct dns_hdr));
		
		upstream_ptr += sizeof(struct dns_hdr);
		downstream_ptr += sizeof(struct dns_hdr);

		/* Copy question */
		//for (int i = 0; i < ntohs(upstream_hdr->qdcount); ++i) {
			size_t qlen = dns_strlen(upstream_ptr);
			memcpy(downstream_ptr, upstream_ptr, qlen + sizeof(struct dns_question));

			struct dns_question *question = (struct dns_question *) (upstream_ptr + qlen);


		//}

		/* Copy the answers */
		int rescnt = ntohs(upstream_hdr->ancount) +
					 ntohs(upstream_hdr->nscount) +
					 ntohs(upstream_hdr->arcount);

		printf("Sending answer type: %d\n", ntohs(question->type));

#ifndef IGNORE_AAAA
		if (ntohs(question->type) == 28 && !rescnt) { // IPv6 adderess needed, but not found
#else
		if (ntohs(question->type) == 28) { // We only ever return NAT64 addresses
#endif
			printf("Creating fake NAT64 address\n");
			unsigned int ipv4;

			unsigned char *dptr = upstream_ptr;
			if (-1 != ask_v4_address(dptr, &ipv4))
			{
				upstream_ptr += qlen + sizeof(struct dns_question);
				downstream_ptr += qlen + sizeof(struct dns_question);

				/* Create the NAT64 answer */
				size_t nl = dns_strlen(dptr);
				memcpy(downstream_ptr, dptr, nl);
				downstream_ptr += nl;

				struct dns_res *fakeans = (struct dns_res *) downstream_ptr;
				fakeans->type = htons(28);
				fakeans->class = htons(1);
				fakeans->ttl = htonl(3600);
				fakeans->rdlength = htons(16);

				downstream_ptr += sizeof(struct dns_res);
				memcpy(downstream_ptr, v6_prefix, 16);

				downstream_ptr += 12;
				*((unsigned int *) downstream_ptr) = ipv4;
				downstream_ptr += 4;

				downstream_hdr->ancount = htons(1);
			}
			else
				fprintf(stderr, "Failed to query upstream server for IPv4\n");
		} else { /* Just forward as is */
			upstream_ptr += qlen + sizeof(struct dns_question);
			downstream_ptr += qlen + sizeof(struct dns_question);

			for (int i = 0; i < rescnt; ++i) {
				size_t slen = dns_strlen(upstream_ptr);
				memcpy(downstream_ptr, upstream_ptr, slen + sizeof(struct dns_res));

				upstream_ptr += slen;
				struct dns_res *curptr = (struct dns_res *) upstream_ptr;
				
				upstream_ptr   += sizeof(struct dns_res);
				downstream_ptr += slen + sizeof(struct dns_res);

				int rdlength = ntohs(curptr->rdlength);
				memcpy(downstream_ptr, upstream_ptr, rdlength);
				upstream_ptr += rdlength;
				downstream_ptr += rdlength;
			}
		}

		if (-1 == sendto(udpsock, downstream_answer, downstream_ptr - (void *) downstream_answer + 1, 0, (struct sockaddr *) &srcaddr, src_len)) {
			perror("Failed to send DNS response");
			continue;	
		}

		src_len = sizeof(srcaddr);
	}

	if (dgram_len == -1) {
		perror("recv()");
		exit(1);
	}

	if (-1 == close(udpsock)) {
		perror("Failed to close socket");
		exit(1);
	}
}
