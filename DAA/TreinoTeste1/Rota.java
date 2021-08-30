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

class Rota {
  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int elem = stdin.nextInt();
    int orig = stdin.nextInt();
    int dest = stdin.nextInt();
    int nnos = stdin.nextInt();
    int ramos = stdin.nextInt();
    Grafo2 g = new Grafo2(nnos);
    for (int i=0;i<ramos;i++){
      int prev = stdin.nextInt();
      int next = stdin.nextInt();
      int prob = stdin.nextInt();
      if (g.find_arc(prev,next)==null){
        g.insert_new_arc(prev,next,prob,0);
      }
    }
    int nrotas = stdin.nextInt();
    for (int i=0;i<nrotas;i++){
      int k = stdin.nextInt();
      for (int j=0;j<k;j++){
        int prev = stdin.nextInt();
        int lugares = stdin.nextInt();
        int next = stdin.nextInt();
        g.find_arc(prev,next).novo_valor1(lugares);
        for(int t=0;t<k-1;t++){
          prev = next;
          lugares = stdin.nextInt();
          next = stdin.nextInt();
          g.find_arc(prev,next).novo_valor1(lugares);
        }
      }
    }
  }
}
