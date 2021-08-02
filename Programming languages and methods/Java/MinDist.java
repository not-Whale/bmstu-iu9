import java.util.Scanner;
import static java.lang.Math.*;

public class MinDist {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        char x = in.next().charAt(0);
        char y = in.next().charAt(0);
        int curX = -1, curY = -1;
        int dist, m = -1;
        int p = 0;
        while (p < s.length()) {
            char z = s.charAt(p);
            if (z == x) {
                if (curY == -1) {
                    curX = p;
                } else {
                    if (curX == -1) {
                        dist = abs(p - curY);
                    } else {
                        dist = min(abs(p - curY), abs(curX - curY));
                    }
                    if ((dist < m) || (m == -1)) {
                        m = dist;
                    }
                    curX = p;
                }
            } else if (z == y) {
                if (curX == -1) {
                    curY = p;
                } else {
                    if (curY == -1) {
                        dist = abs(p - curX);
                    } else {
                        dist = min(abs(p - curX), abs(curY - curX));
                    }
                    if ((dist < m) || (m == -1)) {
                        m = dist;
                    }
                    curY = p;
                }
            }
            if (m == 0) {
                break;
            }
            p++;
        }
        System.out.println(m - 1);
    }
}