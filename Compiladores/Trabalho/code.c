#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "code.h"
#define MAX 7

int cont = -1;
int cont2 = 0;

Atom* make_int(int v) {
  Atom* node = (Atom*) malloc(sizeof(Atom*));
  node->type = NUMBER;
  node->u_type.number = v;
  return node;
}

Atom* make_var(char* v) {
  Atom* node = (Atom*) malloc(sizeof(Atom*));
  node->type = VAR;
  node->u_type.var = strdup(v);
  return node;
}

Label* make_label() {
  char* l = malloc(sizeof(char));
  Label* node = (Label*) malloc(sizeof(Label*));
  sprintf(l, "label_%d", cont2);
  cont2++;
  node->label = strdup(l);
  return node;
}

Instr* add_label(Label* l) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = B_LAB;
  node->u_type.l = l;
  return node;
}

Instr* add_goto_label(Label* l) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = B_GOTO;
  node->u_type.l = l;
  return node;
}

Instr* make_attr_instr(char* var, Atom* a) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_ATTR; // =
  node->u_type.attr.var = strdup(var);
  node->u_type.attr.a = a;
  return node;
}

Instr* make_store_instr(char* var, Atom* a) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_STORE;
  node->u_type.attr.var = strdup(var);
  node->u_type.attr.a = a;
  return node;
}

Instr* make_plus_instr(char* var, Atom* a1, Atom* a2) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_PLUS; // +
  node->u_type.oper.var = strdup(var);
  node->u_type.oper.a1 = a1;
  node->u_type.oper.a2 = a2;
  return node;
}

Instr* make_minus_instr(char* var, Atom* a1, Atom* a2) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_MINUS; // -
  node->u_type.oper.var = strdup(var);
  node->u_type.oper.a1 = a1;
  node->u_type.oper.a2 = a2;
  return node;
}

Instr* make_div_instr(char* var, Atom* a1, Atom* a2) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_DIV; // /
  node->u_type.oper.var = strdup(var);
  node->u_type.oper.a1 = a1;
  node->u_type.oper.a2 = a2;
  return node;
}

Instr* make_mult_instr(char* var, Atom* a1, Atom* a2) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_MULT; // *
  node->u_type.oper.var = strdup(var);
  node->u_type.oper.a1 = a1;
  node->u_type.oper.a2 = a2;
  return node;
}

Instr* make_bool_instr(char* var, bool b) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = B_ATTR; // TRUE or FALSE
  node->u_type.attr.var = strdup(var);
  node->u_type.attr.a = (struct _Atom*) make_int(b);
  return node;
}

Instr* make_if_instr(char* var, Atom* a, Label* l1, Label* l2) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->u_type.if_label.var = strdup(var);
  node->u_type.if_label.a = a;
  node->u_type.if_label.l1 = l1;
  node->u_type.if_label.l2 = l2;
  return node;
}

Instr* make_print_instr(char* var) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_PRINT;
  node->u_type.attr.var = strdup(var);
  return node;
}

Instr* make_read_instr(char* var) {
  Instr* node = (Instr*) malloc(sizeof(Instr));
  node->binRel = I_READ;
  node->u_type.attr.var = strdup(var);
  return node;
}

InstrList* make_list(Instr* i) {
  InstrList* node = (InstrList*) malloc(sizeof(InstrList));
  node->listHead = i;
  node->listTail = NULL;
  return node;
}

void add_to_list(InstrList* l, Instr* i) {
  while(l->listTail != NULL) l = l->listTail;
  l->listTail = make_list(i);
}

Instr* get_first(InstrList* l) {
  InstrList* aux = l;
  return aux->listHead;
}

InstrList* next_instr(InstrList* l) {
  InstrList* aux = l;
  return aux->listTail;
}

InstrList* append(InstrList* l1, InstrList* l2) {
  InstrList* aux = l1;
  InstrList* new_list = make_list(get_first(l1));
  while(aux->listTail != NULL) {
    aux = aux->listTail;
    add_to_list(new_list, aux->listHead);
  }
  aux = new_list;
  while(aux->listTail != NULL) aux = aux->listTail;
  aux->listTail = l2;
  return new_list;
}

