// Tokens
%token
  INT
  BOOL
  STRING
  PLUS
  MINUS
  MULT
  DIV
  MOD
  EQUAL
  DIFFERENT
  LESS_THEN
  BIGGER_THEN
  LESS_OR_EQUAL
  BIGGER_OR_EQUAL
  AND
  OR
  LET
  WHILE
  IF
  ELSE
  ATTRIB
  MAIN
  PRINTLN
  INPUT
  COM_AND
  SEMICOLON
  COMMA
  QUOTES
  OPEN_P
  CLOSE_P
  OPEN_BRAKET
  CLOSE_BRAKET

// Operator associativity & precedence
%left PLUS MINUS
%left MULT DIV MOD
%left BIGGER_THEN LESS_THEN BIGGER_OR_EQUAL LESS_OR_EQUAL
%left EQUAL DIFFERENT

// Root-level grammar symbol
%start program;

// Types/values in association to grammar symbols.
%union {
  int intValue;
  bool boolValue;
  Expr* exprValue;
  BoolExpr* boolExpr;
  char* string;
  Cmd* cmdValue;
  CmdList* cmdList;
}

%type <intValue> INT
%type <exprValue> expr
%type <boolValue> BOOL
%type <boolExpr> bExpr
%type <string> STRING
%type <cmdValue> cmd
%type <cmdList> list

// Use "%code requires" to make declarations go
// into both parser.c and parser.h
%code requires {
#include <stdio.h>
#include <stdlib.h>
#include "ast.h"

extern int yylex();
extern int yyline;
extern char* yytext;
extern FILE* yyin;
extern void yyerror(const char* msg);
CmdList* root;
}

%%

program: MAIN OPEN_BRAKET list CLOSE_BRAKET {
         root = $3;
       }
       ;

list: { $$ = NULL; }
      |
      cmd list {
        $$ = ast_list($1, $2);
      }
      ;

cmd: LET STRING ATTRIB expr SEMICOLON {
      $$ = ast_attrib($2, $4);
    }
    |
    STRING ATTRIB expr SEMICOLON {
      $$ = ast_attrib2($1, $3);
    }
    |
    IF OPEN_P bExpr CLOSE_P OPEN_BRAKET list CLOSE_BRAKET ELSE OPEN_BRAKET list CLOSE_BRAKET {
    $$ = ast_if_else($3, $6, $10);
    }
    |
    IF OPEN_P bExpr CLOSE_P OPEN_BRAKET list CLOSE_BRAKET {
      $$ = ast_if($3, $6);
    }
    |
    WHILE OPEN_P bExpr CLOSE_P OPEN_BRAKET list CLOSE_BRAKET {
      $$ = ast_while($3, $6);
    }
    |
    PRINTLN OPEN_P QUOTES STRING QUOTES COMMA STRING CLOSE_P SEMICOLON {
    $$ = ast_print_var($4, $7);
    }
    |
    PRINTLN OPEN_P QUOTES STRING QUOTES CLOSE_P SEMICOLON {
      $$ = ast_print_str($4);
    }
    |
    INPUT OPEN_P COM_AND STRING CLOSE_P SEMICOLON {
      $$ = ast_read($4);
    }
    ;

bExpr:
  BOOL {
    $$ = ast_boolean($1);
  }
  |
  expr EQUAL expr {
    $$ = ast_operation_bool(EQUAL, $1, $3);
  }
  |
  expr DIFFERENT expr {
    $$ = ast_operation_bool(DIFFERENT, $1, $3);
  }
  |
  expr LESS_THEN expr {
    $$ = ast_operation_bool(LESS_THEN, $1, $3);
  }
  |
  expr BIGGER_THEN expr {
    $$ = ast_operation_bool(BIGGER_THEN, $1, $3);
  }
  |
  expr LESS_OR_EQUAL expr {
    $$ = ast_operation_bool(LESS_OR_EQUAL, $1, $3);
  }
  |
  expr BIGGER_OR_EQUAL expr {
    $$ = ast_operation_bool(BIGGER_OR_EQUAL, $1, $3);
  }
  ;

expr:
  INT {
    $$ = ast_integer($1);
  }
  |
  STRING {
    $$ = ast_var($1);
  }
  |
  expr PLUS expr {
    $$ = ast_operation(PLUS, $1, $3);
  }
  |
  expr MINUS expr {
    $$ = ast_operation(MINUS, $1, $3);
  }
  |
  expr MULT expr {
    $$ = ast_operation(MULT, $1, $3);
  }
  |
  expr DIV expr {
    $$ = ast_operation(DIV, $1, $3);
  }
  |
  expr MOD expr {
    $$ = ast_operation(MOD, $1, $3);
  }
  ;

%%

void yyerror(const char* err) {
  printf("Line %d: %s - '%s'\n", yyline, err, yytext  );
}
