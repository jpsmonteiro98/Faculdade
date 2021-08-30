import java.util.*;
/* Criada uma classe tabuleiro para guardar diversos valores importantes para a análise dos tabuleiros */
class greedyComparator implements Comparator<Table>{
    public int compare(Table s1, Table s2) {
        if (s1.manhattanValue > s2.manhattanValue)
            return 1;
        else if (s1.manhattanValue < s2.manhattanValue)
            return -1;
        return 0;
    }
}
class aStarComparator implements Comparator<Table>{
    public int compare(Table s1, Table s2) {
        if (s1.manhattanValue + s1.parent.path_cost > s2.manhattanValue + s2.parent.path_cost)
            return 1;
        else if (s1.manhattanValue < s2.manhattanValue)
            return -1;
        return 0;
    }
}
class Table {
    int[] state = new int[16]; // Guarda os valores em cada peça do tabuleiro
    Table parent; // Guarda o tabuleiro pai do tabuleiro atual
    String action; // Guarda o movimento feito para chegar ao tabuleiro atual (Cima, Baixo, Esquerda, Direita)
    int path_cost; // Guarda a quantidade de movimentos feito para chegar ao tabuleiro atual
    int outOfPlace; // Guarda o valor da heuristica das peças fora do lugar
    int manhattanValue; // Guarda o valor da heuristica de manhattan
    int edSize; //Ira guardar na altura de resposta o valor de capacidade maxima que a estrutura usada (queue ou stack) teve de elementos
    /* Criação do node, colocando no array state os valores lidos para o tabuleiro */
    public Table(int[] config) {
        for(int i=0; i<16; i++)
            state[i] = config[i];
    }
    /* Quantro funções para mover as peças, em cada uma delas cria um novo tabuleiro com a movimentação já feita,
       e com todos os outros dados atualizados descritos acima. */
    public Table moveUp(Table finalTable) {
        Table newTable = new Table(this.state);
        int blankSpace = newTable.getBlank();
        int newPos = blankSpace-4;
        int aux = newTable.state[blankSpace];
        newTable.state[blankSpace] = newTable.state[newPos];
        newTable.state[newPos] = aux;
        newTable.action = "Cima";
        newTable.path_cost = this.path_cost + 1;
        newTable.parent = this;
        newTable.outOfPlace = missMatch(newTable, finalTable);
        newTable.manhattanValue = manhattan(newTable, finalTable);
        return newTable;
    }
    public Table moveDown(Table finalTable) {
        Table newTable = new Table(this.state);
        int blankSpace = newTable.getBlank();
        int newPos = blankSpace+4;
        int aux = newTable.state[blankSpace];
        newTable.state[blankSpace] = newTable.state[newPos];
        newTable.state[newPos] = aux;
        newTable.action = "Baixo";
        newTable.path_cost = this.path_cost + 1;
        newTable.parent = this;
        newTable.outOfPlace = missMatch(newTable, finalTable);
        newTable.manhattanValue = manhattan(newTable, finalTable);
        return newTable;
    }
    public Table moveLeft(Table finalTable) {
        Table newTable = new Table(this.state);
        int blankSpace = newTable.getBlank();
        int newPos = blankSpace-1;
        int aux = newTable.state[blankSpace];
        newTable.state[blankSpace] = newTable.state[newPos];
        newTable.state[newPos] = aux;
        newTable.action = "Esquerda";
        newTable.path_cost = this.path_cost + 1;
        newTable.parent = this;
        newTable.outOfPlace = missMatch(newTable, finalTable);
        newTable.manhattanValue = manhattan(newTable, finalTable);
        return newTable;
    }
    public Table moveRight(Table finalTable) {
        Table newTable = new Table(this.state);
        int blankSpace = newTable.getBlank();
        int newPos = blankSpace+1;
        int aux = newTable.state[blankSpace];
        newTable.state[blankSpace] = newTable.state[newPos];
        newTable.state[newPos] = aux;
        newTable.action = "Direita";
        newTable.path_cost = this.path_cost + 1;
        newTable.parent = this;
        newTable.outOfPlace = missMatch(newTable, finalTable);
        newTable.manhattanValue = manhattan(newTable, finalTable);
        return newTable;
    }
    /* Retorna a posicao da peça neutra/branca */
    public int getBlank() {
        int aux = 0;
        for(int i=0; i<16; i++)
            if(this.state[i] == 0)
                aux = i;
        return aux;
    }
    /* Compara duas tables e retorna se sao iguais ou nao */
    public boolean tableEquals(Table finalTable) {
        for(int i=0; i<16; i++)
            if(this.state[i] != finalTable.state[i])
                return false;
        return true;
    }
    /* Heuristica de peças fora do lugar */
    public static int missMatch(Table node, Table finalTable) {
        int ctr = 0;
        for(int i=0; i<15; i++)
            if(node.state[i] != 0)
                if(node.state[i] != finalTable.state[i])
                    ctr++;
        return ctr;
    }
    /* Heuristica de Manhattan */
    public static int manhattan(Table node, Table finalTable) {
        int sum = 0;
        for(int i=0; i<16; i++)
            if(node.state[i]!=0) {
                int coll = getColl(i);
                int row = getRow(i);
                int numberInFinal = finalTable.getPos(node.state[i]);
                int trueColl = getColl(numberInFinal);
                int trueRow = getRow(numberInFinal);
                sum += (Math.abs(trueColl - coll) + Math.abs(trueRow - row));
            }
        return sum;
    }
    public static int getColl(int number) {
        if(number == 0 || number == 4 || number == 8 || number == 12)
            return 1;
        else if(number == 1 || number == 5 || number == 9 || number == 13)
            return 2;
        else if(number == 2 || number == 6 || number == 10 || number == 14)
            return 3;
        else
            return 4;
    }
    public static int getRow(int number) {
        if(number >= 0 && number <= 3)
            return 1;
        else if(number >= 4 && number <=7)
            return 2;
        else if(number >= 8 && number <=11)
            return 3;
        else
            return 4;
    }
    public int getPos(int number) {
        for(int i=0; i<16; i++)
            if(this.state[i] == number)
                return i;
        return 0;
    }
}

