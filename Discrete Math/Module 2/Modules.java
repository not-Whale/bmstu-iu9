import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

import static java.lang.System.exit;

public class Modules {
    private static ArrayList<Token> tokens;
    private static ArrayList<String> functions;
    private static ArrayList<Integer> arguments;
    private static ArrayList<Node> nodes;
    private static boolean[][] edges;

    private static int k;
    private static int r;
    private static int v;
    private static int time;
    private static int count;

    private static String currentF;

    public static void main(String[] args) {

        tokens = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        v = 0;
        while (in.hasNextLine()) {
            String x = in.nextLine();
            if (x.isEmpty()) {
                break;
            }
            try {
                lexer(x);
            } catch (Error e) {
                System.out.println("error");
                exit(0);
            }
            v++;
        }

        functions = new ArrayList<>();
        arguments = new ArrayList<>();
        nodes = new ArrayList<>();
        edges = new boolean[v][v];
        k = 0;
        time = 1;
        count = 1;
        r = tokens.size();

        parse();

        for (int i = 0; i < v; i++) {
            nodes.add(new Node(functions.get(i)));
        }
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < v; j++) {
                if (edges[i][j]) {
                    Insert(nodes, i, j);
                }
            }
        }

        Tarjan();
        System.out.println(count - 1);
    }

    private static void Tarjan() {
        int m = nodes.size();
        Stack s = new Stack(m);
        for (int i = 0; i < m; i++) {
            Node v = nodes.get(i);
            if (v.T1 == 0) {
                VisitVertex(v, s);
            }
        }
    }

    private static void VisitVertex(Node v, Stack s) {
        v.T1 = v.low = time;
        time++;
        s.Push(v);
        Edge x = v.edgesOut;
        while (x != null) {
            Node u = x.target;
            if (u.T1 == 0) {
                VisitVertex(u, s);
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
                throw new Error("122: error: stack overflow");
            }
            data[top] = x;
            top++;
        }

        public Node Pop() {
            if (isEmpty()) {
                throw new Error("130: error: stack is empty");
            }
            top--;
            return data[top];
        }
    }

    private static void Insert(ArrayList<Node> nodes, int i, int j) {
        Edge x = nodes.get(i).edgesOut;
        nodes.get(i).edgesOut = new Edge(nodes.get(i), nodes.get(j));
        nodes.get(i).edgesOut.next = x;
    }

    private static void lexer(String expr) {
        ArrayList<Character> nums = new ArrayList<>();
        nums.add('1'); nums.add('2'); nums.add('3');
        nums.add('4'); nums.add('5'); nums.add('6');
        nums.add('7'); nums.add('8'); nums.add('9');
        nums.add('0');
        int i = 0;
        int n = expr.length();
        while (i < n) {
            char x = expr.charAt(i);
            if (nums.contains(x)) {
                String buf = "";
                while ((i < n) && (nums.contains(x))) {
                    buf += x;
                    i++;
                    if (i < n) {
                        x = expr.charAt(i);
                    }
                }
                tokens.add(new Token(buf, "NUMBER"));
            } else if (((x >= 'a') && (x <= 'z')) || ((x >= 'A') && (x <= 'Z'))) {
                String buf = "";
                while ((i < n) && ((((x >= 'a') && (x <= 'z')) || ((x >= 'A') && (x <= 'Z'))) || (nums.contains(x)))) {
                    buf += x;
                    i++;
                    if (i < n) {
                        x = expr.charAt(i);
                    }
                }
                tokens.add(new Token(buf, "IDENT"));
            } else if (x == '=') {
                tokens.add(new Token(x + "", "COMP_OP"));
                i++;
            } else if (x == '<') {
                String buf =  x + "";
                i++;
                x = expr.charAt(i);
                if ((x == '>') || (x == '=')) {
                    buf += x;
                    i++;
                }
                tokens.add(new Token(buf, "COMP_OP"));
            } else if (x == '>') {
                String buf = x + "";
                i++;
                x = expr.charAt(i);
                if (x == '=') {
                    buf += x;
                    i++;
                }
                tokens.add(new Token(buf, "COMP_OP"));
            } else if ((x == '+') || (x == '-') || (x == '*') || (x == '/')) {
                tokens.add(new Token(x + "", "ARITH_OP"));
                i++;
            } else if (x == '?') {
                tokens.add(new Token(x + "", "TERN_OP"));
                i++;
            } else if (x == '(') {
                tokens.add(new Token(x + "", "L_BRACKET"));
                i++;
            } else if (x == ')') {
                tokens.add(new Token(x + "", "R_BRACKET"));
                i++;
            } else if (x == ',') {
                tokens.add(new Token(x + "", "COMMA"));
                i++;
            } else if (x == ';') {
                tokens.add(new Token(x + "", "SEMICOLON"));
                i++;
            } else if (x == ':') {
                String buf = x + "";
                i++;
                x = expr.charAt(i);
                if (x == '=') {
                    buf += x;
                    i++;
                    tokens.add(new Token(buf, "IMPlEMENTATION"));
                } else {
                    tokens.add(new Token(buf, "TERN_OP"));
                }
            } else if ((x == ' ') || (x == '\t') || (x == '\n') || (x == '\r')) {
                i++;
            } else {
                throw new Error("226: syntax error");
            }
        }
    }

    private static void parse() {
        try {
            program();
        } catch (Error e) {
            System.out.println("error");
            exit(0);
        }
    }

    private static void program() {
        function();
        program2();
    }

    private static void program2() {
        if (k < r) {
            function();
            program2();
        }
    }

    private static void checkArgs(String name) {
        int i = 0;
        int n = arguments.get(functions.indexOf(name));
        while (i < r) {
            Token x = tokens.get(i);
            if (x.name.equals(name)) {
                i += 2;
                k = i;
                int m = actual_args();
                if (m != n) {
                    throw new Error("263: formal args not match with actual args");
                }
            }
            i++;
        }
    }

    private static void function() {
        // <ident>
        String name = ident();
        currentF = name;

        if (!functions.contains(name)) {
            functions.add(name);
            arguments.add(-1);
        }

        if (functions.size() > v) {
            throw new Error("281: too much functions");
        }

        // '('
        String x = tokens.get(k).mark;
        if (!x.equals("L_BRACKET")) {
            throw new Error("287: expected L_BRACKET, but given " + x + " i = " + k);
        }
        k++;

        // <formal_args>
        ArrayList<String> args = formal_args();
        arguments.set(functions.indexOf(name), args.size());

        // соответсвие формальных и фактических при вызове
        int backUp = k;
        String backUp2 = currentF;
        currentF = "$";

        checkArgs(name);

        k = backUp;
        currentF = backUp2;

        // использование идентификаторов внутри функции
        ArrayList<String> check = new ArrayList<>();
        int l = k;

        Token y = tokens.get(l);
        while ((l < r) && (!y.mark.equals("SEMICOLON"))) {
            y = tokens.get(l);
            if ((y.mark.equals("IDENT")) && (l < r - 1) && (!tokens.get(l + 1).mark.equals("L_BRACKET")) && (!check.contains(y.name))) {
                check.add(y.name);
            }
            l++;
        }

        if (check.size() > args.size()) {
            throw new Error("314: formal args not match with actual args");
        } else {
            for (int i = 0; i < check.size(); i++) {
                if (!args.contains(check.get(i))) {
                    throw new Error("318: formal args not match with actual args");
                }
            }
        }

        // ')'
        if (k < r) {
            x = tokens.get(k).mark;
        } else {
            throw new Error("overflow");
        }
        if (!x.equals("R_BRACKET")) {
            throw new Error("330: expected R_BRACKET, but given " + x + " k = " + k);
        }
        k++;

        // ':='
        if (k < r) {
            x = tokens.get(k).mark;
        } else {
            throw new Error("overflow");
        }
        if (!x.equals("IMPlEMENTATION")) {
            throw new Error("341: expected IMPlEMENTATION, but given " + x);
        }
        k++;

        // <expr>
        expr();

        // ';'
        if (k < r) {
            x = tokens.get(k).mark;
        } else {
            throw new Error("overflow error");
        }
        if (!x.equals("SEMICOLON")) {
            throw new Error("355: expected SEMICOLON, but given " + x);
        }
        k++;
    }

    private static ArrayList<String> formal_args() {
        ArrayList<String> res = new ArrayList<>();
        Token x = tokens.get(k);
        if (!x.mark.equals("R_BRACKET")) {
            ident_list(res);
        }
        return res;
    }

    private static void ident_list(ArrayList<String> res) {
        res.add(ident());
        ident_list2(res);
    }

    private static void ident_list2(ArrayList<String> res) {
        Token x = tokens.get(k);
        if (x.mark.equals("COMMA")) {
            k++;
            ident_list(res);
        }
    }

    private static void expr() {
        comp_expr();
        expr2();
    }

    private static void expr2() {
        if (k < r) {
            Token x = tokens.get(k);
            // '?'
            if ((x.mark.equals("TERN_OP")) && (x.name.equals("?"))) {
                k++;
                comp_expr();
                if (k < r) {
                    x = tokens.get(k);
                } else {
                    throw new Error("expected TERN_OP : , but token list is empty");
                }
                if ((!x.mark.equals("TERN_OP")) || (!x.name.equals(":"))) {
                    throw new Error("400: expected TERN_OP : , but given" + x.name + "");
                }
                k++;
                expr();
            }
        }
    }

    private static void comp_expr() {
        arith_expr();
        comp_expr2();
    }

    private static void comp_expr2() {
        if ((k < r) && (comp_op())) {
            arith_expr();
        }
    }

    private static boolean comp_op() {
        String x = tokens.get(k).mark;
        if (!x.equals("COMP_OP")) {
            return false;
        } else {
            k++;
            return true;
        }
    }

    private static void arith_expr() {
        term();
        add();
    }

    private static void add() {
        if (k < r) {
            Token x = tokens.get(k);
            if ((x.name.equals("+")) || (x.name.equals("-"))) {
                k++;
                term();
                add();
            }
        }
    }

    private static void term() {
        factor();
        mull();
    }

    private static void mull() {
        if (k < r) {
            Token x = tokens.get(k);
            if ((x.name.equals("*")) || (x.name.equals("/"))) {
                k++;
                factor();
                mull();
            }
        }
    }

    private static void factor() {
        if (k < r) {
            Token x = tokens.get(k);
            if (x.mark.equals("NUMBER")) {
                k++;
            } else if (x.mark.equals("IDENT")) {
                if ((k < r - 1) && (tokens.get(k + 1).mark.equals("L_BRACKET"))) {
                    if (!functions.contains(x.name)) {
                        functions.add(x.name);
                        arguments.add(-1);
                    }
                    if (functions.size() > v) {
                        throw new Error("473: too much functions");
                    }
                    if ((!currentF.equals(x.name)) && (!currentF.equals("$"))) {
                        edges[functions.indexOf(currentF)][functions.indexOf(x.name)] = true;
                    }
                }
                k++;
                factor2();
            } else if (x.mark.equals("L_BRACKET")) {
                k++;
                expr();
                if (k < r) {
                    x = tokens.get(k);
                } else {
                    throw new Error("488: overflow");
                }
                if (!x.mark.equals("R_BRACKET")) {
                    throw new Error("491: expected R_BRACKET, but given " + x.mark + " " + x.name);
                }
                k++;
            } else if (x.name.equals("-")) {
                k++;
                factor();
            } else {
                throw new Error("498: undefined symbol");
            }
        } else {
            throw new Error("501: overflow");
        }
    }

    private static void factor2() {
        if (k < r) {
            Token x = tokens.get(k);
            if (x.mark.equals("L_BRACKET")) {
                k++;

                actual_args();

                if (k < r) {
                    x = tokens.get(k);
                } else {
                    throw new Error("516: overflow");
                }
                if (!x.mark.equals("R_BRACKET")) {
                    throw new Error("519: expected R_BRACKET, but given " + x.mark + " " + x.name);
                }
                k++;
            }
        }
    }

    private static int actual_args() {
        int count = 0;
        if ((k < r) && (!tokens.get(k).mark.equals("R_BRACKET"))) {
            count = expr_list(count);
        }
        return count;
    }

    private static int expr_list(int count) {
        expr();
        count = expr_list2(count + 1);
        return count;
    }

    private static int expr_list2(int count) {
        if ((k < r) && (tokens.get(k).mark.equals("COMMA"))) {
            k++;
            count = expr_list(count);
        }
        return count;
    }

    private static String ident() {
        Token x = tokens.get(k);
        if (!x.mark.equals("IDENT")) {
            throw new Error("551: expected IDENT, but given " + x.mark + " " + x.name);
        } else {
            k++;
            return x.name;
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

    private static class Node {
        private String name;
        private Edge edgesOut;
        private int T1;
        private int comp;
        private int low;

        public Node(String name) {
            this.name = name;
            edgesOut = null;
            T1 = 0;
            comp = 0;
            low = -1;
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