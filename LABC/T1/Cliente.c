#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
//-----------------------------------------------------------------
// Consultar Saldo
//-----------------------------------------------------------------
void consul(char util[]){
  char *saldo=NULL;
  char line[32];
  FILE *con;
  con = fopen(util, "r");
  fseek(con, 0, SEEK_SET);
  while(!feof(con)){
    fgets(line, sizeof line, con);
  }
  saldo=line;
  printf("Saldo atual: %s", saldo);
  fclose(con);
}
//-----------------------------------------------------------------
// Acrescentar Saldo
//-----------------------------------------------------------------
void acres(char util[]){
  float valacres;
  float val;
  char *saldo=NULL;
  char line[32];
  printf("Indique qual o valor que pretende acrescentar\n");
  scanf("%f",&valacres);
  FILE *acc;
  acc = fopen(util, "r");
  fseek(acc, 0, SEEK_SET);
  while(!feof(acc)){
    fgets(line, sizeof line, acc);
  }
  saldo=line;
  val=atof(saldo);
  val = val + valacres;
  fclose(acc);
  acc = fopen(util, "a");
  fseek(acc, 0, SEEK_END);
  fprintf(acc, "\n%.2f", val);
  fclose(acc);
}
//-----------------------------------------------------------------
// Gerir Saldo
//-----------------------------------------------------------------
void gerir(char util[]){
  int escolha=0;
  while(escolha<3){
    printf("\n**Saldo**\n");
    printf("1) Consultar Saldo\n");
    printf("2) Acrescentar Saldo\n");
    printf("3) Menu Principal\n");
    scanf("%d",&escolha);
    switch(escolha){
    case 1: {system("clear");consul(util);break;};
    case 2: {system("clear");acres(util);break;};
    case 3: {system("clear");break;};
    default:{system("clear");printf("Input errado, tente novamente.\n");escolha=0;break;};
    }
  }
}
//-----------------------------------------------------------------
// Consulta de produtos
//-----------------------------------------------------------------
void conprod(char util[]){
  int flag=0;
  int i=0;
  int cod=0;
  char codigo[6];
  char *fpo=NULL;
  char line[36];
  char esco;
  printf("+---------------------------------------------------------+\n");
  printf("| Codigo |  Nome do Produto  | Custo | Preco | Quantidade |\n");
  
  FILE * fptr;
  while(flag!=1){
    sprintf(codigo, "%d.txt", cod);
    fptr = fopen(codigo, "r");
    if(fptr==NULL){
      printf("+---------------------------------------------------------+\n\n");
      flag=1;
    }
    else{
      printf("|  %-4d  |",cod);
      fgets(line, sizeof line, fptr);
      line[strlen(line) - 1] = 0;
      printf(" %-17s |",line);
      fgets(line, sizeof line, fptr);
      line[strlen(line) - 1] = 0;
      printf(" %-5s |",line);
      fgets(line, sizeof line, fptr);
      line[strlen(line) - 1] = 0;
      printf(" %-5s |",line);
      fgets(line, sizeof line, fptr);
      line[strlen(line) - 1] = 0;
      printf(" %-10s |",line);
      printf("\n");
      cod++;
    }
  }
}         
//-----------------------------------------------------------------
// Compra de produtos
//-----------------------------------------------------------------
void comprar(char util[]){
  int flag=0, flag2=0, q=0, i=0, cod=0, rest=0, quant=0, quantprod=0, totprod=0, srv_totprod=0, lucro=0;
  int srv_quantprod=0;
  float custo=0, preco=0, val=0, total=0, sal=0, totdin=0, srv_lucro=0;
  char codigo[8], line[32], prods[20], descs[20];
  char *totprods=NULL, *totdins=NULL, *custos=NULL, *precos=NULL, *quants=NULL, *saldo=NULL, *desc=NULL;
  char *mud=NULL, *quantprods=NULL, *srv_totprods=NULL, *srv_quantprods=NULL, *srv_lucros=NULL;
  char *ptr;
  char statant[100][8], srv_statant[100][8];
  conprod(util);
  while(flag2!=1){
    flag=0;
    q=0;
    printf("Selecione o codigo do produto que pretende comprar: ");
    scanf("%d",&cod);
    while(flag!=1){
      printf("Indique a quantidade: ");
      scanf("%d",&q);
      sprintf(codigo, "%d.txt", cod);
      FILE * utili;
      utili = fopen(util, "r");
      
      // Le o saldo
      fseek(utili, 0, SEEK_SET);
      while(!feof(utili)){
	fgets(line, sizeof line, utili);
      }
      saldo=line;
      sal=atof(saldo);
      fclose(utili);
      // Acaba de ler o saldo
      
      FILE * produto;
      produto = fopen(codigo, "r");
      
      //Comeca a todas as linhas do ficheiro
      fseek(produto, 0, SEEK_SET);
      desc=fgets(line, sizeof line, produto);
      strcpy(descs, desc);
      custos=fgets(line, sizeof line, produto);
      custo=atof(custos);
      precos=fgets(line, sizeof line, produto);
      preco=atof(precos);
      quants=fgets(line, sizeof line, produto);
      quant=atof(quants);
      
      //Acaba de ler todas as linhas do ficheiro
      
      fclose(produto);
      if(quant < q){
	printf("Quantidade em stock insuficiente.\n");
      }
      else
	flag = 1;
    }
    lucro=preco - custo;
    lucro= lucro * q;
    val = preco * q;
    if(sal<val){
      printf("Dinheiro insuficiente.\n");
    }
    else
      flag2 = 1;
  }
  sal = sal - val;
  FILE * utili;
  utili = fopen(util, "a");
  fseek(utili, 0, SEEK_END);
  fprintf(utili, "\n%.2f", sal);
  fclose(utili);

  //atualizar a quantidade de produtos em stock
  rest = quant - q;
  FILE * produto;
  produto = fopen(codigo, "w");

  fprintf(produto,"%s%.2f\n%.2f\n%d\n", descs, custo, preco, rest);

  fclose(produto);
  
  //Atualizar as estatisticas
  FILE * produsr;
  sprintf(prods,"prod%s",util);
  produsr=fopen(prods, "r");
  fseek(produsr, 0, SEEK_SET);
  totprods=fgets(line, sizeof line, produsr);
  totprod=strtol(totprods, &ptr, 10);
  totdins=fgets(line, sizeof line, produsr);
  totdin=atof(totdins);
  totprod = totprod + q;
  totdin = totdin + val;
  
  //Ler quantidade de cada produto ja comprado pelo utilizador
  for(i=0; i<cod; i++){
    strcpy(statant[i], fgets(line, sizeof line, produsr));
  }
  quantprods=fgets(line, sizeof line, produsr);
  for(i=cod+1; i<100; i++){
    strcpy(statant[i], fgets(line, sizeof line, produsr));
  }
  fclose(produsr);
  
  if(cod<10)
    quantprods += 2;
  else
    quantprods += 3;
  
  quantprod=atoi(quantprods);
  quantprod += q;
  
  //Escrever as quantidades atualizadas
  produsr=fopen(prods, "w");
  fseek(produsr, 0, SEEK_SET);
  fprintf(produsr,"%d\n",totprod);
  fprintf(produsr,"%.2f\n",totdin);
  for(i=0; i<cod; i++){
    fprintf(produsr,"%s",statant[i]);
  }
  fprintf(produsr,"%d %d\n", cod, quantprod);
  for(i=cod+1; i<100; i++){
    fprintf(produsr,"%s",statant[i]);
  }
  fclose(produsr);

  
  // Atualizar as estatisticas globais
  FILE * stat;
  stat = fopen("stats.txt", "r");
  fseek(stat, 0, SEEK_SET);
  srv_totprods=fgets(line, sizeof line, stat);
  srv_totprod=strtol(srv_totprods, &ptr, 10);
  srv_lucros=fgets(line, sizeof line, stat);
  srv_lucro=atof(srv_lucros);
  srv_totprod = srv_totprod + q;
  srv_lucro = lucro;

  //Ler a quantidade total comprada deste produto
  for(i=0; i<cod; i++){
    strcpy(srv_statant[i], fgets(line, sizeof line, stat));
  }
  srv_quantprods=fgets(line, sizeof line, stat);
  for(i=cod+1; i<100; i++){
    strcpy(srv_statant[i], fgets(line, sizeof line, stat));
  }
  fclose(stat);

  if(cod<10)
    srv_quantprods += 2;
  else
    srv_quantprods += 3;
  
  srv_quantprod=atoi(srv_quantprods);
  srv_quantprod += q;

  //Escrever estatisticas golbais atualizadas
  stat=fopen("stats.txt", "w");
  fseek(stat, 0, SEEK_SET);
  fprintf(stat,"%d\n",srv_totprod);
  fprintf(stat,"%.2f\n",srv_lucro);
  for(i=0; i<cod; i++){
    fprintf(stat,"%s",srv_statant[i]);
  }
  fprintf(stat,"%d %d\n", cod, srv_quantprod);
  for(i=cod+1; i<100; i++){
    fprintf(stat,"%s",srv_statant[i]);
  }
  fclose(stat);

}
//-----------------------------------------------------------------
// Gerir Lista de Compras
//-----------------------------------------------------------------
void lista(char util[]){
  int escolha=0;
  while(escolha<3){
    printf("**Lista de Compras**\n");
    printf("1) Consultar produtos existentes\n");
    printf("2) Comprar\n");
    printf("3) Menu Principal\n");
    scanf("%d",&escolha);
    switch(escolha){
    case 1: {system("clear");conprod(util);break;};
    case 2: {system("clear");comprar(util);break;};
    case 3: {system("clear");break;};
    default:{system("clear");printf("Input errado, tente novamente.\n");escolha=0;break;};
    }
  }
}
//-----------------------------------------------------------------
// Total de produtos comprados
//-----------------------------------------------------------------
void totcom(char util[]){
  char produsr[24], line[32], tot[32], totdin[32];
  FILE * total;
  sprintf(produsr, "prod%s", util);
  total=fopen(produsr, "r");
  fseek(total, 0, SEEK_SET);
  fgets(line, sizeof line, total);
  line[strlen(line) - 1] = 0;
  strcpy(tot, line);
  printf("Comprou um total de %s produtos.\n", tot);
  fgets(line,sizeof line, total);
  line[strlen(line) - 1] = 0;
  strcpy(totdin, line);
  printf("Gastou um total de %s euros.\n", totdin);
  fclose(total);
}
//-----------------------------------------------------------------
// Quantidade de cada produto
//-----------------------------------------------------------------
void quantcom(char util[]){
  int i=0;
  int flag=0;
  int cod=0;
  int quant=0;
  char codigo[8];
  char line[32];
  char produs[24];
  char *ptr;
  char *linha=NULL;
  printf("+--------------------------------------------------+\n");
  printf("| Codigo |  Nome do Produto  | Quantidade Comprada |\n");
  
  sprintf(produs,"prod%s", util);
  while(flag!=1){
    FILE * estat;
    sprintf(codigo, "%d.txt", cod);
    estat = fopen(codigo, "r");
    if(estat==NULL){
      printf("+--------------------------------------------------+\n\n");
      flag=1;
    }
    else{
      printf("|  %-4d  |",cod);
      fgets(line, sizeof line, estat);
      line[strlen(line) - 1] = 0;
      printf(" %-17s |",line);
      fclose(estat);
      FILE * prd;
      prd=fopen(produs,"r");
      for(i=0; i<cod+2; i++)
	fgets(line, sizeof line, prd);
      linha=fgets(line, sizeof line, prd);
      if(cod<10)
	linha += 2;
      else
	linha += 3;
      quant=strtol(linha, &ptr, 10);
      printf(" %-19d |",quant);
      printf("\n");
      cod++;
    }
  }
}
//-----------------------------------------------------------------
// Estatisticas
//-----------------------------------------------------------------
void stats(char util[]){
  int escolha=0;
  while(escolha<3){
    printf("**Estatísticas**\n");
    printf("1) Total de Produtos comprados\n");
    printf("2) Quantidade comprada de cada produto\n");
    printf("3) Menu Principal\n");
    scanf("%d",&escolha);
    switch(escolha){
    case 1: {system("clear");totcom(util);break;};
    case 2: {system("clear");quantcom(util);break;};
    case 3: {system("clear");break;};
    default:{system("clear");printf("Input errado, tente novamente.\n");escolha=0;break;};
    }
  }
}
//-----------------------------------------------------------------
// Menu Principal & Login
//-----------------------------------------------------------------
int main(){
  int escolha=0;
  int flag1 = 0;
  int flag2 = 0;
  char usr[32];
  char util[36];
  char *pass = NULL;
  char passw[16];
  char passwd[16];
  printf("**Login**\n");
  while(flag1!=1){
  printf("Introduza o nome de Utilizador: \n");
  scanf("%s", usr);
  FILE * login;
  
  sprintf(util,"%s.txt",usr);

  login = fopen(util, "r");
  if(login == NULL){
    printf("Utilizador nao existente.\n");
  }
  else{
    fclose(login);
    flag1 = 1;
  }
  }
  while(flag2!=1){
    FILE * p;
    
    p = fopen(util, "r");
    
    char line [32];
    fseek(p, 0, SEEK_SET);
    pass = fgets(line, sizeof line, p);
    printf("Introduza a palavra passe: \n");
    scanf("%s", passwd);
    sprintf(passw, "%s\n", passwd);
    fclose(p);
    if(strcmp (passw, pass) == 0){
      system("clear");
      flag2 = 1;
    }
    else
      printf("Palavra-passe incorreta! \n");
  }
  while(escolha<4){
    printf("**Menu**\n");
    printf("1) Gerir Saldo\n");
    printf("2) Gerir Lista de Compras\n");
    printf("3) Ver Estatísticas\n");
    printf("4) Logout\n");
    scanf("%d",&escolha);
    switch(escolha){
    case 1: {system("clear");gerir(util);break;};
    case 2: {system("clear");lista(util);break;};
    case 3: {system("clear");stats(util);break;};
    case 4: {system("clear");printf("Volte Sempre!\n");break;};
    default:{system("clear");printf("Input errado, tente novamente.\n");escolha=0;break;};
    }
  }
  return 0;
}
//-----------------------------------------------------------------
// Fin
//-----------------------------------------------------------------
