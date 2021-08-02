import java.util.*;

public class GraphBase {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }
        for (int i = 0; i < m; i++) {
            int a, b;
            a = in.nextInt();
            b = in.nextInt();
            Insert(nodes, a, b);
        }

        Tarjan(nodes);

        count--;
        int[][] komp = new int[count][2];
        for (int i = 0; i < count; i++) {
            komp[i][0] = n + 1;
            komp[i][1] = 0;
        }

        for (int i = 0; i < n; i++) {
            if (i < komp[nodes[i].comp - 1][0]) {
                komp[nodes[i].comp - 1][0] = i;
            }
        }

        if (count == 1) {
            System.out.println(komp[0][0]);
            return;
        }

        for (int i = 0; i < n; i++) {
            Edge x = nodes[i].edgesOut;
            while (x != null) {
                Node v = x.parent;
                Node u = x.target;
                if (v.comp != u.comp) {
                    komp[u.comp - 1][1]++;
                }
                x = x.next;
            }
        }

        ArrayList<Integer> r = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            if (komp[i][1] == 0) {
                r.add(komp[i][0]);
            }
        }

        Collections.sort(r);
        for (int i = 0; i < r.size(); i++) {
            System.out.println(r.get(i));
        }
    }

    private static int time = 1;
    private static int count = 1;

    private static void Tarjan(Node[] nodes) {
        int n = nodes.length;
        Stack s = new Stack(n);
        for (int i = 0; i < n; i++) {
            if (nodes[i].T1 == 0) {
                VisitVertex(nodes, nodes[i], s);
            }
        }
    }

    private static void VisitVertex(Node[] nodes, Node v, Stack s) {
        v.T1 = v.low = time;
        time++;
        s.Push(v);
        Edge x = v.edgesOut;
        while (x != null) {
            Node u = x.target;
            if (u.T1 == 0) {
                VisitVertex(nodes, u, s);
            }
            if ((u.comp == 0) && (v.low > u.low)) {
                v.low = u.low;
            }
            x = x.next;
        }
        if (v.T1 == v.low) {
            Node u;
            do {
                u = s.Pop();
                u.comp = count;
            } while (u != v);
            count++;
        }
    }

    private static void Insert(Node[] nodes, int a, int b) {
        Edge y = nodes[a].edgesOut;
        nodes[a].edgesOut = new Edge(nodes[a], nodes[b]);
        nodes[a].edgesOut.next = y;
    }

    private static class Stack {
        private Node[] data;
        private int cap;
        private int top;

        public Stack(int n) {
            data = new Node[n];
            cap = n;
            top = 0;
        }

        public boolean isEmpty() {
            return (top == 0);
        }

        public void Push(Node x) {
            if (top == cap) {
                System.out.println("Push is broken!");
            }
            data[top] = x;
            top++;
        }

        public Node Pop() {
            if (isEmpty()) {
                System.out.println("Pop is broken!");
            }
            top--;
            return data[top];
        }
    }

    private static class Node {
        private Edge edgesOut;
        private int T1;
        private int low;
        private int comp;
        private int n;

        public Node(int n) {
            this.n = n;
            T1 = 0;
            low = -1;
            comp = 0;
            edgesOut = null;
        }
    }

    private static class Edge {
        private Node parent;
        private Node target;
        private Edge next;

        public Edge(Node parent, Node target) {
            this.parent = parent;
            this.target = target;
            next = null;
        }
    }
}