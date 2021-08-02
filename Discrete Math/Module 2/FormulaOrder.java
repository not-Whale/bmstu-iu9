import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class FormulaOrder {
    private static ArrayList<Node> nodes;
    private static ArrayList<Token> tokens;
    private static ArrayList<String> leftVars;
    private static ArrayList<String> rightVars;
    private static ArrayList<String> vars;
    private static ArrayList<String> currentF;
    private static ArrayList<Integer> countArr;
    private static ArrayList<Form> forms;

    private static int k;
    private static int r;
    private static int count;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String expr;

        nodes = new ArrayList<>();
        tokens = new ArrayList<>();
        leftVars = new ArrayList<>();
        rightVars = new ArrayList<>();
        vars = new ArrayList<>();
        currentF = new ArrayList<>();
        countArr = new ArrayList<>();
        forms = new ArrayList<>();

        while (in.hasNextLine()) {
            expr = in.nextLine();
            if (expr.isEmpty()) {
                break;
            }
            forms.add(new Form(expr));
            lex(expr);
            tokens.add(new Token(".", "END"));
        }

        r = tokens.size();
        k = 0;

        parse();

        int b = forms.size();
        for (int i = 0; i < b; i++) {
            forms.get(i).findVars();
        }

        Stack s = DFS();

        while (!s.isEmpty()) {
            Node y = s.Pop();
            String x = y.name;
            for (int i = 0; i < forms.size(); i++) {
                if (forms.get(i).list.contains(x)) {
                    System.out.println(forms.get(i).formula);
                    forms.remove(i);
                    break;
                }
            }
        }
    }

    private static void lex(String expr) {
        try {
            lexer(expr);
        } catch (Error e) {
            System.out.println(e.getMessage());
            exit(0);
        }
    }

    private static void lexer(String expr) {
        ArrayList<Character> nums = new ArrayList<>();
        nums.add('1'); nums.add('2'); nums.add('3');
        nums.add('4'); nums.add('5'); nums.add('6');
        nums.add('7'); nums.add('8'); nums.add('9');
        nums.add('0');
        int n = expr.length();
        int i = 0;
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
                while ((i < n) && (((x >= 'a') && (x <= 'z')) || ((x >= 'A') && (x <= 'Z')) || (nums.contains(x)))) {
                    buf += x;
                    i++;
                    if (i < n) {
                        x = expr.charAt(i);
                    }
                }
                tokens.add(new Token(buf, "IDENT"));
                if (!vars.contains(buf)) {
                    vars.add(buf);
                    nodes.add(new Node(buf));
                }
            } else if ((x == '+') || (x == '-')) {
                tokens.add(new Token(x + "", "ADD"));
                i++;
            } else if ((x == '*') || (x == '/')) {
                tokens.add(new Token(x + "", "MULL"));
                i++;
            } else if (x == ',') {
                tokens.add(new Token(x + "", "COMMA"));
                i++;
            } else if (x == '(') {
                tokens.add(new Token(x + "", "L_BRACKET"));
                i++;
            } else if (x == ')') {
                tokens.add(new Token(x + "", "R_BRACKET"));
                i++;
            } else if ((x == ' ') || (x == '\t') || (x == '\r') || (x == '\n')) {
                i++;
            } else if (x == '=') {
                tokens.add(new Token(x + "", "EQUAL"));
                i++;
            } else {
                throw new Error("syntax error");
            }
        }
    }

    private static void parse() {
        try {
            program();
            checkArgs();
        } catch (Error e) {
            System.out.println(e.getMessage());
            exit(0);
        }
    }

    private static void checkArgs() {
        int n = rightVars.size();
        int m = leftVars.size();
        for (int i = 0; i < n; i++) {
            if (!leftVars.contains(rightVars.get(i))){
                throw new Error("syntax error");
            }
        }
    }

    private static void program() {
        formula();
        program2();
    }

    private static void program2() {
        if (k < r) {
            formula();
            program2();
        }
    }

    private static void formula() {
        currentF = new ArrayList<>();
        countArr = new ArrayList<>();
        body();
        if ((k < r) && (tokens.get(k).mark.equals("EQUAL"))) {
            k++;
            count = 0;
            implement();
            if (count != countArr.size()) {
                throw new Error("syntax error");
            }
            if ((k >= r) || (!tokens.get(k).mark.equals("END"))) {
                throw new Error("syntax error");
            }
            k++;
        } else {
            throw new Error("syntax error");
        }
    }

    private static void body() {
        String x = ident();
        currentF.add(x);
        leftVars.add(x);
        countArr.add(vars.indexOf(x));
        body2();
    }

    private static void body2() {
        if ((k < r) && (!tokens.get(k).mark.equals("EQUAL"))) {
            if (tokens.get(k).mark.equals("COMMA")) {
                k++;
                body();
            } else {
                throw new Error("syntax error");
            }
        }
    }

    private static void implement() {
        expr();
        count++;
        implement2();
    }

    private static void implement2() {
        if ((k < r) && (!tokens.get(k).mark.equals("END"))) {
            if (tokens.get(k).mark.equals("COMMA")) {
                k++;
                implement();
            } else {
                throw new Error("syntax error");
            }
        }
    }

    private static void expr() {
        term();
        add();
    }

    private static void add() {
        if ((k < r)
                && (!tokens.get(k).mark.equals("END"))
                && (!tokens.get(k).mark.equals("COMMA"))
                && (!tokens.get(k).mark.equals("R_BRACKET"))) {
            Token x = tokens.get(k);
            if (x.mark.equals("ADD")) {
                k++;
                term();
                add();
            } else {
                throw new Error("syntax error");
            }
        }
    }

    private static void term() {
        factor();
        mull();
    }

    private static void mull() {
        if ((k < r)
                && (!tokens.get(k).mark.equals("END"))
                && (!tokens.get(k).mark.equals("COMMA"))
                && (!tokens.get(k).mark.equals("ADD"))
                && (!tokens.get(k).mark.equals("R_BRACKET"))) {
            Token x = tokens.get(k);
            if (x.mark.equals("MULL")) {
                k++;
                factor();
                mull();
            } else {
                throw new Error("syntax error");
            }
        }
    }

    private static void factor() {
        if (k < r) {
            Token x = tokens.get(k);
            if (x.mark.equals("NUMBER")) {
                k++;
            } else if (x.mark.equals("IDENT")) {
                if (!rightVars.contains(x.name)) {
                    rightVars.add(x.name);
                }
                for (int i = 0; i < countArr.size(); i++) {
                    Insert(vars.indexOf(x.name), countArr.get(i));
                }
                k++;
            } else if (x.mark.equals("L_BRACKET")) {
                k++;
                expr();
                if ((k < r) && (tokens.get(k).mark.equals("R_BRACKET"))) {
                    k++;
                } else {
                    throw new Error("syntax error");
                }
            } else if (x.name.equals("-")) {
                k++;
                factor();
            } else {
                throw new Error("syntax error");
            }
        } else {
            throw new Error("syntax error");
        }
    }

    private static String ident() {
        if ((k < r) && (tokens.get(k).mark.equals("IDENT"))) {
            Token x = tokens.get(k);
            k++;
            if (leftVars.contains(x.name)) {
                throw new Error("syntax error");
            } else {
                return x.name;
            }
        } else {
            throw new Error("syntax error");
        }
    }

    private static void Insert(int i, int j) {
        Edge y = nodes.get(i).edgesOut;
        nodes.get(i).edgesOut = new Edge(nodes.get(i), nodes.get(j));
        nodes.get(i).edgesOut.next = y;
    }

    private static Stack DFS() {
        try {
            return DFSstart();
        } catch (Error e) {
            System.out.println(e.getMessage());
        }
        return new Stack(0);
    }

    private static Stack DFSstart() {
        int n = nodes.size();
        for (int i = 0; i < n; i++) {
            nodes.get(i).mark = "white";
        }
        Stack s = new Stack(n);
        for (int i = 0; i < n; i++) {
            if (nodes.get(i).mark.equals("white")) {
                VisitVertex(nodes.get(i), s);
            }
        }
        return s;
    }

    private static void VisitVertex(Node v, Stack s) {
        if (v.mark.equals("gray")) {
            throw new Error("cycle");
        } else if (v.mark.equals("white")) {
            v.mark = "gray";
            Edge x = v.edgesOut;
            while (x != null) {
                VisitVertex(x.target, s);
                x = x.next;
            }
            v.mark = "black";
            s.Push(v);
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
                throw new Error("314: error: stack overflow");
            }
            data[top] = x;
            top++;
        }

        public Node Pop() {
            if (isEmpty()) {
                throw new Error("322: error: stack is empty");
            }
            top--;
            return data[top];
        }
    }

    private static class Form {
        private String formula;
        private ArrayList<String> list;

        public Form(String formula) {
            this.formula = formula;
            list = new ArrayList<>();
        }

        public void findVars() {
            char x = formula.charAt(0);
            int i = 0;
            while (x != '=') {
                x = formula.charAt(i);
                String buf = "";
                while ((x != ',') && (x != '=')) {
                    if (x != ' ') {
                        buf += x;
                    }
                    i++;
                    x = formula.charAt(i);
                }
                list.add(buf);
                i++;
            }
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
        private String mark;
        private Edge edgesOut;

        public Node(String name) {
            this.name = name;
            mark = "white";
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