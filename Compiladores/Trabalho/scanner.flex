%{
// HEADERS
#include <stdlib.h>
#include "parser.h"

// variables maintained by the lexical analyser
int yyline = 1;
%}

%option noyywrap

%%
[ \t]+ {  }
#.*\n { yyline++; }
\n { yyline++; }

\-?[0-9]+ {
   yylval.intValue = atoi(yytext);
   return INT;
}
"+" { return PLUS; }
"-" { return MINUS; }
"*" { return MULT; }
"/" { return DIV; }
"%" { return MOD; }
"==" { return EQUAL; }
"!=" { return DIFFERENT; }
"<" { return LESS_THEN; }
">" { return BIGGER_THEN; }
"<=" { return LESS_OR_EQUAL; }
">=" { return BIGGER_OR_EQUAL; }
"&&" { return AND; }
"||" { return OR; }
"let" { return LET; }
"while" { return WHILE; }
"if" { return IF; }
"else" { return ELSE; }
"=" { return ATTRIB; }
"fn main()" { return MAIN; }
"println!" { return PRINTLN; }
"read_line" { return INPUT; }
"&" { return COM_AND; }
";" { return SEMICOLON; }
"," { return COMMA; }
"\"" { return QUOTES; }
"(" { return OPEN_P; }
")" { return CLOSE_P; }
"{" { return OPEN_BRAKET; }
"}" { return CLOSE_BRAKET; }

[a-z][a-zA-Z]* {
	yylval.string = strdup(yytext);
	return STRING;
}

.  { yyerror("unexpected character"); }
%%
