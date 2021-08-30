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

  public static int BFS (int s, Grafo0 g, int nabob[]){
    boolean visitados[] = new boolean[g.num_vertices()+1];
    int pai[] = new int[g.num_vertices()+1];
    int max;
    for (int i=1;i<=g.num_vertices();i++){
      visitados[i] = false;
      pai[i] = 0;
    }
    visitados[s] = true;
    Queue<Integer> q = new LinkedList<>();
    q.add(s);
    do{
      int aux = q.peek();
      for(Arco a:g.adjs_no(aux)){
        if (!visitados[a.extremo_final()]) {
          q.add(a.extremo_final());
          visitados[a.extremo_final()] = true;
          pai[a.extremo_final()] = aux;
          if (max<=nabob[a.extremo_final()]){
            max = nabob[aux];
          }
        }
      }
    }while(!q.isEmpty());
    return max;
  }

  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int nsuper = stdin.nextInt();
    int nabob[] = new int[nsuper+1];
    Grafo0 g = new Grafo0(nsuper);
    for (int i=1;i<nsuper+1;i++){
      nabob[i] = stdin.nextInt();
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
    int casos = stdin.nextInt();
    for (int i=0;i<casos;i++){
      System.out.println(BFS(stdin.nextInt(),g,nabob));
    }
  }
}