void print_instr(Instr* i) {
  switch (i->binRel) {
    case I_ATTR: {
      if(i->u_type.attr.a->type == VAR) printf("(%s = %s)\n", i->u_type.attr.var, i->u_type.attr.a->u_type.var);
      else printf("(%s = %d)\n", i->u_type.attr.var, i->u_type.attr.a->u_type.number);
    }break;
    case I_STORE: {
      if(i->u_type.attr.a->type == VAR) printf("(%s = %s)\n", i->u_type.attr.var, i->u_type.attr.a->u_type.var);
      else printf("(%s = %d)\n", i->u_type.attr.var, i->u_type.attr.a->u_type.number);
    }break;
    case I_PLUS: {
      if(i->u_type.oper.a1->type == VAR) printf("(%s = %s + ", i->u_type.oper.var, i->u_type.oper.a1->u_type.var);
      else printf("(%s = %d + ", i->u_type.oper.var, i->u_type.oper.a1->u_type.number);
      if(i->u_type.oper.a2->type == VAR) printf("%s)\n", i->u_type.oper.a2->u_type.var);
      else printf("%d)\n", i->u_type.oper.a2->u_type.number);
    }break;
    case I_MINUS: {
      if(i->u_type.oper.a1->type == VAR) printf("(%s = %s - ", i->u_type.oper.var, i->u_type.oper.a1->u_type.var);
      else printf("(%s = %d - ", i->u_type.oper.var, i->u_type.oper.a1->u_type.number);
      if(i->u_type.oper.a2->type == VAR) printf("%s)\n", i->u_type.oper.a2->u_type.var);
      else printf("%d)\n", i->u_type.oper.a2->u_type.number);
    }break;
    case I_DIV: {
      if(i->u_type.oper.a1->type == VAR) printf("(%s = %s / ", i->u_type.oper.var, i->u_type.oper.a1->u_type.var);
      else printf("(%s = %d / ", i->u_type.oper.var, i->u_type.oper.a1->u_type.number);
      if(i->u_type.oper.a2->type == VAR) printf("%s)\n", i->u_type.oper.a2->u_type.var);
      else printf("%d)\n", i->u_type.oper.a2->u_type.number);
    }break;
    case I_MULT: {
      if(i->u_type.oper.a1->type == VAR) printf("(%s = %s * ", i->u_type.oper.var, i->u_type.oper.a1->u_type.var);
      else printf("(%s = %d * ", i->u_type.oper.var, i->u_type.oper.a1->u_type.number);
      if(i->u_type.oper.a2->type == VAR) printf("%s)\n", i->u_type.oper.a2->u_type.var);
      else printf("%d)\n", i->u_type.oper.a2->u_type.number);
    }break;
    case I_PRINT: printf("(print %s)\n", i->u_type.attr.var); break;
    case I_READ: printf("(read %s)\n", i->u_type.attr.var); break;
    case B_EQUAL: {
      if(i->u_type.if_label.a->type == VAR) printf("(if %s == %s then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.var, i->u_type.if_label.l2->label);
      else printf("(if %s == %d then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.number, i->u_type.if_label.l2->label);
    }break;
    case B_DIF: {
      if(i->u_type.if_label.a->type == VAR) printf("(if %s != %s then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.var, i->u_type.if_label.l2->label);
      else printf("(if %s != %d then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.number, i->u_type.if_label.l2->label);
    }break;
    case B_LESS: {
      if(i->u_type.if_label.a->type == VAR) printf("(if %s < %s then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.var, i->u_type.if_label.l2->label);
      else printf("(if %s < %d then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.number, i->u_type.if_label.l2->label);
    }break;
    case B_GT: {
      if(i->u_type.if_label.a->type == VAR) printf("(if %s > %s then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.var, i->u_type.if_label.l2->label);
      else printf("(if %s > %d then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.number, i->u_type.if_label.l2->label);
    }break;
    case B_LE: {
      if(i->u_type.if_label.a->type == VAR) printf("(if %s <= %s then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.var, i->u_type.if_label.l2->label);
      else printf("(if %s <= %d then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.number, i->u_type.if_label.l2->label);
    }break;
    case B_GE: {
      if(i->u_type.if_label.a->type == VAR) printf("(if %s >= %s then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.var, i->u_type.if_label.l2->label);
      else printf("(if %s >= %d then %s)\n", i->u_type.if_label.var, i->u_type.if_label.a->u_type.number, i->u_type.if_label.l2->label);
    }break;
    case B_LAB: printf("LAB %s:\n", i->u_type.l->label); break;
    case B_GOTO: printf("GOTO %s:\n", i->u_type.l->label); break;
  }
}

void print_list_instrs(InstrList* l) {
  InstrList* aux = l;
  while(aux->listTail != NULL) {
    print_instr(aux->listHead);
    aux = aux->listTail;
  }
  print_instr(aux->listHead);
}

int length_list(InstrList* l) {
  int cont = 0;
  InstrList* aux = l;
  while(aux != NULL) {
    aux = aux->listTail;
    cont++;
  }
  return cont;
}

InstrList* compileExp(Expr* e, char* r) {
  switch(e->kind) {
    case E_INTEGER: {
      if(r[0] != 't') {
        char* aux1 = create_register();
        Instr* inst = make_attr_instr(aux1, make_int(e->attr.value));
        InstrList* list = make_list(inst);
        Instr* inst2 = make_store_instr(r, make_var(aux1));
        add_to_list(list, inst2);
        return list;
      }
      Instr* inst = make_attr_instr(r, make_int(e->attr.value));
      InstrList* list = make_list(inst);
      return list;
    }break;
    case E_STRING: {
      if(r[0] != 't') {
        char* aux1 = create_register();
        Instr* inst = make_attr_instr(aux1, make_int(e->attr.value));
        InstrList* list = make_list(inst);
        Instr* inst2 = make_store_instr(r, make_var(aux1));
        add_to_list(list, inst2);
        return list;
      }
      Instr* inst = make_attr_instr(r, make_var(e->attr.var));
      InstrList* list = make_list(inst);
      return list;
    }break;
    case E_OPERATION: {
      if(e->attr.op.operator == PLUS) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(e->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(e->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_plus_instr(aux3, make_var(aux1), make_var(aux2));
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        Instr* inst2 = make_store_instr(r, make_var(aux3));
        add_to_list(list3, inst2);
        return list3;
      }
      else if(e->attr.op.operator == MINUS) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(e->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(e->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_minus_instr(aux3, make_var(aux1), make_var(aux2));
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        Instr* inst2 = make_store_instr(r, make_var(aux3));
        add_to_list(list3, inst2);
        return list3;
      }
      else if(e->attr.op.operator == MULT) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(e->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(e->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_mult_instr(aux3, make_var(aux1), make_var(aux2));
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        Instr* inst2 = make_store_instr(r, make_var(aux3));
        add_to_list(list3, inst2);
        return list3;
      }
      else if(e->attr.op.operator == DIV) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(e->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(e->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_div_instr(aux3, make_var(aux1), make_var(aux2));
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        Instr* inst2 = make_store_instr(r, make_var(aux3));
        add_to_list(list3, inst2);
        return list3;
      }
    }break;
  }
  printf("an error occurred...\n");
  exit(-1);
}

InstrList* compileBool(BoolExpr* b, Label* labelTrue, Label* labelFalse) {
  switch(b->kind) {
    case B_BOOLEAN: {
      Instr* inst = make_bool_instr(create_register(), b->attr.value);
      InstrList* list = make_list(inst);
      return list;
    }break;
    case B_OPERATION: {
      if(b->attr.op.relop == EQUAL) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(b->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(b->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_minus_instr(aux3, make_var(aux1), make_var(aux2));
        Instr* inst2 = make_if_instr(aux3, make_int(0), labelTrue, labelFalse);
        inst2->binRel = B_DIF;
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        add_to_list(list3, inst2);
        return list3;
      }
      else if(b->attr.op.relop == DIFFERENT) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(b->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(b->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_minus_instr(aux3, make_var(aux1), make_var(aux2));
        Instr* inst2 = make_if_instr(aux3, make_int(0), labelTrue, labelFalse);
        inst2->binRel = B_EQUAL;
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        add_to_list(list3, inst2);
        return list3;
      }
      else if(b->attr.op.relop == LESS_THEN) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(b->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(b->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_minus_instr(aux3, make_var(aux1), make_var(aux2));
        Instr* inst2 = make_if_instr(aux3, make_int(0), labelTrue, labelFalse);
        inst2->binRel = B_GE;
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        add_to_list(list3, inst2);
        return list3;
      }
      else if(b->attr.op.relop == BIGGER_THEN) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(b->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(b->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_minus_instr(aux3, make_var(aux1), make_var(aux2));
        Instr* inst2 = make_if_instr(aux3, make_int(0), labelTrue, labelFalse);
        inst2->binRel = B_LE;
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        add_to_list(list3, inst2);
        return list3;
      }
      else if(b->attr.op.relop == LESS_OR_EQUAL) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(b->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(b->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_minus_instr(aux3, make_var(aux1), make_var(aux2));
        Instr* inst2 = make_if_instr(aux3, make_int(0), labelTrue, labelFalse);
        inst2->binRel = B_GT;
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        add_to_list(list3, inst2);
        return list3;
      }
      else if(b->attr.op.relop == BIGGER_OR_EQUAL) {
        char* aux1 = create_register();
        InstrList* list1 = compileExp(b->attr.op.left, aux1);
        char* aux2 = create_register();
        InstrList* list2 = compileExp(b->attr.op.right, aux2);
        char* aux3 = create_register();
        Instr* inst = make_minus_instr(aux3, make_var(aux1), make_var(aux2));
        Instr* inst2 = make_if_instr(aux3, make_int(0), labelTrue, labelFalse);
        inst2->binRel = B_LESS;
        InstrList* list3 = append(list1, list2);
        add_to_list(list3, inst);
        add_to_list(list3, inst2);
        return list3;
      }
    }break;
  }
  printf("an error occurred...\n");
  exit(-1);
}

InstrList* compileCmd(Cmd* c) {
  switch(c->kind) {
    case C_ATTR: {
      InstrList* list = compileExp(c->attr.letCmd.exp, c->attr.letCmd.nomeVar);
      return list;
    }break;
    case C_ATTR2: {
      InstrList* list = compileExp(c->attr.letCmd.exp, c->attr.letCmd.nomeVar);
      return list;
    }break;
    case C_IF: {
      Label* labT = make_label();
      InstrList* list = compileBool(c->attr.ifCmd.head, labT, labT);
      InstrList* list2 = compileCmdList(c->attr.ifCmd.tail);
      InstrList* list3 = append(list, list2);
      add_to_list(list3, add_label(labT));
      return list3;
    }
    case C_IF_ELSE: {
      Label* labT = make_label();
      Label* labF = make_label();
      InstrList* list = compileBool(c->attr.ifElseCmd.head, labT, labF);
      InstrList* list2 = compileCmdList(c->attr.ifElseCmd.ifTail);
      add_to_list(list2, add_goto_label(labT));
      add_to_list(list2, add_label(labF));
      InstrList* list3 = compileCmdList(c->attr.ifElseCmd.elseTail);
      InstrList* list4 = append(list, list2);
      InstrList* list5 = append(list4, list3);
      add_to_list(list5, add_label(labT));
      return list5;
    }break;
    case C_WHILE: {
      Label* labT = make_label();
      Label* labF = make_label();
      InstrList* list0 = make_list(add_label(labT));
      InstrList* list = compileBool(c->attr.whileCmd.head, labT, labF);
      InstrList* list1 = append(list0, list);
      InstrList* list2 = compileCmdList(c->attr.whileCmd.tail);
      add_to_list(list2, add_goto_label(labT));
      InstrList* list3 = append(list1, list2);
      add_to_list(list3, add_label(labF));
      return list3;
    }break;
    case C_PRINT_VAR: {
      InstrList* list = make_list(make_print_instr(c->attr.printCmd.killme.var));
      return list;
    }break;
    case C_READ: {
      InstrList* list = make_list(make_read_instr(c->attr.readCmd.txt));
      return list;
    }break;
    default: printf("an error occurred...\n"); return NULL;
  }
  return NULL;
}

InstrList* compileCmdList(CmdList* cl) {
  InstrList* list = compileCmd(cl->head);
  while(cl->tail != NULL) {
    cl = cl->tail;
    InstrList* aux = compileCmd(cl->head);
    list = append(list, aux);
  }
  return list;
}

void printMIPS(InstrList* l) {
  FILE* fp;
  InstrList* aux = l;
  fp = fopen("mips.asm", "w+");
  fputs(".data\n\n", fp);
  fputs(".text\n", fp);
  while(l != NULL) {
    switch(l->listHead->binRel) {
      case I_ATTR: {
        if(l->listHead->u_type.attr.a->type == NUMBER) {
          fprintf(fp, "li $%s, %d\n", l->listHead->u_type.attr.var, l->listHead->u_type.attr.a->u_type.number);
        }
        else {
          fprintf(fp, "lw $%s, %s\n", l->listHead->u_type.attr.var, l->listHead->u_type.attr.a->u_type.var);
        }
      }break;
      case I_STORE: {
        fprintf(fp, "sw $%s, %s\n", l->listHead->u_type.attr.a->u_type.var, l->listHead->u_type.attr.var);
      }break;
      case I_PLUS: {
        if(l->listHead->u_type.oper.a2->type == VAR) {
          fprintf(fp, "add $%s, $%s, $%s\n", l->listHead->u_type.oper.var, l->listHead->u_type.oper.a1->u_type.var, l->listHead->u_type.oper.a2->u_type.var);
        }
        else {
          fprintf(fp, "addi $%s, $%s, %d\n", l->listHead->u_type.oper.var, l->listHead->u_type.oper.a1->u_type.var, l->listHead->u_type.oper.a2->u_type.number);
        }
      }break;
      case I_MINUS: {
        fprintf(fp, "sub $%s, $%s, $%s\n", l->listHead->u_type.oper.var, l->listHead->u_type.oper.a1->u_type.var, l->listHead->u_type.oper.a2->u_type.var);
      }break;
      case I_DIV: {
        fprintf(fp, "div $%s, $%s, $%s\n", l->listHead->u_type.oper.var, l->listHead->u_type.oper.a1->u_type.var, l->listHead->u_type.oper.a2->u_type.var);
      }break;
      case I_MULT: {
        fprintf(fp, "mul $%s, $%s, $%s\n", l->listHead->u_type.oper.var, l->listHead->u_type.oper.a1->u_type.var, l->listHead->u_type.oper.a2->u_type.var);
      }break;
      case B_EQUAL: {
        fprintf(fp, "beq $%s, %d, %s\n", l->listHead->u_type.if_label.var, l->listHead->u_type.if_label.a->u_type.number, l->listHead->u_type.if_label.l2->label);
      }break;
      case B_DIF: {
        fprintf(fp, "bne $%s, %d, %s\n", l->listHead->u_type.if_label.var, l->listHead->u_type.if_label.a->u_type.number, l->listHead->u_type.if_label.l2->label);
      }break;
      case B_LESS: {
        fprintf(fp, "blt $%s, %d, %s\n", l->listHead->u_type.if_label.var, l->listHead->u_type.if_label.a->u_type.number, l->listHead->u_type.if_label.l2->label);
      }break;
      case B_GT: {
        fprintf(fp, "bgt $%s, %d, %s\n", l->listHead->u_type.if_label.var, l->listHead->u_type.if_label.a->u_type.number, l->listHead->u_type.if_label.l2->label);
      }break;
      case B_LE: {
        fprintf(fp, "ble $%s, %d, %s\n", l->listHead->u_type.if_label.var, l->listHead->u_type.if_label.a->u_type.number, l->listHead->u_type.if_label.l2->label);
      }break;
      case B_GE: {
        fprintf(fp, "bge $%s, %d, %s\n", l->listHead->u_type.if_label.var, l->listHead->u_type.if_label.a->u_type.number, l->listHead->u_type.if_label.l2->label);
      }break;
      case B_LAB: {
        fprintf(fp, "%s:\n", l->listHead->u_type.l->label);
      }break;
      case B_GOTO: {
        fprintf(fp, "j %s\n", l->listHead->u_type.l->label);
      }break;
      case I_PRINT: {
        InstrList* aux2 = aux;
        while(aux2 != NULL) {
          if(strcmp(aux2->listHead->u_type.attr.var, l->listHead->u_type.attr.var)==0 && aux2->listHead->binRel == I_STORE) {
            fputs("li $v0, 1\n", fp);
            fprintf(fp, "move $a0, $%s\n", aux2->listHead->u_type.attr.a->u_type.var);
            fputs("syscall\n", fp);
            break;
          }
          aux2 = aux2->listTail;
        }
      }break;
      case I_READ: {
        fputs("li $v0, 5\n", fp);
        fputs("syscall\n", fp);
      }break;
    }
    l = l->listTail;
  }
  fclose(fp);
}

char* create_register() {
  char* aux = malloc(sizeof(char));
  if(cont < MAX) cont++;
  else cont = 0;
  sprintf(aux, "t%d", cont); /*strdup e strcat tambem sao hipoteses*/
  return aux;
}
