#ifndef __printAbsTree_h__
#define __printAbsTree_h__

#include "parser.h"

void spaces(int s);
void printExpr(Expr* exp, int s);
void printBool(BoolExpr* b, int s);
void printCmdList(CmdList* cmdlist, int s);
void printCmd(Cmd* command, int s);

#endif
