import java.util.ArrayList;
import java.util.Scanner;

public class Econom {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String toParse = in.next();
        int length = toParse.length();
        ArrayList<String> vars = new ArrayList<>();
        parseMain(0, toParse, vars);
        System.out.println(vars.size());
    }

    private static returnResult parseMain(int i, String str, ArrayList<String> vars) {
        char x;
        String res = "";
        returnResult help;
        x = str.charAt(i);

        //System.out.println("19: skobka = " + x);

        if (x == '(') {
            res += x;
            i++;
            x = str.charAt(i);
            while (x != ')') {
                if (x == '(') {
                    help = parseMain(i, str, vars);
                    res += help.getString();
                    i = help.getPos();
                } else if ((x >= 'a') && (x <= 'z')) {
                    help = parseSimpleName(i, str);
                    res += help.getString();
                    i = help.getPos();
                } else if ((x == '#') || (x == '$') || (x == '@')) {
                    help = parseOpp(i, str, vars);
                    res += help.getString();
                    i = help.getPos();
                }
                i++;
                x = str.charAt(i);
            }
            res += ')';
        }
        returnResult r = new returnResult(i, res);
        return r;
    }

    private static returnResult parseSimpleName(int i, String str) {
        char x;
        String res = "";
        x = str.charAt(i);

        //System.out.println("55: name = " + x);

        res += x;
        returnResult r = new returnResult(i, res);
        return r;
    }

    private static returnResult parseOpp(int i, String str, ArrayList<String> vars) {
        char x;
        String res = "";
        returnResult help;
        x = str.charAt(i);

        //System.out.println("71: operation = " + x);

        if ((x == '#') || (x == '$') || (x == '@')) {
            res += x;
            i++;
            x = str.charAt(i);
            if (x == '(') {
                help = parseMain(i, str, vars);
                res += help.getString();
                i = help.getPos();
            } else if ((x >= 'a') && (x <= 'z')) {
                help = parseSimpleName(i, str);
                res += help.getString();
                i = help.getPos();
            }
            i++;
            x = str.charAt(i);
            if (x == '(') {
                help = parseMain(i, str, vars);
                res += help.getString();
                i = help.getPos();
            } else if ((x >= 'a') && (x <= 'z')) {
                help = parseSimpleName(i, str);
                res += help.getString();
                i = help.getPos();
            }
        }
        if (!vars.contains(res)) {
            vars.add(res);
        }

        //System.out.println("token = " + res);

        returnResult r = new returnResult(i, res);
        return r;
    }

    private static class returnResult {
        private String string;
        private int pos;

        public returnResult(int i, String str) {
            string = str;
            pos = i;
        }

        public int getPos() {
            return pos;
        }

        public String getString() {
            return string;
        }
    }
}
