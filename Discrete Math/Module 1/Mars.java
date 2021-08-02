import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;

import static java.lang.System.exit;

public class Mars {
    private static ArrayList<Node> nodes;
    private static TreeMap<Integer, ArrayList<Node> > components;
    private static int totalVertices;
    private static int curComp = 0;
    private static ArrayList<Node> res;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt(in.nextLine());
        totalVertices = n;

        nodes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }
        for (int i = 0; i < n; i++) {
            String s = in.nextLine();
            int m = s.length();
            int j = 0;
            int k = 0;
            while (k < m) {
                char x = s.charAt(k);
                if (x == '+') {
                    Edge y = new Edge(nodes.get(i), nodes.get(j));
                    nodes.get(i).edges.add(y);
                    j++;
                } else if (x == '-') {
                    j++;
                }
                k++;
            }
        }

        for (Node x : nodes) {
            if (x.mark == 0) {
                DFS(x, 0);
                curComp++;
            }
        }

        components = new TreeMap<Integer, ArrayList<Node> >();
        for (Node x : nodes) {
            if (!components.containsKey(x.comp))
                components.put(x.comp, new ArrayList<Node>());
            components.get(x.comp).add(x);
        }
        for (ArrayList<Node> arr : components.values()) {
            Collections.sort(arr);
        }

        res = new ArrayList<Node>();
        permutationRec(new ArrayList<Boolean>());
        for (Node x : res) {
            System.out.printf("%d ", x.number + 1);
        }
        System.out.println();
    }

    private static void permutationRec(ArrayList<Boolean> perm) {
        if (perm.size() == curComp) {
            ArrayList<Node> tempRes = new ArrayList<>();
            for (int i = 0; i < curComp; i++) {
                if (!perm.get(i)) {
                    tempRes.addAll(components.get(i * 2));
                } else if (components.get(i * 2 + 1) != null) {
                    tempRes.addAll(components.get(i * 2 + 1));
                }
            }
            Collections.sort(tempRes);
            if (res.isEmpty()) {
                res = tempRes;
            } else {
                int curBalance = Math.abs(totalVertices - tempRes.size() * 2);
                int oldBalance = Math.abs(totalVertices - res.size() * 2);
                if (curBalance > oldBalance) {
                    return;
                }
                if (tempRes.size() > res.size()) {
                    return;
                } else if (tempRes.size() < res.size()) {
                    res = tempRes;
                    return;
                }
                for (int i = 0; i < res.size(); i++) {
                    if (tempRes.get(i).number < res.get(i).number) {
                        res = tempRes;
                        return;
                    }
                    else if (tempRes.get(i).number > res.get(i).number) {
                        return;
                    }
                }
            }
        } else {
            ArrayList<Boolean> perm1 = new ArrayList<>(perm);
            ArrayList<Boolean> perm2 = new ArrayList<>(perm);
            perm1.add(false);
            perm2.add(true);
            permutationRec(perm1);
            permutationRec(perm2);
        }
    }

    private static void DFS(Node node, int curSubComp) {
        node.mark = 1;
        node.comp = curComp * 2 + curSubComp;
        for (Edge x : node.edges) {
            if ((x.target.mark == 1) && (x.target.comp == node.comp)) {
                System.out.println("No solution");
                exit(0);
            } else if (x.target.mark == 0) {
                int newSubComp;
                if (curSubComp == 0) {
                    newSubComp = 1;
                } else {
                    newSubComp = 0;
                }
                DFS(x.target, newSubComp);
            }
        }
    }

    private static class Node implements Comparable<Node> {
        private int number;
        private int mark;
        private int comp;
        private ArrayList<Edge> edges;

        public Node(int i) {
            number = i;
            mark = 0;
            comp = -1;
            edges = new ArrayList<>();
        }

        @Override
        public int compareTo(Node o) {
            return (this.number - o.number);
        }
    }

    private static class Edge {
        private Node parent;
        private Node target;

        public Edge(Node a, Node b) {
            parent = a;
            target = b;
        }
    }
}