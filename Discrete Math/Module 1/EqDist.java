import javax.crypto.spec.DESedeKeySpec;
import java.util.Scanner;

public class EqDist {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        Node[] nodes = new Node[n];
        Pair[] pairs = new Pair[m];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < m; i++) {
            int a, b;
            a = in.nextInt();
            b = in.nextInt();
            Insert(nodes, a, b);
            pairs[i] = new Pair(a, b);
        }

        int k = in.nextInt();

        int[] primaryNodes = new int[k];

        for (int i = 0; i < n; i++) {
            nodes[i].distances = new int[k];
            for (int j = 0; j < k; j++) {
                nodes[i].distances[j] = -1;
            }
        }

        for (int i = 0; i < k; i++) {
            primaryNodes[i] = in.nextInt();
            nodes[primaryNodes[i]].distances[i] = 0;
        }

        Queue q = new Queue(16*m);

        for (int i = 0; i < k; i++) {
            BFS(nodes, q, primaryNodes[i], i);
            for (int j = 0; j < n; j++) {
                nodes[j].mark = false;
            }
        }

        int counter = 0;
        for (int i = 0; i < n; i++) {
            boolean find = true;
            for (int j = 1; j < k; j++) {
                if ((nodes[i].distances[j - 1] != nodes[i].distances[j]) || (nodes[i].distances[j] == -1)) {
                    find = false;
                    break;
                }
            }
            if (find) {
                System.out.print(i + " ");
                counter++;
            }
        }
        if (counter == 0) {
            System.out.println("-");
        }

    }

    private static void BFS(Node[] nodes, Queue q, int w, int num) {
        nodes[w].mark = true;
        Enqueue(q, nodes[w]);
        int count = 1;
        int iter = 1;
        int citer = 0;
        while (!QueueEmpty(q)) {
            for (int i = 0; i < iter; i++) {
                if (QueueEmpty(q)) {
                    break;
                } else {
                    Node v = Dequeue(q);
                    Edge u = v.edge;
                    while (u != null) {
                        if (!nodes[u.num].mark) {
                            nodes[u.num].mark = true;
                            nodes[u.num].distances[num] = count;
                            citer++;
                            Enqueue(q, nodes[u.num]);
                        }
                        u = u.nextEdge;
                    }
                }
            }
            iter = citer;
            citer = 0;
            count++;
        }
    }

    private static void Insert(Node[] nodes, int a, int b) {
        Edge e = new Edge(b);
        e.nextEdge = nodes[a].edge.nextEdge;
        nodes[a].edge.nextEdge = e;
        nodes[a].edges++;

        e = new Edge(a);
        e.nextEdge = nodes[b].edge.nextEdge;
        nodes[b].edge.nextEdge = e;
        nodes[b].edges++;
    }

    private static class Node {
        public Node(int a) {
            mark = false;
            edge = new Edge(a);
            edges = 0;
        }
        public boolean mark;
        public Edge edge;
        public int edges;
        public int[] distances;
    }

    private static class Edge {
        public Edge(int num) {
            this.num = num;
            nextEdge = null;
        }
        public int num;
        public Edge nextEdge;
    }

    private static class Pair {
        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public int a;
        public int b;
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
