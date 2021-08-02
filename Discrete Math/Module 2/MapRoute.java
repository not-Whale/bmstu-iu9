import java.util.Scanner;

public class MapRoute {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        Node[] nodes = new Node[N*N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int x = in.nextInt();
                nodes[i * N + j] = new Node(x, i * N + j);
            }
        }

        nodes[0].isFirst();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Insert(nodes, i, j, N);
            }
        }

        Dijkstra(nodes);
        System.out.println(nodes[N * N - 1].dist);
    }

    private static void Dijkstra(Node[] nodes) {
        int n = nodes.length;
        PriorityQueue q = new PriorityQueue(n);
        for (int i = 0; i < n; i++) {
            q.Insert(nodes[i]);
        }
        while (!q.isEmpty()) {
            Node v = q.ExtractMin();
            v.index = -1;
            Edge x = v.edgesOut;
            while (x != null) {
                if ((x.target.index != -1) && (Relax(v, x.target, x.value))) {
                    q.DecreaseKey(x.target, x.target.dist);
                }
                x = x.next;
            }
        }
    }

    private static boolean Relax(Node u, Node v, int w) {
        if ((u.dist == Integer.MAX_VALUE) && (v.dist == Integer.MAX_VALUE)) {
            return false;
        } else {
            boolean changed;
            if (u.dist == Integer.MAX_VALUE) {
                changed = (u.dist < v.dist);
            } else {
                changed = (u.dist + w < v.dist);
            }
            if (changed) {
                v.dist = u.dist + w;
            }
            return changed;
        }
    }

    private static void Insert(Node[] nodes, int i, int j, int N) {
        if (j > 0) {
            Edge x = nodes[i * N + j].edgesOut;
            nodes[i * N + j].edgesOut = new Edge(nodes[i * N + j], nodes[i * N + j - 1]);
            nodes[i * N + j].edgesOut.next = x;
        }

        if (j < N - 1) {
            Edge x = nodes[i * N + j].edgesOut;
            nodes[i * N + j].edgesOut = new Edge(nodes[i * N + j], nodes[i * N + j + 1]);
            nodes[i * N + j].edgesOut.next = x;
        }

        if (i > 0) {
            Edge x = nodes[i * N + j].edgesOut;
            nodes[i * N + j].edgesOut = new Edge(nodes[i * N + j], nodes[(i - 1) * N + j]);
            nodes[i * N + j].edgesOut.next = x;
        }

        if (i < N - 1) {
            Edge x = nodes[i * N + j].edgesOut;
            nodes[i * N + j].edgesOut = new Edge(nodes[i * N + j], nodes[(i + 1) * N + j]);
            nodes[i * N + j].edgesOut.next = x;
        }
    }
    private static class Node {
        private int coordinate;
        private int index;
        private int pos;
        private int dist;
        private Edge edgesOut;

        public Node(int c, int p) {
            coordinate = c;
            pos = p;
            edgesOut = null;
            dist = Integer.MAX_VALUE;
            index = - 1;
        }

        public void isFirst() {
            dist = coordinate;
        }

        public int getValue() {
            return coordinate;
        }
    }

    private static class Edge {
        private Node parent;
        private Node target;
        private Edge next;
        private int value;

        public Edge(Node parent, Node target) {
            this.parent = parent;
            this.target = target;
            if (this.target != null) {
                value = target.getValue();
            } else {
                value = 0;
            }
            next = null;
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
            count = i + 1;
            heap[i] = x;
            while ((i > 0) && (heap[(i - 1) / 2].dist > heap[i].dist)) {
                Node y = heap[(i - 1) / 2];
                heap[(i - 1) / 2] = heap[i];
                heap[i] = y;
                heap[i].index = i;
                i = (i - 1) / 2;
            }
            heap[i].index = i;
        }

        public Node ExtractMin() {
            Node x = heap[0];
            count--;
            if (count > 0) {
                heap[0] = heap[count];
                heap[0].index = 0;
                Heapify(0, count, heap);
            }
            return x;
        }

        public void DecreaseKey(Node x, int key) {
            int i = x.index;
            x.dist = key;
            while ((i > 0) && (heap[(i - 1) / 2].dist > key)) {
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
            if ((l < n) && (heap[i].dist > heap[l].dist)) {
                i = l;
            }
            if ((r < n) && (heap[i].dist > heap[r].dist)) {
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
}