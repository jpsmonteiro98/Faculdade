import java.util.*;
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
    /* Copiar a matriz de um tabuleiro para outro */
    public void mCopy(Board toHere) {
        for(int i=0; i<lines; i++)
            for(int j=0; j<cols; j++)
                toHere.m[i][j] = m[i][j];
    }
    /* Retorna um set de jogadas legais */
    public LinkedList<Integer> actions(Board s) {
        LinkedList<Integer> action = new LinkedList<Integer>();
        for(int i=0; i<s.cols; i++)
            if(s.m[0][i] == '_') action.addLast(i);
        return action;
    }
    /* Modelo transição, define o resultado de um movimento */
    public Board result(Board s, int a, char p) {
        Board aux = new Board(6,7);
        s.mCopy(aux);
        for(int i=s.lines-1; i>=0; i--)
            if(aux.m[i][a] == '_') { aux.m[i][a] = p; return aux; }
        return aux;
    }
    /* Verifica se ganhou true: jogo terminou, false: caso contrário */
    public boolean terminalTest(Board s) {
        int v = utility(s);
        return (v >= 512 || v<=-512);
    }
    /* Valor numérico para um jogo que termina num estado terminal s */
    public int utility(Board s) {
        int x = 0, o = 0, points = 0;
        // Horizontal
        for(int i=0; i<s.lines; i++)
            for(int j=0; j<4; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[i][j+k];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(o==4) return 512; else if(x==4) return -512;
                points += applyPoints(x, o);
            }
        // Vertical
        for(int i=0; i<s.cols; i++)
            for(int j=0; j<3; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[j+k][i];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(o==4) return 512; else if(x==4) return -512;
                points += applyPoints(x, o);
            }
        // Diagonal principal
        for(int i=0; i<3; i++)
            for(int j=0; j<4; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[i+k][j+k];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(o==4) return 512; else if(x==4) return -512;
                points += applyPoints(x, o);
            }
        // Diagonal secundária
        for(int i=0; i<3; i++)
            for(int j=3; j<s.cols; j++) {
                o=0; x=0;
                for(int k=0; k<4; k++) {
                    char p=s.m[i+k][j-k];
                    if(p=='O') o++; else if(p=='X') x++;
                }
                if(o==4) return 512; else if(x==4) return -512;
                points += applyPoints(x,o);
            }
        if( == 'X') points+=-16; else points+=16;
        return points;
    }
    public int applyPoints(int p1, int p2) {
        if(p1 == 1 && p2 == 0) return -1;
        if(p1 == 2 && p2 == 0) return -10;
        if(p1 == 3 && p2 == 0) return -50;
        if(p2 == 1 && p1 == 0) return 1;
        if(p2 == 2 && p1 == 0) return 10;
        if(p2 == 3 && p1 == 0) return 50;
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
        char player = 'a';
        System.out.println("");
        int whoStarts;
        int winner = 0;
        System.out.println("Who's first? You or PC?");
        System.out.println("[1] You");
        System.out.println("[2] PC");
        whoStarts = in.nextInt();
        switch(whoStarts) {
            case 1:
                player = 'X';
                break;
            case 2:
                player = 'O';
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
                winner = game(s, "minimax", player, in);
                break;
            /*case 2:
                winner = game(s, "alphabeta", player, in);
                break;
            case 3:
                winner = game(s, "mcts", player, in);
                break;*/
            default:
                System.out.println("Invalid option!");
                break;
        }
        return winner;
    }
    /* Jogo */
    public int game(Board s, String algorithm, char player, Scanner in) {
        System.out.println("");
        int v = utility(s);
        int option = 0;
        System.out.println("Utility value: " + v);
        if(actions(s).isEmpty()) {
            System.out.println("More luck next time!");
            printBoard(s);
            return 0;
        } else {
            if(player == 'X') {
                System.out.println("Your turn");
                printBoard(s);
                System.out.print("Actions avaiable: "); for(int a : actions(s)) System.out.print(a + " ");
                System.out.println("");
                System.out.print("Where to play? "); option = in.nextInt();
                s = result(s,option, player);
                player = 'O';
            } else {
                System.out.println("AI's turn");
                printBoard(s);
                System.out.println("Awaiting for AI to play...");
                if(algorithm.equals("minimax"))
                    option = miniMaxDecision(s, player);
                /*else if(algorithm.equals("alphabeta"))
                    option = alphabeta(s);
                else if(algorithm.equals("mcts"))
                    option = MCTS(s);*/
                s = result(s, option, player);
                System.out.println("AI Option: " + option);
                player = 'X';
            }
        }
        if(terminalTest(s) && utility(s)>0) {
            System.out.println("");
            System.out.println("Utility value: " + utility(s));
            printBoard(s);
            return 1;
        } else if(terminalTest(s) && utility(s)<0) {
            System.out.println("");
            System.out.println("Utility value: " + utility(s));
            printBoard(s);
            return -1;
        } else return game(s, algorithm, player, in);
    }
    /* MiniMax */
    public int miniMaxDecision(Board s, char p) {
        int max = -9999;
        int action = 0;
        long startTime = System.currentTimeMillis();
        for(Integer a: actions(s)) {
            int v = maxValue(result(s, a, p), 8, p);
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
    public int maxValue(Board s, int depth, char p) {
        p = 'X';
        if(terminalTest(s)) return 9999;
        if(depth == 0) return utility(s);
        if(actions(s).isEmpty()) return 0;
        int v = -9999;
        for(Integer a : actions(s))
            v = Math.max(v, minValue(result(s, a, p), depth-1, p));
        return v;
    }
    public int minValue(Board s, int depth, char p) {
        p = 'O';
        if(terminalTest(s)) return -9999;
        if(depth == 0) return utility(s);
        if(actions(s).isEmpty()) return 0;
        int v = 9999;
        for(Integer a : actions(s))
            v = Math.min(v, maxValue(result(s, a, p), depth-1, p));
        return v;
    }
}

class CFour {
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