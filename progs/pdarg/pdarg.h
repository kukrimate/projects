/* types */
typedef void (*pdarg_exit_callback)(void);

/* functions */

/* call before usage, initializes buffers */
void pdarg_init(pdarg_exit_callback exit_callback, char *program_description);

/* add an argument to the parser */
void pdarg_add_arg(char **aliases, char *description, bool required);

/* add a flag to the parser */
void pdarg_add_flag(char **aliases, char *description);

/* call to parse */
void pdarg_parse(int argc, char **argv);

/* check is a flag present */
bool pdarg_is_flag_present(char *flag_first_alias);

/* get the value of an argument */
char *pdarg_get_arg_value(char *arg_first_alias);

/* returns generated help page */
char *pdarg_generate_help(void);

/* return generated usage message */
char *pdarg_generate_usage(void);

/* call before exit, it does cleanup */
void pdarg_fini(void);
