import java.util.*;

class Arco {
    int no_final;
    int valor0;
    int valor1;

    Arco(int fim, int v0, int v1){
	no_final = fim;
	valor0  = v0;
	valor1 = v1;
    }

    int extremo_final() {
	return no_final;
    }

    int valor0_arco() {
	return valor0;
    }

    int valor1_arco() {
	return valor1;
    }

    void novo_valor0(int v) {
	valor0 = v;
    }

    void novo_valor1(int v) {
	valor1 = v;
    }
}


class No {
    //int label;
    LinkedList<Arco> adjs;

    No() {
	adjs = new LinkedList<Arco>();
    }
}


class Grafo2 {
    No verts[];
    int nvs, narcos;

    public Grafo2(int n) {
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

    public void insert_new_arc(int i, int j, int valor0, int valor1){
	verts[i].adjs.addFirst(new Arco(j,valor0,valor1));
        narcos++;
    }

    public Arco find_arc(int i, int j){
	for (Arco adj: adjs_no(i))
	    if (adj.extremo_final() == j) return adj;
	return null;
    }
}

class Bacalhaus {
  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int nos = stdin.nextInt();
    Grafo2 g = new Grafo2(nos);
    int ntro = stdin.nextInt();
    for (int i=0;i<ntro;i++){
      int prev = stdin.nextInt();
      int next = stdin.nextInt();
      int v1 = stdin.nextInt();
      int v2 = stdin.nextInt();
      g.insert_new_arc(prev,next,v1,v2);
      g.insert_new_arc(next,prev,v1,v2);
    }
    int n = 1;

    while ( n!=0 ) {
      n = stdin.nextInt();
      prev = stdin.nextInt();
      for(int i=0;i<n;i++){
        next = stdin.nextInt();
        int aux = g.valor0.find_arc(prev,next)
        if (aux );
      }
    }
  }
}
