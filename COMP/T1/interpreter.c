#include <stdio.h>
#include "printAbsTree.h"

int main(int argc, char** argv) {
  --argc; ++argv;
  if (argc != 0) {
    yyin = fopen(*argv, "r");
    if (!yyin) { //  yyin = stdin
      printf("'%s': could not open file\n", *argv);
      return 1;
    }
  }
  if (yyparse() == 0) {
    printf("fn main()\n");
    printCmdList(root, 1);
  }
  return 0;
}
