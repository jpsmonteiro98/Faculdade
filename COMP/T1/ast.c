// AST constructor functions

#include <stdlib.h> // for malloc
#include <string.h>
#include "ast.h" // AST header

Expr* ast_integer(int v) {
  Expr* node = (Expr*) malloc(sizeof(Expr));
  node->kind = E_INTEGER;
  node->attr.value = v;
  return node;
}

Expr* ast_operation(int operator, Expr* left, Expr* right) {
  Expr* node = (Expr*) malloc(sizeof(Expr));
  node->kind = E_OPERATION;
  node->attr.op.operator = operator;
  node->attr.op.left = left;
  node->attr.op.right = right;
  return node;
}

Expr* ast_var(char* string) {
  Expr* node = (Expr*) malloc(sizeof(Expr));
  node->kind = E_STRING;
  node->attr.var = strdup(string);
  return node;
}

BoolExpr* ast_boolean(bool b) {
  BoolExpr* node = (BoolExpr*) malloc(sizeof(BoolExpr));
  node->kind = B_BOOLEAN;
  node->attr.value = b;
  return node;
}

BoolExpr* ast_operation_bool(int relop, Expr* left, Expr* right) {
  BoolExpr* node = (BoolExpr*) malloc(sizeof(BoolExpr));
  node->kind = B_OPERATION;
  node->attr.op.relop = relop;
  node->attr.op.left = left;
  node->attr.op.right = right;
  return node;
}

Cmd* ast_attrib(char* v, void* e) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_ATTR;
  node->attr.letCmd.nomeVar = strdup(v);
  node->attr.letCmd.exp = (Expr*)e;
  return node;
}

Cmd* ast_attrib2(char* v, void* e) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_ATTR2;
  node->attr.letCmd.nomeVar = strdup(v);
  node->attr.letCmd.exp = (Expr*)e;
  return node;
}

Cmd* ast_if(BoolExpr* b, CmdList* c) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_IF;
  node->attr.ifCmd.head = b;
  node->attr.ifCmd.tail = c;
  return node;
}

Cmd* ast_if_else(BoolExpr* b, CmdList* c1, CmdList* c2) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_IF_ELSE;
  node->attr.ifElseCmd.head = b;
  node->attr.ifElseCmd.ifTail = c1;
  node->attr.ifElseCmd.elseTail = c2;
  return node;
}

Cmd* ast_while(BoolExpr* b, CmdList* c) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_WHILE;
  node->attr.whileCmd.head = b;
  node->attr.whileCmd.tail = c;
  return node;
}

Cmd* ast_print_str(char* v) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_PRINT_STRING;
  node->attr.printCmd.str = strdup(v);
  return node;
}

Cmd* ast_print_var(char* v1, char* v2) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_PRINT_VAR;
  node->attr.printCmd.killme.str1 = strdup(v1);
  node->attr.printCmd.killme.var = strdup(v2);
  return node;
}

Cmd* ast_read(char* c) {
  Cmd* node = (Cmd*) malloc(sizeof(Cmd));
  node->kind = C_READ;
  node->attr.readCmd.txt = strdup(c);
  return node;
}

CmdList* ast_list(Cmd* c1, CmdList* c2) {
  CmdList* node = (CmdList*) malloc(sizeof(CmdList));
  node->head = c1;
  node->tail = c2;
  return node;
}
