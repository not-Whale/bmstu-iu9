import java.util.ArrayList;
import java.util.Scanner;

public class Calc {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String expr = in.nextLine() + " ";

        Pair h = Lexer(expr, in);
        ArrayList<Token> tokens = h.getTokens();
        ArrayList<String> vars = h.getVars();

        System.out.print(parse(tokens, vars));
    }

    private static Pair Lexer(String expr, Scanner in) {
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<String> vars = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Character> nums = new ArrayList<>();
        nums.add('0'); nums.add('1'); nums.add('2');
        nums.add('3'); nums.add('4'); nums.add('5');
        nums.add('6'); nums.add('7'); nums.add('8');
        nums.add('9');
        int i = 0;
        char x;
        int n = expr.length();
        while (i < n) {
            x = expr.charAt(i);
            if (((x >= 'a') && (x <= 'z')) || ((x >= 'A') && (x <= 'Z'))) {
                i = tokenizeVar(expr, i, tokens, vars, values, in);
            } else if ((x == '+') || (x == '-') || (x == '*') || (x == '/')) {
                tokens.add(new Token("" + x, "OPERATION"));
                i++;
            } else if ((x == '(') || (x == ')')) {
                tokens.add(new Token("" + x, "BRACKET"));
                i++;
            } else if (nums.contains(x)) {
                i = tokenizeNumber(expr, i, tokens, nums);
            } else if ((x == ' ') || (x == '\n') || (x == '\t')) {
                i++;
            }
        }
        Pair result = new Pair(tokens, vars);
        return result;
    }

    private static int tokenizeVar(String expr, int i, ArrayList<Token> tokens, ArrayList<String> vars, ArrayList<Integer> values, Scanner in) {
        char x = expr.charAt(i);
        String buf = "";
        while ((x != '*') && (x != '/') && (x != '+') && (x != '-')
                && (x != ' ') && (x != '\n') && (x != '\t')
                && (x != '(') && (x != ')')) {
            buf += x;
            i++;
            x = expr.charAt(i);
        }
        if (!vars.contains(buf)) {
            vars.add(buf);
            int v = in.nextInt();
            values.add(v);
            tokens.add(new Token("" + v, "VARIABLE"));
        } else {
            tokens.add(new Token("" + values.get(vars.indexOf(buf)), "VARIABLE"));
        }
        return i;
    }

    private static int tokenizeNumber(String expr, int i, ArrayList<Token> tokens, ArrayList<Character> nums) {
        char x = expr.charAt(i);
        String buf = "";
        while (nums.contains(x)) {
            buf += x;
            i++;
            x = expr.charAt(i);
        }
        tokens.add(new Token(buf, "NUMBER"));
        return i;
    }

    private static void findValues(String s, ArrayList<Integer> values) {
        char x;
        int n = s.length();
        int i = 0;
        String buf = "";
        while (i < n) {
            x = s.charAt(i);
            if (x == ' ') {
                values.add(Integer.parseInt(buf));
                buf = "";
            } else {
                buf += x;
            }
            i++;
        }
        if (!buf.equals("")) {
            values.add(Integer.parseInt(buf));
        }
    }

    static int pos = 0;
    private static int parse(ArrayList<Token> tokens, ArrayList<String> vars) {
        int res = parseE(tokens, vars, 0);
        if (pos != tokens.size()) {
            System.out.println("error");
            System.exit(0);
        }
        return res;
    }

    private static int parseE(ArrayList<Token> tokens, ArrayList<String> vars, int res) {
        res = parseT(tokens, vars, res);
        res = parseE2(tokens, vars, res);
        return res;
    }

    private static int parseE2(ArrayList<Token> tokens, ArrayList<String> vars, int res) {
        if (pos < tokens.size()) {
            String x = tokens.get(pos).token();
            if (x.equals("+")) {
                pos++;
                res += parseT(tokens, vars, res);
                res = parseE2(tokens, vars, res);
            } else if (x.equals("-")) {
                pos++;
                res -= parseT(tokens, vars, res);
                res = parseE2(tokens, vars, res);
            }
        }
        return res;
    }

    private static int parseT(ArrayList<Token> tokens, ArrayList<String> vars, int res) {
        res = parseF(tokens, vars, res);
        res = parseT2(tokens, vars, res);
        return res;
    }

    private static int parseT2(ArrayList<Token> tokens, ArrayList<String> vars, int res) {
        if (pos < tokens.size()) {
            String x = tokens.get(pos).token();
            if (x.equals("*")) {
                pos++;
                res *= parseF(tokens, vars, res);
                res = parseT2(tokens, vars, res);
            } else if (x.equals("/")) {
                pos++;
                res /= parseF(tokens, vars, res);
                res = parseT2(tokens, vars, res);
            }
        }
        return res;
    }

    private static int parseF(ArrayList<Token> tokens, ArrayList<String> vars, int res) {
        if (pos < tokens.size()) {
            Token x = tokens.get(pos);
            if (x.tag.equals("NUMBER")) {
                pos++;
                res = Integer.parseInt(x.token);
            } else if (x.tag.equals("VARIABLE")) {
                pos++;
                res = Integer.parseInt(x.token);
            } else if (x.token.equals("(")) {
                pos++;
                res = parseE(tokens, vars, res);
                if (pos < tokens.size()) {
                    x = tokens.get(pos);
                } else {
                    System.out.println("error");
                    System.exit(0);
                }
                if (x.token.equals(")")) {
                    pos++;
                } else {
                    System.out.println("error");
                    System.exit(0);
                }
            } else if (x.token.equals("-")) {
                pos++;
                res = -parseF(tokens, vars, res);
            } else {
                System.out.println("error");
                System.exit(0);
            }
        } else {
            System.out.println("error");
            System.exit(0);
        }
        return res;
    }

    private static class Pair {
        private ArrayList<Token> tokens;
        private ArrayList<String> vars;

        public Pair(ArrayList<Token> t, ArrayList<String> v) {
            this.tokens = t;
            this.vars = v;
        }

        public ArrayList<Token> getTokens() {
            return tokens;
        }

        public ArrayList<String> getVars() {
            return vars;
        }
    }

    private static class Token {
        private String token;
        private String tag;

        public Token(String token, String tag) {
            this.token = token;
            this.tag = tag;
        }

        public String token() {
            return token;
        }

        public String tag() {
            return tag;
        }
    }
}