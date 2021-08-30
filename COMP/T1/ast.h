// AST definitions
#ifndef __ast_h__
#define __ast_h__

typedef int bool;
#define TRUE 1
#define FALSE 0

// AST for expressions
struct _Expr {
  enum {
    E_INTEGER,
    E_OPERATION,
    E_STRING
  } kind;
  union {
    int value; // for integer values
    char* var;
    struct {
      int operator; // PLUS, MINUS, etc
      struct _Expr* left;
      struct _Expr* right;
    } op; // for binary expressions
  } attr;
};

struct _BoolExpr {
  enum {
    B_BOOLEAN,
    B_OPERATION,
  } kind;
  union {
    bool value; // for boolean values
    struct {
      int relop; // EQUAL, DIFFERENT, etc
      struct _Expr* left;
      struct _Expr* right;
    } op; // for binary expressions
  } attr;
};

struct _Cmd {
  enum {
    C_ATTR,
    C_ATTR2,
    C_ATTR3,
    C_ATTR4,
    C_IF,
    C_IF_ELSE,
    C_WHILE,
    C_PRINT_STRING,
    C_PRINT_VAR,
    C_READ,
  } kind;
  union {
    //letArit
    struct {
      char* nomeVar;
      struct _Expr* exp;
    }letCmd;
    //if
    struct {
      struct _BoolExpr* head;
      struct _CmdList* tail;
    }ifCmd;
    //if/else
    struct {
      struct _BoolExpr* head;
      struct _CmdList* ifTail;
      struct _CmdList* elseTail;
    }ifElseCmd;
    //while
    struct {
      struct _BoolExpr* head;
      struct _CmdList* tail;
    }whileCmd;
    //print
    union {
      char* str;
      struct {
        char* str1;
        char* var;
      }killme;
    }printCmd;
    //read
    struct{
      char* txt;
    }readCmd;
  } attr;
};

struct _CmdList {
  struct _Cmd* head;
  struct _CmdList* tail;
};

// Convenience typedef
typedef struct _Expr Expr;
typedef struct _BoolExpr BoolExpr;
typedef struct _Cmd Cmd;
typedef struct _CmdList CmdList;


// Constructor functions (see implementation in ast.c)
Expr* ast_integer(int v);
Expr* ast_operation(int operator, Expr* left, Expr* right);
Expr* ast_var(char* string);

BoolExpr* ast_boolean(bool b);
BoolExpr* ast_operation_bool(int relop, Expr* left, Expr* right);

Cmd* ast_attrib(char* v, void* e);
Cmd* ast_attrib2(char* v, void* e);
Cmd* ast_attrib3(char* v, char* b);
Cmd* ast_attrib4(char* v, char* b);
Cmd* ast_if(BoolExpr* b, CmdList* c);
Cmd* ast_if_else(BoolExpr* b, CmdList* c1, CmdList* c2);
Cmd* ast_while(BoolExpr* b, CmdList* c);
Cmd* ast_print_str(char* v);
Cmd* ast_print_var(char* v1, char* v2);
Cmd* ast_read(char* v);

CmdList* ast_list(Cmd* c1, CmdList* c2);


#endif
