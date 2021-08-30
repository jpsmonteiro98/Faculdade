#include "printAbsTree.h"
#include <stdio.h>
#include <string.h>

void spaces(int s) {
  int i=0;
  while(i!=s){
    printf("|");
    i++;
  }
}

void printExpr(Expr* exp, int s) {
  spaces(s);
  if(exp -> kind == E_INTEGER) {
    printf("%d\n", exp -> attr.value);
  }
  else if(exp -> kind == E_OPERATION) {
    switch(exp -> attr.op.operator) {
      case PLUS: printf("+\n"); break;
      case MINUS: printf("-\n"); break;
      case MULT: printf("*\n"); break;
      case DIV: printf("/\n"); break;
      case MOD: printf("%%\n"); break;
    }
    printExpr(exp -> attr.op.left, s+1);
    printExpr(exp -> attr.op.right, s+1);
  }
  else if (exp-> kind == E_STRING){
    printf("%s\n", exp -> attr.var);
  }
}

void printBool(BoolExpr* b, int s) {
  spaces(s);
  if(b -> kind == B_BOOLEAN) {
    switch (b -> attr.value) {
      case TRUE: printf("TRUE\n"); break;
      case FALSE: printf("FALSE\n"); break;
    }
  }
  else if(b -> kind == B_OPERATION) {
    switch(b -> attr.op.relop) {
      case EQUAL: printf("==\n"); break;
      case DIFFERENT: printf("!=\n"); break;
      case LESS_THEN: printf("<\n"); break;
      case BIGGER_THEN: printf(">\n"); break;
      case LESS_OR_EQUAL: printf("<=\n"); break;
      case BIGGER_OR_EQUAL: printf(">=\n"); break;
    }
    printExpr(b -> attr.op.left, s+1);
    printExpr(b -> attr.op.right, s+1);
  }
}

void printCmdList(CmdList* cmdlist, int s) {
  while( cmdlist != NULL) {
    printCmd(cmdlist->head, s);
    cmdlist = cmdlist->tail;
  }
}

void printCmd(Cmd* command, int s) {
  spaces(s);
  if(command -> kind == C_ATTR) {
    printf("LET\n");
    spaces(s+1);
    printf("%s\n", command -> attr.letCmd.nomeVar);
    printExpr(command -> attr.letCmd.exp, s+1);
  }
  if(command -> kind == C_ATTR2) {
    printf("ATTRIB\n");
    spaces(s+1);
    printf("%s\n", command -> attr.letCmd.nomeVar);
    printExpr(command -> attr.letCmd.exp, s+1);
  }
  if(command -> kind == C_PRINT_STRING) {
    printf("PRINT\n");
    spaces(s+1);
    printf("%s\n", command -> attr.printCmd.str);
  }
  if(command -> kind == C_PRINT_VAR) {
    printf("PRINT\n");
    spaces(s+1);
    printf("%s\n", command -> attr.printCmd.killme.str1);
    spaces(s+1);
    printf("%s\n", command -> attr.printCmd.killme.var);
  }
  if(command -> kind == C_IF) {
    printf("IF\n");
    printBool(command -> attr.ifCmd.head, s+1);
    printCmdList(command -> attr.ifCmd.tail, s+1);
  }
  if(command -> kind == C_IF_ELSE) {
    printf("IF\n");
    printBool(command -> attr.ifElseCmd.head, s+1);
    printCmdList(command -> attr.ifElseCmd.ifTail, s+1);
    spaces(s);
    printf("ELSE\n");
    printCmdList(command -> attr.ifElseCmd.elseTail, s+1);
  }
  if(command -> kind == C_WHILE) {
    printf("WHILE\n");
    printBool(command->attr.whileCmd.head, s+1);
    printCmdList(command->attr.whileCmd.tail, s+1);
  }
  if(command -> kind == C_READ) {
    printf("READ_LINE\n");
    spaces(s+1);
    printf("%s\n", command->attr.readCmd.txt);
  }
}
