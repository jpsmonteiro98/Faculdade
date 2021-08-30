#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

//-----------------------------------------------------------------
// Criar Novo Utilizador
//-----------------------------------------------------------------
void criar(){
  int i=0, quant=0, flag=0, flag2=0, totquant=0, totdin=0;
  char contacto[32];
  char nome[16];
  float saldo=0;
  char util[32];
  char pass1[16];
  char pass2[16];
  char confirmar[20];
  char produto[40];
  printf("Nome: ");
  scanf("%s", nome);
  printf("Contacto: ");
  scanf("%s", contacto);
  while(flag!=1){
    printf("Password: ");
    scanf("%s",pass1);
    printf("Confirma a Password: ");
    scanf("%s",pass2);
    if(strcmp (pass1, pass2) == 0){
      flag=1;
    }
    else
      printf("As passwords não são iguais, tente outra vez.\n");
  }
 ------  while(flag2!=1){ ----------------------------------------------------------
    printf("Nome de Utilizador: ");
    scanf("%s", util);
    FILE * conf;
    FILE * prod;
    
    sprintf(confirmar,"%s.txt",util);
    sprintf(produto,"prod%s",confirmar);
    conf = fopen(confirmar, "r");
    if(conf== NULL){
      conf = fopen(confirmar, "w");
      
      fprintf(conf,"%s\n%s\n%s\n%.2f\n", pass1, nome, contacto, saldo);

      fclose(conf);
      
      prod = fopen(produto, "w");
      fprintf(prod,"%d\n%d\n", totquant, totdin);
      for(i=0; i<100; i++){
	fprintf(prod,"%d %d\n",i,quant);
      }
      fclose(prod);

      system("clear");
      flag2 = 1;
    }
    else{
      fclose(conf);
      printf("O usuário já existe. Tente Novamente.\n");
    }
  }
}---------------------------------------------------------------------------
//-----------------------------------------------------------------
// Criar Novo Produto
//-----------------------------------------------------------------
void novo(){
  char prod[16];
  int cod=0;
  int quant=0;
  float custo=0;
  float preco=0;
  char codigo[6];
  int flag=0;
  printf("Escreva o nome do produto\n");
  scanf("%s",prod);
  printf("Escreva o custo do produto\n");
  scanf("%f",&custo);
  printf("Escreva o preco do produto\n");
  scanf("%f",&preco);
  printf("Escreva a quantidade do produto\n");
  scanf("%d",&quant);
 -------------------------- -------------
  FILE * fpo;
  while (flag != 1){
    sprintf(codigo, "%d.txt", cod);
    fpo = fopen(codigo, "r");
    if(fpo==NULL){
      fpo = fopen(codigo, "a");
      fprintf(fpo,"%s\n%.2f\n%.2f\n%d\n", prod, custo, preco, quant);
      flag = 1;
      fclose(fpo);
    }
    else{
      fclose(fpo);
      cod++;
    }
  }
  system("clear");
}----------------------------------
//-----------------------------------------------------------------
// Editar Um Produto
//-----------------------------------------------------------------
void editar(){
  int cod=-1;
  char codigo[6];
  char *desc = NULL;
  int quant;
  float custo;
  float preco;
  int flag=0;
  while(flag != 1){
  printf("Insira o codigo do produto a editar: \n");
  scanf("%d",&cod);
  sprintf(codigo, "%d.txt", cod);
  FILE *fil;
  fil = fopen(codigo, "r");
  if(fil == NULL){
    system("clear");
    printf("O produto não existe.\n");
    break;
  }
  else{
    char line [32];
    fseek(fil, 0, SEEK_SET);
    desc = fgets(line, sizeof line, fil);
    fclose(fil);
    printf("Escreva o novo custo do produto\n");
    scanf("%f",&custo);
    printf("Escreva o novo preco do produto\n");
    scanf("%f",&preco);
    printf("Escreva a nova quantidade do produto\n");
    scanf("%d",&quant);
    fopen(codigo, "w");
    fprintf(fil,"%s%.2f\n%.2f\n%d\n", desc, custo, preco, quant);
    fclose(fil);
    flag = 1;
    system("clear");
  }
  }
}
//-----------------------------------------------------------------
// Eliminar
//-----------------------------------------------------------------
void elim(){
  int cod=-1;
  char codigo[6];
  int flag=0;
  char line[32];
  char *linha;
  while(flag != 1){
  printf("Insira o codigo do produto a eliminar: \n");
  scanf("%d",&cod);
  sprintf(codigo, "%d.txt", cod);
  FILE *file;
  file = fopen(codigo, "r");
  if(file == NULL){
    printf("O produto não existe.\n");
    system("clear");
    break;
  }
  else{
    linha=fgets(line, sizeof line, file);
    fclose(file);
    file=fopen(codigo, "w");
    fprintf(file,"%s\n\n\n\n", linha);
    fclose(file);
    flag = 1;
  }
  }
}
//-----------------------------------------------------------------
// Gerir Stocks
//-----------------------------------------------------------------
void gerir(){
  int esco=0;
  while(esco<4){
    printf("**Gerir Stocks**\n");
    printf("1) Criar Novo Produto\n");
    printf("2) Editar Produto\n");
    printf("3) Eliminar Produto\n");
    printf("4) Menu Principal\n");
    scanf("%d",&esco);
    switch(esco){
    case 1: {system("clear");novo();break;};
    case 2: {system("clear");editar();break;};
    case 3: {system("clear");elim();break;};
    case 4: {system("clear");break;};
    default:{system("clear");printf("Input errado, tente novamente.\n");esco=0;break;};
    }
  }
}
//-----------------------------------------------------------------
// Estatisticas Gerais
//-----------------------------------------------------------------
-------------------------
void total(){
  char produsr[20], line[32], tot[32], totdin[32];
  FILE * total;
  total=fopen("stats.txt", "r");
  fseek(total, 0, SEEK_SET);
  fgets(line, sizeof line, total);
  line[strlen(line) - 1] = 0;
  strcpy(tot, line);
  printf("Vendemos um total de %s produtos.\n", tot);
  fgets(line,sizeof line, total);
  line[strlen(line) - 1] = 0;
  strcpy(totdin, line);
  printf("Tivemos %s euros de lucro!\n", totdin);
  fclose(total);
}
-------------------------
//-----------------------------------------------------------------
// Estatisticas dos produtos
//-----------------------------------------------------------------
void individual(){
  int i=0;
  int flag=0;
  int cod=0;
  int quant=0;
  char codigo[8];
  char line[32];
  char produs[20];
  char *ptr;
  char *linha=NULL;
  printf("+--------------------------------------------------+\n");
  printf("| Codigo |  Nome do Produto  | Quantidade Comprada |\n");
  
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
      prd=fopen("stats.txt","r");
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
// Estatisticas de um certo utilizador.
//-----------------------------------------------------------------
void estat(){
  char util[20];
  char utilizador[16];
  int i=0;
  int flag=0;
  int cod=0;
  int quant=0;
  char codigo[8];
  char line[32];
  char produs[24];
  char *ptr;
  char *linha=NULL;
  
  printf("Indique o Nome de utilizador pretendido: \n");
  scanf("%s", utilizador);
  sprintf(util, "%s.txt", utilizador); 
  
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
void stats(){
  int esc=0;
  while(esc<4){
    printf("**Estatísticas**\n");
    printf("1) Estatisticas gerais\n");
    printf("2) Estatisticas dos produtos\n");
    printf("3) Compras de um certo utilizador\n");
    printf("4) Menu Principal\n");
    scanf("%d",&esc);
    switch(esc){
    case 1: {system("clear");total();break;};
    case 2: {system("clear");individual();break;};
    case 3: {system("clear");estat();break;};
    case 4: {system("clear");break;};
    default:{system("clear");printf("Input errado, tente novamente.\n");esc=0;break;};
    }
  }
}
//-----------------------------------------------------------------
// Login
//-----------------------------------------------------------------
---------------------
void login(){
  char user[32];
  char gestor[32] = "gestor";
  char pass[16];
  char passadm[16] = "admin1";
  int flag = 0;
  printf("Insira o nome de utilizador: \n");
  scanf("%s", user);
  while(flag != 1){
    if(strcmp (user, gestor) == 0){
      printf("Introduza a palavra-passe\n");
      scanf("%s", pass);
      if(strcmp (pass, passadm) == 0){
	flag = 1;
      }
      else{
	printf("Palavra-passe errada!\n");
	exit(0);
      }
    }
    else{
      printf("Utilizador errado!\n");
      exit(0);
    }
  }
  system("clear");
}
---------------------------
//-----------------------------------------------------------------
// Menu Principal
//-----------------------------------------------------------------
int main(){
  int escolha=0;
  login();
  while(escolha<4){
    printf("**Menu Principal**\n");
    printf("1) Criar Novo Utilizador\n");
    printf("2) Gerir Stocks\n");
    printf("3) Ver Estatísticas\n");
    printf("4) Logout\n");
    scanf("%d",&escolha);
    switch(escolha){
    case 1: {system("clear");criar();break;};
    case 2: {system("clear");gerir();break;};
    case 3: {system("clear");stats();break;};
    case 4: {system("clear");printf("Vai ser desconectado.\n");break;};
    default:{system("clear");printf("Input errado, tente novamente.\n");escolha=0;break;};
    }
  }
  return 0;
}
//-----------------------------------------------------------------
// Fin
//-----------------------------------------------------------------
