import java.util.ArrayList;
import java.util.Scanner;

public class Prim {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }

        for (int i = 0; i < m; i++) {
            int a, b, v;
            a = in.nextInt();
            b = in.nextInt();
            v = in.nextInt();
            InsertEdge(nodes, a, b, v);
        }

        ArrayList<Edge> T = MST_Prim(nodes, n);

        int counter = 0;
        for (int i = 0; i < n - 1; i++) {
            counter += T.get(i).a;
        }

        System.out.println(counter);
    }

    private static void InsertEdge(Node[] nodes, int a, int b, int v) {
        Edge e = new Edge(nodes[a], nodes[b], v);
        Edge f = nodes[a].edges;
        nodes[a].edges = e;
        e.nextEdge = f;

        e = new Edge(nodes[b], nodes[a], v);
        f = nodes[b].edges;
        nodes[b].edges = e;
        e.nextEdge = f;
    }

    private static ArrayList<Edge> MST_Prim (Node[] nodes, int n) {
        ArrayList<Edge> T = new ArrayList<>(n);
        QueuePrior q = new QueuePrior((n * (n + 1) / 2));
        Node v = nodes[0];
        while (true) {
            v.index = -2;
            Edge c = v.edges;
            while (c != null) {
                if (c.u.index == -1) {
                    c.u.key = c.a;
                    c.u.value = v;
                    Insert(q, c.u);
                } else if ((c.u.index != -2) && (c.a < c.u.key)) {
                    c.u.value = v;
                    DecreaseKey(q, c.u, c.a);
                }
                c = c.nextEdge;
            }
            if (QueueEmpty(q)) {
                break;
            }
            v = ExtractMin(q);
            T.add(new Edge(v, v.value, v.key));
        }
        return T;
    }

    private static boolean QueueEmpty(QueuePrior q) {
        return (q.count == 0);
    }

    private static void Insert(QueuePrior q, Node ptr) {
        int i = q.count;
        q.count++;
        q.heap[i] = ptr;
        while ((i > 0) && (q.heap[(i - 1) / 2].key > q.heap[i].key)) {
            swap(q.heap, ((i - 1) / 2), i);
            q.heap[i].index = i;
            i = ((i - 1) / 2);
        }
        q.heap[i].index = i;
    }

    private static Node ExtractMin(QueuePrior q) {
        Node res = q.heap[0];
        q.count--;
        if (q.count > 0) {
            q.heap[0] = q.heap[q.count];
            q.heap[0].index = 0;
            Heapify(0, q.count, q.heap);
        }
        return res;
    }

    private static void Heapify(int i, int n, Node[] P) {
        while (true) {
            int l = 2 * i + 1;
            int r = l + 1;
            int j = i;
            if ((l < n) && (P[i].key > P[l].key)) {
                i = l;
            }
            if ((r < n) && (P[i].key > P[r].key)) {
                i = r;
            }
            if (i == j) {
                break;
            }
            swap(P, i, j);
            P[i].index = i;
            P[j].index = j;
        }
    }

    private static void DecreaseKey(QueuePrior q, Node v, int a) {
        int i = v.index;
        v.key = a;
        while ((i > 0) && (q.heap[(i - 1) / 2].key > a)) {
            swap(q.heap, ((i - 1) / 2), i);
            q.heap[i].index = i;
            i = ((i - 1) / 2);
        }
        v.index = i;
    }

    private static void swap(Node[] heap, int a, int b) {
        Node c = heap[a];
        heap[a] = heap[b];
        heap[b] = c;
    }

    private static class QueuePrior {
        public QueuePrior(int n) {
            heap = new Node[n];
            cap = n;
            count = 0;
        }
        public Node[] heap;
        public int cap;
        public int count;
    }

    private static class Node {
        public Node() {
            index = -1;
            edges = null;
            value = null;
        }
        public int index;
        public int key;
        public Node value;
        public Edge edges;
    }

    private static class Edge {
        public Edge(Node b, Node c, int d) {
            v = b;
            u = c;
            a = d;
            nextEdge = null;
        }
        public Node v;
        public Node u;
        public int a;
        public Edge nextEdge;
    }
}
