#ifndef __code_h__
#define __code_h__

#include "ast.h"
#include "parser.h"

typedef int bool;
#define TRUE 1
#define FALSE 0

struct _Atom {
  enum {
    VAR,
    NUMBER,
  }type;
  union {
    char* var;
    int number;
  }u_type;
};

struct _Label {
  char* label;
};

struct _Instr {
  enum {
    I_ATTR,
    I_STORE,
    I_PLUS,
    I_MINUS,
    I_DIV,
    I_MULT,
    I_PRINT,
    I_READ,
    B_ATTR,
    B_EQUAL,
    B_DIF,
    B_LESS,
    B_GT,
    B_LE,
    B_GE,
    B_LAB,
    B_GOTO,
  }binRel;
  union {
    struct {
      char* var;
      struct _Atom* a;
    }attr;
    struct {
      char* var;
      struct _Atom* a1;
      struct _Atom* a2;
    }oper;
    struct {
      char* var;
      struct _Atom* a;
      struct _Label* l1;
      struct _Label* l2;
    }if_label;
    struct _Label* l;
  }u_type;
};


struct _InstrList {
  struct _Instr* listHead;
  struct _InstrList* listTail;
};

typedef struct _Instr Instr;
typedef struct _Atom Atom;
typedef struct _Label Label;
typedef struct _InstrList InstrList;

Atom* make_int(int v);
Atom* make_var(char* v);

Label* make_label();

Instr* add_label(Label* l);
Instr* add_goto_label(Label* l);
Instr* make_attr_instr(char* var, Atom* a);
Instr* make_store_instr(char* var, Atom* a);
Instr* make_plus_instr(char* var, Atom* a1, Atom* a2);
Instr* make_minus_instr(char* var, Atom* a1, Atom* a2);
Instr* make_mult_instr(char* var, Atom* a1, Atom* a2);
Instr* make_div_instr(char* var, Atom* a1, Atom* a2);
Instr* make_bool_instr(char* var, bool b);
Instr* make_if_instr(char* var, Atom* a, Label* l1, Label* l2);
Instr* make_print_instr(char* var);
Instr* make_read_instr(char* var);

InstrList* make_list(Instr* i);
void add_to_list(InstrList* l, Instr* i);
Instr* get_first(InstrList* l);
InstrList* next_instr(InstrList* list);
InstrList* append(InstrList* l1, InstrList* l2);
void print_instr(Instr* i);
void print_list_instrs(InstrList* l);
int length_list(InstrList* l);
InstrList* compileExp(Expr* e, char* r);
InstrList* compileBool(BoolExpr* b, Label* labelTrue, Label* labelFalse);
InstrList* compileCmd(Cmd* c);
InstrList* compileCmdList(CmdList* cl);

void printMIPS(InstrList* l);

char* create_register();

#endif
