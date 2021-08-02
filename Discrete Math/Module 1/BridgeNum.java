import java.util.Scanner;

public class BridgeNum {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        Node[] nodes = new Node[n];
        Pair[] edges = new Pair[m];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
            nodes[i].edge.sign = 0;
        }

        for (int i = 0; i < m; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            Insert(nodes[a].edge, b);
            Insert(nodes[b].edge, a);
            edges[i] = new Pair(a, b);
        }

        Queue q = new Queue(m * 4);

        DFS1(nodes, q, n);

        DFS2(nodes, q);

        System.out.println(getBriges(nodes, edges, m));
    }

    private static int getBriges(Node[] nodes, Pair[] edges, int m) {
        int counter = 0;
        for (int i = 0; i < m; i++) {
            if (nodes[edges[i].a].component != nodes[edges[i].b].component) {
                counter++;
            }
        }
        return counter;
    }

    private static void DFS1(Node[] nodes, Queue q, int n) {
        for (int i = 0; i < n; i++) {
            if (nodes[i].color == 0) {
                nodes[i].parent = null;
                visitVertex1(nodes, q, nodes[i], n);
            }
        }
    }

    private static void visitVertex1(Node[] nodes, Queue q, Node v, int n) {
        v.color = 1;
        Enqueue(q, v);
        Edge c = v.edge;
        while (c != null) {
            if (nodes[c.num].color == 0) {
                nodes[c.num].parent = v;
                visitVertex1(nodes, q, nodes[c.num], n);
            }
            c = c.nextEdge;
        }
        v.color = 2;
    }

    private static void DFS2(Node[] nodes, Queue q) {
        int component = 0;
        while (!QueueEmpty(q)) {
            Node v = Dequeue(q);
            if (v.component == -1) {
                visitVertex2(nodes, v, component);
                component++;
            }
        }
    }

    private static void visitVertex2(Node[] nodes, Node v, int component) {
        v.component = component;
        Edge c = v.edge;
        while (c != null) {
            if ((nodes[c.num].component == -1) && (nodes[c.num].parent != v)) {
                visitVertex2(nodes, nodes[c.num], component);
            }
            c = c.nextEdge;
        }
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

    private static class Node {
        public Node(int a) {
            color = 0;
            component = -1;
            edge = new Edge(a);
        }
        public int color;
        public int component;
        public Edge edge;
        public Node parent;
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

    private static class Queue {
        public Queue(int a) {
            data = new Node[a];
            count = 0;
            head = 0;
            tail = 0;
            cap = a;
        }
        public Node[] data;
        public int count;
        public int head;
        public int tail;
        public int cap;
    }

    private static void Enqueue(Queue q, Node x) {
        if (q.count == q.cap) {
            Node[] buf = new Node[q.cap];
            for (int i = 0; i < q.cap; i++) {
                buf[i] = q.data[i];
            }
            int c = q.cap*2;
            q.data = new Node[c];
            for (int i = 0; i < q.cap; i++) {
                q.data[i] = buf[i];
            }
            q.cap = c;
        }
        q.data[q.tail] = x;
        q.tail = (q.tail + 1) % q.cap;
        q.count++;
    }

    private static Node Dequeue(Queue q) {
        Node x = q.data[q.head];
        q.head = (q.head + 1) % q.cap;
        q.count--;
        return x;
    }

    private static boolean QueueEmpty(Queue q) {
        return (q.count == 0);
    }
}