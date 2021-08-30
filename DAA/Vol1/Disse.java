import java.util.*;

class Disse{

  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int n = stdin.nextInt();
    int[n] p = new int();
    for (int i=0; i<n;i++) {
      p[i]=stdin.nextInt();
    }
    int[n] aux = new int();
    aux[0] = p[0];
    for (int i=1;i<n;i++) {
      if (aux.isIn(p[aux[i-1]-1]));
    }
  }
}
