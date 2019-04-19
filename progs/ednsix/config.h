/* Port and address to listen on */
static unsigned       listen_address     = INADDR_ANY;
static unsigned short listen_port        = 5353;

/* Define dotted decimal IPs easily */
#define DECIP(a, b, c, d) (unsigned) (d << 24 | c << 16 | b << 8 | a)

/* Default resolvers to forward to */
static unsigned       target_resolvers[] =
{
	DECIP(10, 46, 0, 1)
};

/* Ignore real AAAA records and always return NAT64 address */
//#define IGNORE_AAAA

/* NAT64 IPv6 prefix to use */
static char nat64_prefix[] = "64:ff9b::/96";
