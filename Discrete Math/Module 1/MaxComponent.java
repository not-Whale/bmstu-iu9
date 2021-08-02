import java.util.Scanner;

public class MaxComponent {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        Node[] nodes = new Node[n];
        Component[] components = new Component[n];
        Pair[] pairs = new Pair[m];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
            nodes[i].edge.sign = 0;
        }

        for (int i = 0; i < m; i++) {
            int a, b;
            a = in.nextInt();
            b = in.nextInt();
            Insert(nodes[a].edge, b);
            Insert(nodes[b].edge, a);
            pairs[i] = new Pair(a, b);
        }

        int counter = DFS(nodes, components, n);

        for (int i = 0; i < n; i++) {
            Edge e = nodes[i].edge;
            while(e != null) {
                components[nodes[i].component].edges += e.sign;
                e = e.nextEdge;
            }
        }

        int num = 0;
        int max1 = -1;
        int max2 = -1;
        for (int i = 0; i < counter; i++) {
            if (components[i].nodes > max1) {
                max1 = components[i].nodes;
                max2 = components[i].edges;
                num = i;
            }
            if ((components[i].nodes == max1) && (components[i].edges > max2)) {
                max2 = components[i].edges;
                num = i;
            }
        }

        System.out.println("graph {");
        for (int i = 0; i < n; i++) {
            if (nodes[i].component == num) {
                System.out.println(i + " [color = red]");
            } else {
                System.out.println(i);
            }
        }

        for (int i = 0; i < m; i++) {
            if ((nodes[pairs[i].a].component == num) && (nodes[pairs[i].b].component == num)) {
                System.out.println(pairs[i].a + " -- " + pairs[i].b + " [color = red]");
            } else {
                System.out.println(pairs[i].a + " -- " + pairs[i].b);
            }
        }
        System.out.println("}");
    }

    private static int DFS(Node[] G, Component[] comps, int n) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (G[i].color == 0) {
                G[i].component = count;
                comps[count] = new Component();
                visitVertex(G, comps, i, count);
                count++;
            }
        }
        return count;
    }

    private static void visitVertex(Node[] G, Component[] comps, int v, int count) {
        G[v].color = 1;
        Edge c = G[v].edge;
        while (c != null) {
            if (G[c.num].color == 0) {
                comps[count].nodes++;
                G[c.num].component = count;
                visitVertex(G, comps, c.num, count);
            }
            c = c.nextEdge;
        }
        G[v].color = 2;
    }

    private static void Insert(Edge nodes, int b) {
        boolean add = false;
        Edge c = nodes;
        while (c != null) {
            if (c.num == b) {
                c.sign++;
                add = true;
                break;
            }
            c = c.nextEdge;
        }
        if (!add) {
            Edge e = new Edge(b);
            e.nextEdge = nodes.nextEdge;
            nodes.nextEdge = e;
        }
    }

    private static class Component {
        public Component() {
            edges = 0;
            nodes = 1;
        }
        public int edges;
        public int nodes;
    }

    private static class Node {
        public Node(int a) {
            color = 0;
            component = -1;
            edge = new Edge(a);
        }
        public int color;
        public int component;
        public Edge edge;
    }

    private static class Edge {
        public Edge(int num) {
            this.num = num;
            sign = 1;
            nextEdge = null;
        }
        public int num;
        public int sign;
        public Edge nextEdge;
    }

    private static class Pair {
        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
            sign = 1;
        }
        public int a;
        public int b;
        public int sign;
    }
}
