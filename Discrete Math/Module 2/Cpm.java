import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class Cpm {
    private static ArrayList<Token> tokens;
    private static HashMap<String, Node> nodes;
    private static ArrayList<Node> res;
    private static int[][] edges;
    private static Node del;
    private static String start;

    public static void main(String[] args) {
        tokens = new ArrayList<>();
        nodes = new HashMap<>();
        res = new ArrayList<>();
        del = null;

        edges = new int[2000][2000];

        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String expr = in.nextLine();
            if (expr.isEmpty()) {
                break;
            }
            lexer(expr);
        }
        tokens.add(new Token(";", "SYMBOL"));
        parse();

        DFS();
        //System.out.print("dfs out");
        for (Node x : res) {
            if (x.mark == 0) {
                Dijkstra(x);
            }
        }

        int max = 0;
        for (Node x : res) {
            if (x.dist > max) {
                max = x.dist;
            }
        }

        colorizeRed(max);
        printAnswer();
    }



    private static void colorizeRed(int max) {
        for (Node x : res) {
            x.mark = 0;
        }
        for (Node x : res) {
            if ((x.mark == 0) && (x.color != 3)) {
                int r = VisitVertexRed(x, 0, max);
                if (r == max) {
                    x.color = 4;
                }
            }
        }
    }

    private static int VisitVertexRed(Node x, int current, int max) {
        x.mark = 1;
        current += x.value;
        int res = current;
        int local = 0;
        for (Edge y : x.edgesOut) {
            if (y.target.color != 3) {
                res = VisitVertexRed(y.target, current, max);
            }
            if (res > local) {
                local = res;
            }
            if (res == max) {
                x.color = 4;
                y.color = 4;
            }
        }
        if (res > local) {
            local = res;
        }
        if (local == max) {
            x.color = 4;
        }
//        if (res == current) {
//            System.out.println("Node " + x.name + ", res = " + res);
//        }
        return local;
    }

    private static void Dijkstra(Node s) {
        PriorityQueue q = new PriorityQueue(res.size());
        //System.out.println("Dijkstra from " + s.name);
        for (Node v : res) {
            if (v.name.equals(s.name)) {
                v.dist = v.value;
            }
            q.Insert(v);
        }
        while (!q.isEmpty()) {
            Node v = q.ExtractMax();
            //System.out.println("Extract " + v.name);
            v.index = -1;
            for (Edge z : v.edgesOut) {
                if (z.target.color != 3) {
                    Node u = z.target;
                    //System.out.println("Edge to " + u.name);
                    //System.out.println("v = " + v.dist + ", u = " + u.dist + ", u.value = " + u.value);
                    if (Relax(v, u, u.value)) {
                        u.mark = -1;
                        if (u.index != -1) {
                            q.IncreaseKey(u, u.dist);
                        } else {
                            q.Insert(u);
                        }
                        //System.out.println("Dist of " + u.name + " increased to " + u.dist);
                    }
                }
            }
        }
        s.mark = -1;
    }

    private static boolean Relax(Node u, Node v, int w) {
        boolean changed = (u.dist + w > v.dist);
        if (changed) {
            v.dist = u.dist + w;
        }
        return changed;
    }

    private static void DFS() {
        DFSfind();
        while (del != null) {
            del.color = 3;
            DFSdel(del);
            del = null;
            DFSfind();
        }
        for (Node x : nodes.values()) {
            if (x.color != 3) {
                res.add(x);
            }
        }
    }

    private static void DFSdel(Node x) {
        x.color = 3;
        for (Edge y : x.edgesOut) {
            if (y.target.color != 3) {
                DFSdel(y.target);
            }
        }
    }

    private static void DFSfind() {
        for (Node x : nodes.values()) {
            if (x.color != 3) {
                x.color = 0;
            }
        }
        for (Node x : nodes.values()) {
            if (x.color == 0) {
                VisitVertex(x);
            }
        }
    }

    private static void VisitVertex(Node x) {
        x.color = 1;
        for (Edge y : x.edgesOut) {
            if (y.target.color == 0) {
                VisitVertex(y.target);
            } else if (y.target.color == 1) {
                del = y.target;
                return;
            }
        }
        x.color = 2;
    }

    private static void lexer(String expr) {
        int i = 0;
        int n = expr.length();
        while (i < n) {
            char x = expr.charAt(i);
            if ((x >= '0') && (x <= '9')) {
                String buf = "";
                while ((x >= '0') && (x <= '9')) {
                    buf += x;
                    i++;
                    if (i < n) {
                        x = expr.charAt(i);
                    } else {
                        x = ' ';
                    }
                }
                tokens.add(new Token(buf, "NUMBER"));
            } else if (((x >= 'a') && (x <= 'z')) || ((x >= 'A') && (x <= 'Z'))) {
                String buf = "";
                while (((x >= 'a') && (x <= 'z')) || ((x >= 'A') && (x <= 'Z')) || ((x >= '0') && (x <= '9'))) {
                    buf += x;
                    i++;
                    if (i < n) {
                        x = expr.charAt(i);
                    } else {
                        x = ' ';
                    }
                }
                tokens.add(new Token(buf, "IDENT"));
                nodes.putIfAbsent(buf, new Node(buf));
            } else if ((x == '<') || (x == '(') || (x == ')') || (x == ';')) {
                tokens.add(new Token(x + "", "SYMBOL"));
                i++;
            } else if ((x == ' ') || (x == '\t') || (x == '\r') || (x == '\n')) {
                i++;
            } else {
                throw new Error("lexer: syntax error");
            }
        }
    }

    private static void parse() {
        int i = 0;
        int n = tokens.size();
        Token from = tokens.get(0);
        while (i < n) {
            Token x = tokens.get(i);
            if (i == 0) {
                start = x.name;
            }
            while (!x.name.equals(";")) {
                x = tokens.get(i);
                if (x.mark.equals("IDENT")) {
                    from = x;
                    i++;
                } else if (x.name.equals("<")) {
                    insert(from.name, tokens.get(i + 1).name);
                    i++;
                } else if (x.name.equals("(")) {
                    nodes.get(tokens.get(i - 1).name).value = Integer.parseInt(tokens.get(i + 1).name);
                    i += 3;
                }
            }
            i++;
        }
    }

    private static void insert(String parent, String target) {
        Node x = nodes.get(parent);
        Node y = nodes.get(target);
        if (edges[x.number][y.number] == 0) {
            Edge z = new Edge(y);
            x.edgesOut.add(z);
            edges[x.number][y.number] = 1;
        }
    }

    private static class PriorityQueue {
        private Node[] heap;
        private int cap;
        private int count;

        public PriorityQueue(int n) {
            heap = new Node[n];
            cap = n;
            count = 0;
        }

        public boolean isEmpty() {
            return (count == 0);
        }

        public void Insert(Node x) {
            int i = count;
            if (i == cap) {
                System.out.println("Queue, insert: overflow");
            }
            count = i + 1;
            heap[i] = x;
            while ((i > 0) && (heap[(i - 1) / 2].dist < heap[i].dist)) {
                Node y = heap[(i - 1) / 2];
                heap[(i - 1) / 2] = heap[i];
                heap[i] = y;
                heap[i].index = i;
                i = (i - 1) / 2;
            }
            heap[i].index = i;
        }

        public Node ExtractMax() {
            if (count == 0) {
                System.out.println("Queue, extract max: queue is empty");
            }
            Node x = heap[0];
            count--;
            if (count > 0) {
                heap[0] = heap[count];
                heap[0].index = 0;
                Heapify(0, count, heap);
            }
            return x;
        }

        public void IncreaseKey(Node x, int key) {
            int i = x.index;
            x.dist = key;
            while ((i > 0) && (heap[(i - 1) / 2].dist < key)) {
                Node y = heap[(i - 1) / 2];
                heap[(i - 1) / 2] = heap[i];
                heap[i] = y;
                heap[i].index = i;
                i = (i - 1) / 2;
            }
            x.index = i;
        }
    }

    private static void Heapify(int i, int n, Node[] heap) {
        while (true) {
            int l = 2 * i + 1;
            int r = l + 1;
            int j = i;
            if ((l < n) && (heap[i].dist < heap[l].dist)) {
                i = l;
            }
            if ((r < n) && (heap[i].dist < heap[r].dist)) {
                i = r;
            }
            if (i == j) {
                break;
            }
            Node y = heap[i];
            heap[i] = heap[j];
            heap[j] = y;
            heap[i].index = i;
            heap[j].index = j;
        }
    }

    private static class Node {
        private static int count = 0;

        private String name;
        private int value;
        private int color;
        private int number;

        private int mark;
        private int dist;
        private int index;

        private ArrayList<Edge> edgesOut;

        public Node(String name) {
            this.name = name;
            number = count;
            count++;
            value = 0;
            color = 0;
            //mark = 0;
            dist = 0;
            index = -1;
            edgesOut = new ArrayList<>();
        }
    }

    private static class Edge {
        private Node target;
        private int color;

        public Edge(Node target) {
            this.target = target;
            color = 0;
        }
    }

    private static class Token {
        private String name;
        private String mark;

        public Token(String name, String mark) {
            this.name = name;
            this.mark = mark;
        }
    }

    private static void printAnswer() {
        System.out.println("digraph {");
        for (Node x : nodes.values()) {
            System.out.print(x.name + " [label = \"" + x.name + "(" + x.value + ")\"");
            if (x.color == 3) {
                System.out.print(", color = blue");
            } else if (x.color == 4) {
                System.out.print(", color = red");
            }
            System.out.print("]");
            System.out.println();
        }

        for (Node x : nodes.values()) {
            for (Edge y : x.edgesOut) {
                System.out.print(x.name + " -> " + y.target.name);
                if ((x.color == 3) && (y.target.color == 3)) {
                    System.out.print(" [color = blue]");
                } else if ((y.color == 4) && (x.color == 4) && (y.target.color == 4)) {
                    System.out.print(" [color = red]");
//                } else if ((x.color == 4) && (y.target.color == 4)) {
//                    System.out.print(" [color = orange]");
                }
                System.out.println();
            }
        }
        System.out.println("}");
    }
}