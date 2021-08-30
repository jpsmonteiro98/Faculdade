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

class Mapas{

  public static void main(String[] args){
    Scanner stdin = new Scanner(System.in);
    Grafo0 g = new Grafo0(stdin.nextInt());
    int ntra = stdin.nextInt();
    for (int i=0; i<ntra ; i++){
      int nnos = stdin.nextInt();
      int prev = stdin.nextInt();
      int curr = stdin.nextInt();
      if (g.find_arc(prev,curr)==null){
        g.insert_new_arc(prev,curr);
      }
      for (int j=0;j<nnos-2;j++){
        prev = curr;
        curr=stdin.nextInt();
        if (g.find_arc(prev,curr)==null)
        g.insert_new_arc(prev,curr);
      }
    }
    for(int i=1;i<=g.num_vertices();i++){
      System.out.println(g.adjs_no(i).size());
    }
  }
}
