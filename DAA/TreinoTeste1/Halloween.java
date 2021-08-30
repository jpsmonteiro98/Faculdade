import java.util.*;

class Arco {
  int no_final;

  Arco(int fim){
    no_final = fim;
  }

  int extremo_final() {
    return no_final;
  }
}


class No {
  //int label;
  LinkedList<Arco> adjs;

  No() {
    adjs = new LinkedList<Arco>();
  }
}


class Grafo0 {
  No verts[];
  int nvs, narcos;

  public Grafo0(int n) {
    nvs = n;
    narcos = 0;
    verts  = new No[n+1];
    for (int i = 0 ; i <= n ; i++)
    verts[i] = new No();
    // para vertices numerados de 1 a n (posicao 0 nao vai ser usada)
  }

  public int num_vertices(){
    return nvs;
  }

  public int num_arcos(){
    return narcos;
  }

  public LinkedList<Arco> adjs_no(int i) {
    return verts[i].adjs;
  }

  public void insert_new_arc(int i, int j){
    verts[i].adjs.addFirst(new Arco(j));
    narcos++;
  }

  public Arco find_arc(int i, int j){
    for (Arco adj: adjs_no(i))
    if (adj.extremo_final() == j) return adj;
    return null;
  }
}

class Halloween {

  public static void BFS (int no, Grafo0 g ,int nhabob[]){
    Queue<Integer> q = new LinkedList<Integer>();
    boolean visitados[] = new boolean[g.num_arcos()+1];
    //int pai[] = new int[g.num_arcos()+1];
    if (nhabob[no]!=0){
      System.out.println(no);
      return;
    }
    for (int i=0;i<g.num_arcos()+1;i++){
      visitados[i]=false;
      //pai[i] = 0;
    }
    visitados[no]=true;
    q.add(no);
    int max = 0;
    do{
      int v = q.remove();
      for (Arco a:g.adjs_no(v)){
        if (visitados[a.extremo_final()]==false){
          q.add(a.extremo_final());
          //pai[a.extremo_final()] = v;
          if (nhabob[max]==nhabob[a.extremo_final()] && a.extremo_final()<max || nhabob[a.extremo_final()] > max){
            max = a.extremo_final();
          }
          visitados[a.extremo_final()]=true;
        }
      }
    }while(!q.isEmpty());
    if (nhabob[max] == 0){
      System.out.println("Impossivel");
      return ;
    }
    else {
      System.out.println(max);
      return;
    }
  }

  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int nsuper = stdin.nextInt();
    Grafo0 g = new Grafo0(nsuper);
    int nhabob[] = new int[nsuper+1];
    for (int i=1;i<=nsuper;i++){
      nhabob[i] = stdin.nextInt();
    }
    int nramos = stdin.nextInt();
    for (int i=0;i<nramos;i++){
      int prev = stdin.nextInt();
      int next = stdin.nextInt();
      if (g.find_arc(prev,next)==null){
        g.insert_new_arc(prev,next);
      }
      if (g.find_arc(next,prev)==null){
        g.insert_new_arc(next,prev);
      }
    }
    int ncasos = stdin.nextInt();
    for (int i=0;i<ncasos;i++){
      int caso = stdin.nextInt();
      BFS(caso,g,nhabob);
    }
  }
}
