import java.lang.*;
import java.util.*;

class Qnode {
  int vert;
  int vertkey;

  Qnode(int v, int key) {
    vert = v;
    vertkey = key;
  }
}

class Heapmin {
  private static int posinvalida = 0;
  int sizeMax,size;

  Qnode[] a;
  int[] pos_a;

  Heapmin(int vec[], int n) {
    a = new Qnode[n + 1];
    pos_a = new int[n + 1];
    sizeMax = n;
    size = n;
    for (int i = 1; i <= n; i++) {
      a[i] = new Qnode(i,vec[i]);
      pos_a[i] = i;
    }

    for (int i = n/2; i >= 1; i--)
    heapify(i);
  }

  boolean isEmpty() {
    if (size == 0) return true;
    return false;
  }

  int extractMin() {
    int vertv = a[1].vert;
    swap(1,size);
    pos_a[vertv] = posinvalida;  // assinala vertv como removido
    size--;
    heapify(1);
    return vertv;
  }

  void decreaseKey(int vertv, int newkey) {

    int i = pos_a[vertv];
    a[i].vertkey = newkey;

    while (i > 1 && compare(i, parent(i)) < 0) {
      swap(i, parent(i));
      i = parent(i);
    }
  }


  void insert(int vertv, int key)
  {
    if (sizeMax == size)
    new Error("Heap is full\n");

    size++;
    a[size].vert = vertv;
    pos_a[vertv] = size;   // supondo 1 <= vertv <= n
    decreaseKey(vertv,key);   // diminui a chave e corrige posicao se necessario
  }

  void write_heap(){
    System.out.printf("Max size: %d\n",sizeMax);
    System.out.printf("Current size: %d\n",size);
    System.out.printf("(Vert,Key)\n---------\n");
    for(int i=1; i <= size; i++)
    System.out.printf("(%d,%d)\n",a[i].vert,a[i].vertkey);

    System.out.printf("-------\n(Vert,PosVert)\n---------\n");

    for(int i=1; i <= sizeMax; i++)
    if (pos_valida(pos_a[i]))
    System.out.printf("(%d,%d)\n",i,pos_a[i]);
  }

  private int parent(int i){
    return i/2;
  }
  private int left(int i){
    return 2*i;
  }
  private int right(int i){
    return 2*i+1;
  }

  private int compare(int i, int j) {
    if (a[i].vertkey < a[j].vertkey)
    return -1;
    if (a[i].vertkey == a[j].vertkey)
    return 0;
    return 1;
  }


  private void heapify(int i) {
    int l, r, smallest;

    l = left(i);
    if (l > size) l = i;

    r = right(i);
    if (r > size) r = i;

    smallest = i;
    if (compare(l,smallest) < 0)
    smallest = l;
    if (compare(r,smallest) < 0)
    smallest = r;

    if (i != smallest) {
      swap(i, smallest);
      heapify(smallest);
    }

  }

  private void swap(int i, int j) {
    Qnode aux;
    pos_a[a[i].vert] = j;
    pos_a[a[j].vert] = i;
    aux = a[i];
    a[i] = a[j];
    a[j] = aux;
  }

  private boolean pos_valida(int i) {
    return (i >= 1 && i <= size);
  }
}

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

class Luxo {
  public static int Dijkstra(Grafo2 g,int orig,int fin){
    int[] dist =  new int[g.num_vertices()+1];
    int[] pai = new int[g.num_vertices()+1];
    int cust = 0;
    for(int i=0;i<g.num_vertices()+1;i++){
      dist[i] = 10000000;
    }
    dist[orig] = 0;
    Heapmin h = new Heapmin(dist,g.num_vertices());
    do{
      int v = h.extractMin();
      for(Arco a : g.adjs_no(v)){
        if ((dist[v]+a.valor1) < dist[a.extremo_final()]){
          dist[a.extremo_final()] = dist[v]+a.valor1;
          pai[a.extremo_final()] = v;
          h.decreaseKey(a.extremo_final(),dist[a.extremo_final()]);
        }
      }
    }while(!h.isEmpty());
    return dist[fin];
  }

  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    int tmin = stdin.nextInt();
    int tmax = stdin.nextInt();
    int orig = stdin.nextInt();
    int fin = stdin.nextInt();
    int nos = stdin.nextInt();
    int ramos = stdin.nextInt();
    Grafo2 g = new Grafo2(nos);
    int prev,next,temp,cust;
    for(int i=0;i<ramos;i++){
      prev = stdin.nextInt();
      next = stdin.nextInt();
      temp = stdin.nextInt();
      cust = stdin.nextInt();
      if (temp>=tmin && temp<=tmax){
        g.insert_new_arc(prev,next,temp,cust);
        g.insert_new_arc(next,prev,temp,cust);
      }
    }
    int cenarios = stdin.nextInt();
    int valor = Dijkstra(g,orig,fin);
    int[] max = new int[cenarios];
    for(int i=0;i<cenarios;i++){
      max[i] = stdin.nextInt();
    }
    for(int i=0;i<cenarios;i++){
      if (valor <= max[i]) {
        System.out.println("Sim");
      }
      else {
        System.out.println("Nao");
      }
    }
  }
}
