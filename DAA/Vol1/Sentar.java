import java.util.*;

class Sentar {
  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int ntipos = stdin.nextInt();
    int ncadeiras[] = new int[ntipos+1];
    for (int i=0;i<ntipos;i++){
      int tipo = stdin.nextInt();
      ncadeiras[tipo] = stdin.nextInt();
    }
    int nhabit = stdin.nextInt();
    for (int i=0;i<nhabit;i++){
      int nop = stdin.nextInt();
      int op[] = new int[nop];
      for (int j=0;j<nop;j++){
        op[i] = stdin.nextInt();
      }
      int flag = 0;
      for (int j=0;j<nop;j++){
        while(flag!=1){
          if (ncadeiras[op[j]]!=0){
            nhabit--;
            ncadeiras[op[j]]--;
            flag = 1;
          }
        }
      }
    }
    int total = 0;
    for (int i=0;i<ntipos;i++){
      total = total + ncadeiras[i];
    }
    System.out.println(total);
  }
}
