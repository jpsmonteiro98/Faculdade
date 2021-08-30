import java.util.*;
class Board {
    char m[][] = {{'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'},
                  {'_','_','_','_','_','_','_'}};
    char player(Board s) {
        int xTiles=0, oTiles=0;
        for(int i=0; i<7; i++)
            for(int j=0; j<6; j++)
                if(s.m[i][j] == 'X') xTiles++;
                else if(s.m[i][j] == 'O') oTiles++;
        if(xTiles > oTiles) return 'O';
        return 'X';
    }
    ArrayList<Integer> actions(Board s) {
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int i=0; i<7; i++)
            if(s.m[0][i] == '_') actions.add(i);
        return actions;
    }
    Board result(Board s, int colAdd, char p) {
        Board aux = s;
        int lineAdd = getLine(s,colAdd);
    }
    int getLine(Board s, int colAdd) {
        int i=5;
        while(s.m[i][colAdd]!='_')
            i--;
        return i;
    }
    int terminalTest(Board s, int colAdd)
}
class ConnectFour {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
    }
}