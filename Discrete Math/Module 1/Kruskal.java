import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static java.lang.Math.*;

public class Kruskal {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            nodes[i] = new Node(x, y);
        }

        int m = n * (n + 1) / 2;

        ArrayList<Edge> edges = new ArrayList<>(m);

        int c = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                edges.add(new Edge(nodes[i], nodes[j]));
                c++;
            }
        }

        double result = MST_Kruskal(nodes, edges, n, m);
        System.out.printf("%.2f", result);
    }

    private static double MST_Kruskal(Node[] nodes, ArrayList<Edge> edges, int n, int m) {
        Collections.sort(edges);
        return SpanningTree(nodes, edges, n, m);
    }

    private static double SpanningTree(Node[] nodes, ArrayList<Edge> edges, int n, int m) {
        int i = 0;
        int counter = 0;
        double len = 0;
        while ((i < m) && (counter < n - 1)) {
            Edge e = edges.get(i);
            if (Find(e.a) != Find(e.b)) {
                counter++;
                len += e.length;
                Union(e.a, e.b);
            }
            i++;
        }
        return len;
    }

    private static void Union (Node x, Node y) {
        Node rootx = Find(x);
        Node rooty = Find(y);
        if (rootx.depth < rooty.depth) {
            rootx.parent = rooty;
        } else {
            rooty.parent = rootx;
            if ((rootx.depth == rooty.depth) && (rootx != rooty)) {
                rootx.depth++;
            }
        }
    }

    private static Node Find(Node x) {
        Node root;
        if (x.parent == x) {
            root = x;
        } else {
            x.parent = Find(x.parent);
            root = x.parent;
        }
        return root;
    }

    private static class Node {
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            parent = this;
            depth = 0;
        }
        public int x;
        public int y;
        public int depth;
        public Node parent;
    }

    private static class Edge implements Comparable<Edge> {
        public Edge(Node a, Node b) {
            this.a = a;
            this.b = b;
            length = sqrt( (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y) );
        }
        public Node a;
        public Node b;
        public double length;
        public int compareTo(Edge o) {
            double res = (this.length - o.length);
            if (res < 0) {
                return -1;
            } else if (res == 0) {
                return 0;
            }
            return 1;
        }
    }
}