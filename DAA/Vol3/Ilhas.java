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

class Ilhas{

  public static void BFS(Grafo0 g,int c){
    boolean visitados[] = new boolean[g.num_vertices()];
    Queue<Integer> q = new LinkedList<Integer>();
    q.add(c);
    visitados[c] = true;
    int fin = 0;
    do{
      int v = q.remove();
      for (Arco a: g.adjs_no(v)){
        if (visitados[a.extremo_final()]==false){
          q.add(a.extremo_final());
          fin++;
          visitados[a.extremo_final()] = true;
        }
      }
    }while(!q.isEmpty());
    System.out.println("No "+ c +": " + fin);
  }

  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int nnos = stdin.nextInt();
    int ramos = stdin.nextInt();
    Grafo0 g = new Grafo0(nnos);
    for(int i=0;i<ramos;i++){
      int prev = stdin.nextInt();
      int next = stdin.nextInt();
      if (g.find_arc(prev,next)==null){
        g.insert_new_arc(prev,next);
        g.insert_new_arc(next,prev);
      }
    }
    int n = stdin.nextInt();
    int arr[] = new int[n];
    for(int i=0;i<n;i++){
      arr[i] = stdin.nextInt();
    }
    for(int i=0;i<n;i++){
      BFS(g,arr[i]);
    }
  }
}
