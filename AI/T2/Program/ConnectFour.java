import java.util.*;
import java.lang.*;
class Node {
    Board state;
    Node parent;
    int visitCount;
    double winScore;
    List<Node> childArray;
    Node(Board s) {
        state = s;
        visitCount = 0;
        parent = null;
        childArray = new ArrayList<Node>();
    }
    /* Verifica se o nó em que nos encontramos é folha, ou seja, não tem filhos */
    public boolean isLeaf() {
        if(childArray.isEmpty())
            return true;
        return false;
    }
    /* Retorna o USB que é necessário para avançar */
    public Node getUCB(List<Node> childs) {
        double max = -99999;
        double ucb = 0;
        Node op = null;
        int n = this.visitCount;
        for(Node child : this.childArray) {
            if(child.visitCount == 0) ucb = 99999;
            else {
                ucb = (child.winScore/child.visitCount) + 2*Math.sqrt(Math.log(n)/child.visitCount);
            }
            if(max < ucb) {
                max = ucb;
                op = child;
            }
        }
        return op;
    }
    /* Retorna o valor de quantas vezes foi visitado do root */
    public int getNValue() {
        Node aux = this;
        while(aux.parent!=null)
            aux = aux.parent;
        return visitCount;
    }
    /* Retorna os filhos de um determinado nó */
    public void getChild() {
        List<Node> childs = new ArrayList<Node>();
        for(Integer a : state.actions(state)) {
            Node aux = new Node(state.result(state, a));
            aux.parent = this;
            childs.add(aux);
        }
        this.childArray = childs;
    }
    /* Retorna o número de visitados do nó em que estamos */
    public int getniValue() {
        return visitCount;
    }
    /* Retorna o primeiro filho de um nó */
    public Node getfstNewChild() {
        if(!childArray.isEmpty())
            return childArray.get(0);
        else    
            return this;
    }
    /* Retorna a ação para o qual o UCB é o melhor */
    public int getBest(Node root) {
        double max = -99999;
        double ucb = 0;
        int n = root.visitCount;
        int index = 0;
        int i = 0;
        for(Node child : root.childArray) {
            if(child.visitCount == 0) ucb = 99999;
            else {
                ucb = (child.winScore/child.visitCount) + 2*Math.sqrt(Math.log(n)/child.visitCount);
            }
            if(max < ucb) {
                max = ucb;
                index = i;
            }
            i++;
        }
        return index;
    }
}
class Board {
    char m[][] = {{'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'}};
    int lines, cols;
    Board(int l, int c) {
        lines = l;
        cols = c;
    }
    /* Devolve um valor quando encontra um terminal test. se O ganhar retorna 1, senão retorna 0 */
    public int value(Board s) {
        if(actions(s).isEmpty()) return 0;
        else if(player(s) == 'X') return 1;
        else return -1;
    }
    /* Copiar a matriz de um tabuleiro para outro */
    public void mCopy(Board toHere) {
        for(int i=0; i<lines; i++)
            for(int j=0; j<cols; j++)
                toHere.m[i][j] = m[i][j];
    }
    /* Que jogador joga no estado s */
    public char player(Board s) {
        int p1 = 0, p2 = 0;
        for(int i=0; i<s.lines; i++)
            for(int j=0; j<s.cols; j++) {
                if(s.m[i][j] == 'X') p1++;
                if(s.m[i][j] == 'O') p2++;
            }
        if(p1 > p2) return 'O';
        else return 'X';
    }
    /* Retorna um set de jogadas legais */
    public LinkedList<Integer> actions(Board s) {
        LinkedList<Integer> action = new LinkedList<Integer>();
        for(int i=0; i<s.cols; i++)
            if(s.m[0][i] == '_') action.addLast(i);
        return action;
    }
    /* Modelo transição, define o resultado de um movimento */
    public Board result(Board s, int a) {
        Board aux = new Board(6,7);
        s.mCopy(aux);
        char p = player(s);
        for(int i=s.lines-1; i>=0; i--)
            if(aux.m[i][a] == '_') { aux.m[i][a] = p; return aux; }
        return aux;
    }
    /* Verifica se ganhou true: jogo terminou, false: caso contrário */
    public boolean terminalTest(Board s, boolean pc) {
        int v = utility(s, pc);
        if(v >= 512 || v <= -512) { return true;  }
        return false;
    }
    /* Valor numérico para um jogo que termina num estado terminal s */
    public int utility(Board s, boolean pc) {
        int x = 0, o = 0, points = 0;
        // Horizontal
        for(int i=0; i<s.lines; i++)
            for(int j=0; j<4; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[i][j+k];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(!pc)
                    if(o==4) return 512; else if(x==4) return -512;
                else
                    if(o==4) return -512; else if(x==4) return 512;
                points += applyPoints(x, o, pc);
            }
        // Vertical
        for(int i=0; i<s.cols; i++)
            for(int j=0; j<3; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[j+k][i];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(!pc)
                    if(o==4) return 512; else if(x==4) return -512;
                else
                    if(o==4) return -512; else if(x==4) return 512;
                points += applyPoints(x, o, pc);
            }
        // Diagonal principal
        for(int i=0; i<3; i++)
            for(int j=0; j<4; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[i+k][j+k];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(!pc)
                    if(o==4) return 512; else if(x==4) return -512;
                else
                    if(o==4) return -512; else if(x==4) return 512;
                points += applyPoints(x, o, pc);
            }
        // Diagonal secundária
        for(int i=0; i<3; i++)
            for(int j=3; j<s.cols; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[i+k][j-k];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(!pc)
                    if(o==4) return 512; else if(x==4) return -512;
                else
                    if(o==4) return -512; else if(x==4) return 512;
                points += applyPoints(x, o, pc);
            }
        //if(player(s) == 'X') points+=-16; else points+=16;
        return points;
    }
    public int applyPoints(int p1, int p2, boolean pc) {
        if(!pc) 
            if(p1 == 1 && p2 == 0) return -1;
            if(p1 == 2 && p2 == 0) return -10;
            if(p1 == 3 && p2 == 0) return -50;
            if(p2 == 1 && p1 == 0) return 1;
            if(p2 == 2 && p1 == 0) return 10;
            if(p2 == 3 && p1 == 0) return 50;
        else if(pc)
            if(p1 == 1 && p2 == 0) return 1;
            if(p1 == 2 && p2 == 0) return 10;
            if(p1 == 3 && p2 == 0) return 50;
            if(p2 == 1 && p1 == 0) return -1;
            if(p2 == 2 && p1 == 0) return -10;
            if(p2 == 3 && p1 == 0) return -50;
        return 0;
    }
    /* Imprime o tabuleiro na consola */
    public void printBoard(Board s) {
        for(int i=0; i<s.lines; i++) {
            for(int j=0; j<s.cols; j++)
                System.out.print(" | " + s.m[i][j]);
            System.out.println(" | ");
        }
        System.out.println("");
        System.out.println(" | 0 | 1 | 2 | 3 | 4 | 5 | 6 | ");
    }
    /* Escolha das definições do jogo */
    public int gameStart(Board s, Scanner in) {
        System.out.println("");
        boolean pc = false;
        int whoStarts;
        int winner = 0;
        System.out.println("Who's first? You or PC?");
        System.out.println("[1] You");
        System.out.println("[2] PC");
        whoStarts = in.nextInt();
        switch(whoStarts) {
            case 1:
                pc = false;
                break;
            case 2:
                pc = true;
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }

        System.out.println("");
        int algorithm;
        System.out.println("What algorithm to use?");
        System.out.println("[1] MiniMax");
        System.out.println("[2] AlphaBeta");
        System.out.println("[3] Monte Carlo Tree Search (MCTS)");
        algorithm = in.nextInt();
        switch(algorithm) {
            case 1:
                winner = game(s, "minimax", pc, in);
                break;
            case 2:
                winner = game(s, "alphabeta", pc, in);
                break;
            case 3:
                winner = game(s, "mcts", pc, in);
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
        return winner;
    }
    /* Jogo */
    public int game(Board s, String algorithm, boolean pc, Scanner in) {
        System.out.println("");
        int v = utility(s, pc);
        int option = 0;
        System.out.println("Utility value: " + v);
        if(actions(s).isEmpty()) {
            System.out.println("More luck next time!");
            printBoard(s);
            return 0;
        } else {
            if(!pc) {
                System.out.println("Your turn");
                printBoard(s);
                System.out.print("Actions avaiable: "); for(int a : actions(s)) System.out.print(a + " ");
                System.out.println("");
                System.out.print("Where to play? "); option = in.nextInt();
                s = result(s,option);
            } else {
                System.out.println("AI's turn");
                printBoard(s);
                System.out.println("Awaiting for AI to play...");
                if(algorithm.equals("minimax"))
                    option = miniMaxDecision(s, pc);
                else if(algorithm.equals("alphabeta"))
                    option = alphabeta(s, pc);
                else if(algorithm.equals("mcts"))
                    option = MCTS(s, pc);
                s = result(s, option);
                System.out.println("AI Option: " + option);
            }
        }
        if(terminalTest(s, pc) && utility(s, pc)>0) {
            System.out.println("");
            System.out.println("Utility value: " + utility(s, pc));
            printBoard(s);
            return 1;
        } else if(terminalTest(s, pc) && utility(s, pc)<0) {
            System.out.println("");
            System.out.println("Utility value: " + utility(s, pc));
            printBoard(s);
            return -1;
        } else return game(s, algorithm, !pc, in);
    }
    /* MiniMax */
    public int miniMaxDecision(Board s, boolean pc) {
        int max = -9999;
        int action = 0;
        long startTime = System.currentTimeMillis();
        for(Integer a: actions(s)) {
            if(terminalTest(result(s,a), pc)) return a;
            int v = maxValue(result(s, a), 8, pc);
            if(v >= max) {
                max = v;
                action = a;
            }
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Tempo:" + totalTime);
        return action;
    }
    public int maxValue(Board s, int depth, boolean pc) {
        if(terminalTest(s, pc)) return 9999;
        if(depth == 0) return utility(s, pc);
        if(actions(s).isEmpty()) return 0;
        int v = -9999;
        for(Integer a : actions(s))
            v = Math.max(v, minValue(result(s, a), depth-1, pc));
        return v;
    }
    public int minValue(Board s, int depth, boolean pc) {
        if(terminalTest(s, pc)) return -9999;
        if(depth == 0) return utility(s, pc);
        if(actions(s).isEmpty()) return 0;
        int v = 9999;
        for(Integer a : actions(s))
            v = Math.min(v, maxValue(result(s, a), depth-1, pc));
        return v;
    }
    public int alphabeta(Board s, boolean pc) {
        int max = -9999;
        int action = 0;
        long startTime = System.currentTimeMillis();
        for(Integer a: actions(s)) {
            if(terminalTest(result(s,a), pc)) return a;
            int v = maxValueAB(result(s, a), -9999, 9999, 11, pc);
            if(v >= max) {
                max = v;
                action = a;
            }
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Tempo:" + totalTime);
        return action;
    }
    public int maxValueAB(Board s, int alpha, int beta, int depth, boolean pc) {
        if(terminalTest(s, pc)) return 9999;
        if(depth == 0) return utility(s, pc);
        if(actions(s).isEmpty()) return 0;
        int v = -9999;
        for(Integer a : actions(s))
            v = Math.max(v, minValueAB(result(s, a),alpha, beta, depth-1, pc));
            if(v>=beta) return v;
            alpha = Math.max(alpha, v);
        return v;
    }
    public int minValueAB(Board s, int alpha, int beta, int depth, boolean pc) {
        if(terminalTest(s, pc)) return -9999;
        if(depth == 0) return utility(s, pc);
        if(actions(s).isEmpty()) return 0;
        int v = 9999;
        for(Integer a : actions(s))
            v = Math.min(v, maxValueAB(result(s, a),alpha, beta, depth-1, pc));
            if(v<=alpha) return v;
            beta = Math.min(beta, v);
        return v;
    }
    public int MCTS(Board s, boolean pc) {
        long startTime = System.currentTimeMillis();
        Node current = new Node(s);
        Node root = current;
        current.getChild();
        int v = 0;
        for(int i=0; i<1000000; i++) {
            current = root;
            while(!current.isLeaf())
                current = current.getUCB(current.childArray);
            if(current.getniValue() != 0) {
                current.getChild();
                current = current.getfstNewChild();
                v = rollout(current, pc);
            } else
                v = rollout(current, pc);
            while(current != null) {
                current.visitCount++;
                current.winScore+=v;
                current = current.parent;
            }
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Tempo:" + totalTime);
        return root.getBest(root);
    }
    public int rollout(Node current, boolean pc) {
        Board s = current.state;
        while(!terminalTest(s, pc) || actions(s).isEmpty()) {
            int action = random(actions(s));
            if(action == 7) return value(s);
            s = result(s, action);
        }
        return value(s);
    } 
    public int random(LinkedList<Integer> actions) {
        if(actions.isEmpty()) return 7;
        Random rand = new Random();
        return actions.get(rand.nextInt(actions.size()));
    }
}
class ConnectFour {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Board newGame = new Board(6,7);
        int winner = newGame.gameStart(newGame, scan);
        if(winner == 0) System.out.println("It's a DRAW");
        else if(winner == -1) System.out.println("You win!");
        else System.out.println("PC wins!");
        scan.close();
    }
}