    /* Alpha Beta */
    public int alphabeta(Board s) {
        int max = -9999;
        int action = 0;
        long startTime = System.currentTimeMillis();
        for(Integer a: actions(s)) {
            int v = maxValueAB(result(s, a), -9999, 9999, 11);
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
    public int maxValueAB(Board s, int alpha, int beta, int depth) {
        if(terminalTest(s)) return 9999;
        if(depth == 0) return utility(s);
        if(actions(s).isEmpty()) return 0;
        int v = -9999;
        for(Integer a : actions(s))
            v = Math.max(v, minValueAB(result(s, a),alpha, beta, depth-1));
            if(v>=beta) return v;
            alpha = Math.max(alpha, v);
        return v;
    }
    public int minValueAB(Board s, int alpha, int beta, int depth) {
        if(terminalTest(s)) return -9999;
        if(depth == 0) return utility(s);
        if(actions(s).isEmpty()) return 0;
        int v = 9999;
        for(Integer a : actions(s))
            v = Math.min(v, maxValueAB(result(s, a),alpha, beta, depth-1));
            if(v<=alpha) return v;
            beta = Math.min(beta, v);
        return v;
    }
    /* Monte Carlo Tree Search */
    public int MCTS(Board s) {
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
                v = rollout(current);
            } else
                v = rollout(current);
            while(current != null) {
                current.visitCount++;
                current.winScore+=v;
                current = current.parent;
            }
        }
        return root.getBest(root);
    }
    /* Simula a jogada até chegar a um teste terminal */
    public int rollout(Node current) {
        Board s = current.state;
        while(!terminalTest(s) || actions(s).isEmpty()) {
            int action = random(actions(s));
            if(action == 7) return value(s);
            s = result(s, action);
        }
        return value(s);
    } 
    /* Escolhe um valor random das possiveis ações no tabuleiro */
    public int random(LinkedList<Integer> actions) {
        if(actions.isEmpty()) return 7;
        Random rand = new Random();
        return actions.get(rand.nextInt(actions.size()));
    }