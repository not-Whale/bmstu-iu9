import java.util.*;

public class Loops {
    private static ArrayList<Node> res;
    private static HashMap<Integer, Node> nodes;

    private static int r;
    private static int time;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        nodes = new HashMap<>();
        res = new ArrayList<>();

        int n = in.nextInt();
        boolean next = false;
        int from = 0;

        for (int i = 0; i < n; i++) {
            int m = in.nextInt();
            String name = in.next();
            nodes.putIfAbsent(m, new Node(m));
            if (i == 0) {
                r = m;
            }
            if (next) {
                next = false;
                Edge z = new Edge(nodes.get(from), nodes.get(m));
                nodes.get(from).edgesOut.add(z);
                nodes.get(m).edgesIn.add(z);
            }
            if (name.equals("ACTION")) {
                next = true;
                from = m;
            } else if (name.equals("JUMP")) {
                int l = in.nextInt();
                nodes.putIfAbsent(l, new Node(l));

                Edge z = new Edge(nodes.get(m), nodes.get(l));
                nodes.get(l).edgesIn.add(z);
                nodes.get(m).edgesOut.add(z);
            } else if (name.equals("BRANCH")) {
                int l = in.nextInt();
                nodes.putIfAbsent(l, new Node(l));

                Edge z = new Edge(nodes.get(m), nodes.get(l));
                nodes.get(l).edgesIn.add(z);
                nodes.get(m).edgesOut.add(z);

                next = true;
                from = m;
            }
        }

        DFS();

        for (Node x : nodes.values()) {
            if (x.mark != 0) {
                res.add(x);
            }
        }

        Collections.sort(res);

        Dominators();

        int count = CycleSeek();
        System.out.print(count);
    }

    private static int CycleSeek() {
        int count = 0;
        for (int i = 0; i < res.size(); i++) {
            Node x = res.get(i);
            for (int j = 0; j < x.edgesIn.size(); j++) {
                Edge z = x.edgesIn.get(j);
                Node y = z.parent;
                if ((res.contains(y)) && (x != y) && (DomSeek(x, y))) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private static boolean DomSeek(Node x, Node y) {
        if (y == null) {
            return false;
        }
        if (y == x) {
            return true;
        } else {
            return (DomSeek(x, y.dom));
        }
    }

    private static void Dominators() {
        for (int i = 0; i < res.size() - 1; i++) {
            Node w = res.get(i);
            for (int j = 0; j < w.edgesIn.size(); j++) {
                Node v = w.edgesIn.get(j).parent;
                if (res.contains(v)) {
                    Node u = FindMin(v);
                    if (u.sdom.T1 < w.sdom.T1) {
                        w.sdom = u.sdom;
                    }
                }
            }
            w.ancestor = w.parent;
            w.sdom.bucket.add(w);
            for (int j = 0; j < w.parent.bucket.size(); j++) {
                Node v = w.parent.bucket.get(j);
                Node u = FindMin(v);
                if (u.sdom == v.sdom) {
                    v.dom = v.sdom;
                } else {
                    v.dom = u;
                }
            }
        }
        for (int i = res.size() - 2; i >= 0; i--) {
            Node w = res.get(i);
            if ((w.dom != null) && (w.dom != w.sdom)) {
                w.dom = w.dom.dom;
            }
        }
        res.get(res.size() - 1).dom = null;
    }

    private static Node FindMin(Node v) {
        Node min;
        if (v.ancestor == null) {
            return v;
        } else {
            Stack<Node> s = new Stack<>();
            Node u = v;
            while (u.ancestor.ancestor != null) {
                s.push(u);
                u = u.ancestor;
            }
            while (!s.isEmpty()) {
                v = s.pop();
                if (v.ancestor.label.sdom.T1 < v.label.sdom.T1) {
                    v.label = v.ancestor.label;
                }
                v.ancestor = u.ancestor;
            }
            min = v.label;
        }
        return min;
    }

    private static void DFS() {
        VisitVertex(nodes.get(r));
    }

    private static void VisitVertex(Node v) {
        v.mark = 1;
        v.T1 = time;
        time++;
        int n = v.edgesOut.size();
        for (int i = 0; i < n; i++) {
            Edge x = v.edgesOut.get(i);
            if (x.target.mark == 0) {
                x.target.parent = v;
                VisitVertex(x.target);
            }
        }
    }

    private static class Node implements Comparable<Node> {
        private int name;
        private int mark;
        private Node parent;
        private int T1;
        private Node sdom;
        private Node label;
        private Node ancestor;
        private Node dom;
        private ArrayList<Node> bucket;
        private ArrayList<Edge> edgesIn;
        private ArrayList<Edge> edgesOut;

        public Node(int name) {
            this.name = name;
            this.mark = 0;
            this.T1 = 0;
            parent = null;
            ancestor = null;
            label = this;
            dom = this;
            sdom = this;
            bucket = new ArrayList<>();
            edgesIn = new ArrayList<>();
            edgesOut = new ArrayList<>();
        }

        @Override
        public int compareTo(Node o) {
            return (o.T1 - this.T1);
        }
    }

    private static class Edge {
        private Node parent;
        private Node target;

        public Edge(Node parent, Node target) {
            this.parent = parent;
            this.target = target;
        }
    }
}