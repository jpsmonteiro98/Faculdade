import java.util.*;

class Cigarras {

  public static int insere (int caminho[],int nnos, int novo){
    for(int i=0; i<=nnos-1;i++){
      if (caminho[i] == novo){
        return i+1;
      }
    }
    caminho[nnos] = novo;
    return nnos+1;
  }

  public static void escreveCaminho(int caminho[], int nnos){
    for(int i=0;i<=nnos-1;i++){
      System.out.println(caminho[i]);
    }
  }

  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int caminho[] = new int[30];
    caminho[0] = stdin.nextInt();
    int nnos = 1;
    int novo = stdin.nextInt();
    while( novo!=0 ){
      nnos = insere(caminho, nnos, novo);
      novo = stdin.nextInt();
    }
    escreveCaminho(caminho,nnos);
  }
}