class Npuzzle {
    /* Funcao que retorna, quando atingida a solucao, a quantidade de movimentos para chegar a solucao,
       e o caminho feito para lá chegar. */
    public static void solution (Table node, long totalTime) {
        System.out.print("Quantidade de movimentos para chegar à solução: " + node.path_cost);
        System.out.println("");
        String[] path = new String[node.path_cost];
        path[node.path_cost-1] = node.action;
        if(node.path_cost != 1) {
            Table aux = node.parent;
            for(int i=node.path_cost-2; i>=0; i--) {
                path[i] = aux.action;
                aux = aux.parent;
            }
        }
        //System.out.print("Caminho para solucao: " + Arrays.toString(path));
        //System.out.println("");
        System.out.println("Quantidade de espaço gasto: " + node.edSize + " tabuleiros");
        System.out.println("Tempo: " + totalTime + " ms");
    }
    /* Funcao que recebe um tabuleiro e procura os seus descendentes, guardado-os numa Lista ligada */
    public static LinkedList<Table> descendantList(Table node, Table finalTable) {
        int whereZero = node.getBlank();
        LinkedList<Table> list = new LinkedList<Table>();
        if(whereZero >= 4) {
            Table newTable = node.moveUp(finalTable);
            list.addLast(newTable);
        }
        if(whereZero%4 != 0) {
            Table newTable = node.moveLeft(finalTable);
            list.addLast(newTable);
        }
        if((whereZero+1)%4 != 0) {
            Table newTable = node.moveRight(finalTable);
            list.addLast(newTable);
        }
        if(whereZero <= 11) {
            Table newTable = node.moveDown(finalTable);
            list.addLast(newTable);
        }
        return list;
    }
    /* Algoritmo de pesquisa em largura */
    public static Table BFS(Table initTable, Table finalTable) {
        Queue<Table> frontier = new LinkedList<Table>();
        Set<String> explored = new HashSet<String>();
        int queueSize = 0;
        frontier.add(initTable);
        explored.add(Arrays.toString(initTable.state));
        queueSize++;
        while(!frontier.isEmpty()) {
            Table node = frontier.remove();
            LinkedList<Table> nodeList = new LinkedList<Table>();
            nodeList = descendantList(node, finalTable);
            while(!nodeList.isEmpty()) {
                Table child = nodeList.removeLast();
                if(!explored.contains(Arrays.toString(child.state))) {
                    queueSize++;
                    if(child.tableEquals(finalTable)) {
                        child.edSize = queueSize;
                        return child;
                    }
                    frontier.add(child);
                }
            }
        }
        return null;
    }
    /* Algoritmo de pesquisa em profundidade (limitado a uma profundidade já predefinida no codigo) */
    public static Table DFS(Table initTable, Table finalTable, int depth) {
        Stack<Table> frontier = new Stack<Table>();
        Set<String> explored = new HashSet<String>();
        int listSize = 1;
        frontier.push(initTable);
        explored.add(Arrays.toString(initTable.state));
        listSize++;
        while(!frontier.isEmpty()) {
            Table node = frontier.pop();
            if(node.path_cost <= depth) {
                explored.add(Arrays.toString(node.state));
                listSize--;
                if(node.tableEquals(finalTable)) {
                    node.edSize = listSize;
                    return node;
                } else {
                    LinkedList<Table> nodeList = new LinkedList<Table>();
                    nodeList = descendantList(node, finalTable);
                    for(int i=0; i<nodeList.size()-1; i++) {
                        if(!explored.contains(Arrays.toString(nodeList.getFirst().state))) {
                            frontier.push(nodeList.removeFirst());
                            listSize++;
                        }
                    }
                    while(!nodeList.isEmpty()) {
                        Table child = nodeList.removeFirst();
                        if(child.tableEquals(finalTable)) {
                            child.edSize = listSize;
                            return child;
                        } else {
                            frontier.push(child);
                            listSize++;
                        }
                    }
                }
            }
        }
        return null;
    }
    /* Algoritmo de busca iterativa em profundidade */
    public static Table ittDFS(Table initTable, Table finalTable) {
        for(int depth = 0;; depth++) {
            Table result = DFS(initTable, finalTable, depth);
            if(result != null && result.tableEquals(finalTable))
                return result;
        }
    }
/*  Algoritmo greedy com heurística de manhattan
    Guarda os valores numa priority queue com um comparador que organiza de forma
    ascendente de valor de f(n) a queue

    Algoritmo A* com heurística de manhattan + custo de caminho do pai
    Guarda os valores numa priority queue com um comparador que organiza de forma
    ascendente de valor de f(n) = h(n) + g(n) a queue */
    public static Table greedy(Table node, Table finalTable) {
        PriorityQueue<Table> frontier = new PriorityQueue<Table>(4, new greedyComparator());
        int priorityQueue = 1;
        frontier.add(node);
        priorityQueue++;
        Set<String> explored = new HashSet<String>();
        while(!frontier.isEmpty()) {
            Table aux = frontier.remove();
            priorityQueue--;
            explored.add(Arrays.toString(aux.state));
            PriorityQueue<Table> sucessors = new PriorityQueue<Table>(4, new greedyComparator());
            sucessors = descendantQueue(aux, finalTable);
            while(!sucessors.isEmpty()) {
                Table child = sucessors.remove();
                if(!explored.contains(Arrays.toString(child.state))) {
                    if(child.tableEquals(finalTable)) {
                        child.edSize = priorityQueue;
                        return child;
                    }
                    frontier.add(child);
                    priorityQueue++;
                }
            }
        }
        return null;
    }
    public static Table aStar(Table node, Table finalTable) {
        PriorityQueue<Table> frontier = new PriorityQueue<Table>(4, new aStarComparator());
        int priorityQueue = 1;
        frontier.add(node);
        priorityQueue++;
        Set<String> explored = new HashSet<String>();
        while(!frontier.isEmpty()) {
            Table aux = frontier.remove();
            priorityQueue--;
            explored.add(Arrays.toString(aux.state));
            PriorityQueue<Table> sucessors = new PriorityQueue<Table>(4, new aStarComparator());
            sucessors = descendantQueue(aux, finalTable);
            while(!sucessors.isEmpty()) {
                Table child = sucessors.remove();
                if(!explored.contains(Arrays.toString(child.state))) {
                    if(child.tableEquals(finalTable)) {
                        child.edSize = priorityQueue;
                        return child;
                    }
                    priorityQueue++;
                    frontier.add(child);
                }
            }
        }
        return null;
    }
    public static PriorityQueue<Table> descendantQueue(Table node, Table finalTable) {
        int whereZero = node.getBlank();
        PriorityQueue<Table> queueP = new PriorityQueue<Table>(4, new greedyComparator());
        if(whereZero >= 4) {
            Table newTable = node.moveUp(finalTable);
            queueP.add(newTable);
        }
        if(whereZero%4 != 0) {
            Table newTable = node.moveLeft(finalTable);
            queueP.add(newTable);
        }
        if((whereZero+1)%4 != 0) {
            Table newTable = node.moveRight(finalTable);
            queueP.add(newTable);
        }
        if(whereZero <= 11) {
            Table newTable = node.moveDown(finalTable);
            queueP.add(newTable);
        }
        return queueP;
    }
    /* Funcao de input do utilizador par escolher qual algoritmo quer usar para resolver o problema
       tambem calcula os tempos de execucao e a memoria usada no programa */
    public static void selectOption(Table initTable, Table finalTable) {
        long startTime;
        long endTime;
        long totalTime;
        Scanner scan = new Scanner(System.in);
        int optionAlgorithm = 0;
        System.out.println("        Que algoritmo quer usar para resolver o puzzle?");
        System.out.println("            [1] BFS");
        System.out.println("            [2] DFS");
        System.out.println("            [3] Iteractive DFS");
        System.out.println("            [4] A* (Manhattan)");
        System.out.println("            [5] Gulosa (greedy) Manhattan");
        System.out.print("            Opcao: ");
        optionAlgorithm = scan.nextInt();
        switch(optionAlgorithm) {
            case 1 :
                startTime = System.currentTimeMillis();
                Table resultBFS = BFS(initTable, finalTable);
                endTime   = System.currentTimeMillis();
                totalTime = (endTime - startTime);
                solution(resultBFS, totalTime);
                break;
            case 2 :
                startTime = System.currentTimeMillis();
                Table resultDLS = DFS(initTable, finalTable, 25);
                endTime   = System.currentTimeMillis();
                totalTime = (endTime - startTime);
                solution(resultDLS, totalTime);
                break;
            case 3 :
                startTime = System.currentTimeMillis();
                Table resultittDFS = ittDFS(initTable, finalTable);
                endTime   = System.currentTimeMillis();
                totalTime = (endTime - startTime);
                solution(resultittDFS, totalTime);
                break;
            case 4 :
                startTime = System.currentTimeMillis();
                Table resultAstar = aStar(initTable, finalTable);
                endTime   = System.currentTimeMillis();
                totalTime = (endTime - startTime);
                solution(resultAstar, totalTime);
                break;
            case 5 :
                startTime = System.currentTimeMillis();
                Table resultGreedy = greedy(initTable, finalTable);
                endTime   = System.currentTimeMillis();
                totalTime = (endTime - startTime);
                solution(resultGreedy, totalTime);
                break;
            default :
                System.out.println("Erro, essa opcao nao e valida.");
                break;
        }
        scan.close();
    }
    /* Conta as inversoes para auxiliar na analise de solvabilidade dos tabuleiros */
    public static int getInversions(Table node) {
        int inversions=0;
        for(int i=0; i<16; i++)
            if(node.state[i] != 0)
                for(int j=i+1; j<16; j++)
                    if(node.state[j] != 0)
                        if(node.state[i] > node.state[j])
                            inversions++;
        return inversions;
    }
    /* Funcao que verifica se e possivel chegar a solucao com os tabuleiros dados (solvabilidade) */
    public static boolean solvable(Table node) {
        int blank = node.getBlank();
        int inversion = getInversions(node);
        if((blank >= 4 && blank <=7) || (blank >= 12 && blank <= 15)) {//posição par
            if(inversion%2 == 0)
                return true;
        } else { //posição impar
            if(inversion%2 != 0)
                return true;
        }
        return false;
    }
    /* Inicializador do programa */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //Table que guarda a configuração inicial
        int[] configInit = new int[16];
        for(int i=0; i<16; i++) configInit[i] = scan.nextInt();
        Table initTable = new Table(configInit);
        int[] configFinal = new int[16];
        for(int i=0; i<16; i++) configFinal[i] = scan.nextInt();
        Table finalTable = new Table(configFinal);
        if(initTable.tableEquals(finalTable)) {
            System.out.println("Os tabuleiros são iguais.");
            scan.close();
            return;
        }
        if(solvable(initTable) && solvable(finalTable)) selectOption(initTable, finalTable);
        else System.out.println("O puzzle não passou aos testes de solvabilidade.");
        scan.close();
    }
}
