import java.util.*;
import java.lang.*;
import java.io.*;
public class Decision {
    // Verifica se os exemplos possíveis do atributo target têm todos a mesma classificação
    public static boolean sameClassification(LinkedList<Exemplos> exemplos, Atributos atributoTarget) {
        String s = atributoTarget.target.get(exemplos.get(0).index);
        for(Exemplos e : exemplos)
            if(!atributoTarget.target.get(e.index).equals(s)) return false;
        return true;
    }
    // Verifica qual é, num conjunto de exemplos, o valor mais frequente
    public static No valorMaisComum(LinkedList<Exemplos> exemplos, Atributos atributoTarget) {
        String s="";
        int max=0;
        for(String t : atributoTarget.tipos) {
            int ctr = 0;
            for(Exemplos e: exemplos)
                if(atributoTarget.target.get(e.index).equals(t)) ctr++;
            if(ctr > max) {
                max = ctr;
                s = t; } }
        return new No(s, exemplos.size());
    }
    // Função de cálcula o valor de importância para um dado atributo
    public static double importance(Atributos a, LinkedList<Exemplos> exemplos, Atributos atributoTarget) {
        double e = 0;
        for(String s : a.tipos) {
            double[] counter = new double[atributoTarget.tipos.size()];
            int space = 0;
            for(String d: atributoTarget.tipos) {
                int ctr = 0;
                for(int i=0; i<exemplos.size(); i++)
                    if(exemplos.get(i).exsList.get(a.index).equals(s) && atributoTarget.target.get(exemplos.get(i).index).equals(d)) ctr++;
                counter[space] = ctr;
                space++; }
            double sum = 0.0;
            for(double d: counter)
                sum += d;
            e += (sum/12) * formulaInside(counter, sum); }
        return e;
    }
    public static double formulaInside(double[] counter, double sum) {
        double e = 0.0;
        for(double d : counter) {
            if(d == 0.0) e+=0.0;
            else e += -((d/(sum))*(Math.log(d/sum)/Math.log(2)));}
        return e;
    }
    // Função que verifica numa lista de exemplos quais deles têm o mesmo valor
    public static LinkedList<Exemplos> sameValue(LinkedList<Exemplos> exemplos, String v, int index) {
        LinkedList<Exemplos> exs = new LinkedList<Exemplos>();
        for(Exemplos e: exemplos)
            if(e.exsList.get(index).equals(v)) exs.addLast(e);
        return exs;
    }

    public static LinkedList<Atributos> copy(LinkedList<Atributos> atributos, String s) {
        LinkedList<Atributos> auxA = new LinkedList<Atributos>();
        for(int i=0; i<atributos.size(); i++)
            if(!atributos.get(i).nome.equals(s))
                auxA.addLast(atributos.get(i));
        return auxA;
    }

    // Algoritmo para indução de árvore de decisão
    public static No id3(LinkedList<Exemplos> exemplos, Atributos atributoTarget, LinkedList<Atributos> atributos, LinkedList<Exemplos> parent) {
        if(exemplos.isEmpty()) return valorMaisComum(parent, atributoTarget);
        else if(sameClassification(exemplos, atributoTarget)) return new No(atributoTarget.target.get(exemplos.get(0).index), exemplos.size());
        else if(atributos.isEmpty()) return valorMaisComum(exemplos, atributoTarget);
        else {
            Atributos A = null;
            double i = 9999;
            for(Atributos a: atributos){
                double iAux = importance(a , exemplos, atributoTarget);
                if(iAux < i) {
                    i = iAux;
                    A = a; } }
            No root = new No(A);
            for(int z=0; z<A.tipos.size(); z++) {
                String v = A.tipos.get(z);
                LinkedList<Exemplos> exs = sameValue(exemplos, v, A.index);
                LinkedList<Atributos> auxA = copy(atributos, A.nome);
                No newTree = id3(exs, atributoTarget, auxA, exemplos);
                root.addBranch(newTree, z);
            }
            return root;
        } 
    }
    public static void dfs(No root, String space) {
        if(root.filhos == null) return;
        System.out.println(space + "<" + root.label + ">");
        space += "\t"; 
        for(int i=0; i<root.filhos.length; i++) {

            if (root.filhos[i].filhos!=null)
                System.out.println(space + root.nFilhos[i] + ": ");
            else
                System.out.println(space + root.nFilhos[i] + ": " + root.filhos[i].label + " (" + root.filhos[i].counter + ")");
            dfs(root.filhos[i], space + "\t"); 
        }
    }
    public static void run(String ficheiro) throws FileNotFoundException{
        Scanner reader = new Scanner(new File(ficheiro));
        String nomeAtributos = reader.next().replaceAll("," , " ");

        int csvSize = 0;
        Scanner readIn = new Scanner(nomeAtributos);
        while(readIn.hasNext()) { readIn.next(); csvSize ++; }
        readIn = new Scanner(nomeAtributos);

        LinkedList<Atributos> atributos = new LinkedList<Atributos>();
        
        readIn.next();
        for(int i=0; i<csvSize-2; i++) {
            Atributos newAtributo = new Atributos(readIn.next());
            newAtributo.index = i;
            atributos.add(newAtributo); }

        Atributos atributoTarget = new Atributos(readIn.next());

        LinkedList<Exemplos> exemplos = new LinkedList<Exemplos>();
        int ctr = 0;
        while(reader.hasNext()) {
            String exs = reader.next().replaceAll(",", " ");
            readIn = new Scanner(exs);
            readIn.next();
            Exemplos aux = new Exemplos();
            aux.index = ctr;
            for(int i=0; i<csvSize-2; i++) {
                String s = readIn.next();
                aux.exsList.add(s);
                atributos.get(i).addDiff(s); }
            exemplos.add(aux);
            String s = readIn.next();
            atributoTarget.target.add(s);
            atributoTarget.addDiff(s);
            ctr++;
        }

        No aux = id3(exemplos, atributoTarget, atributos, new LinkedList<Exemplos>());

        dfs(aux, "");
    }
    public static void main(String[] args) throws FileNotFoundException{
        System.out.println("<<<<<Menu>>>>>");
        System.out.println("");
        System.out.println("-Pressione 1 para carregar ficheiro csv");
        System.out.println("-Pressione 2 para sair");
        System.out.println("");
        System.out.print("Escolha: ");
        Scanner stdin = new Scanner(System.in);
        int escolha = stdin.nextInt();
        switch (escolha) {
            case 1:
                Scanner readFile = new Scanner(System.in);
                String nameFile = readFile.nextLine(); 
                run(nameFile);
                break;
            case 2:
                break;
        }
        stdin.close();
    }
}