import java.util.*;
import java.lang.*;
public class Atributos {
    String nome;
    LinkedList<String> tipos; // Guarda os tipos que cada variável tem
    LinkedList<String> target; // Aplicado só à variável classificação, guarda os valores de exemplo
    int index; // Guarda o index que o atributo tinha ao início, para correta remoção dos exemplos
    Atributos(String s) {
        nome = s;
        tipos = new LinkedList<String>();
        target = new LinkedList<String>();
    }

    // Adiciona a lista de tipos todos os tipos diferentes para cada atributo
    public void addDiff(String s) {
        for(int i=0; i<tipos.size(); i++)
            if(tipos.get(i).equals(s)) return ;
        tipos.add(s);
    }
}
