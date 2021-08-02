import java.util.Scanner;
import static java.lang.Math.*;

public class Gauss {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Q[][] matrix = new Q[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++) {
                matrix[i][j] = new Q(in.nextInt());
            }
        }

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i;
        }

        Q[][] buf = new Q[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++) {
                buf[i][j] = new Q(1, 1);
                buf[i][j].m = matrix[i][j].m;
                buf[i][j].n = matrix[i][j].n;
                buf[i][j].sign = matrix[i][j].sign;
            }
        }

        Q[][] b = new Q[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++) {
                b[i][j] = new Q(1, 1);
                b[i][j].m = matrix[i][j].m;
                b[i][j].n = matrix[i][j].n;
                b[i][j].sign = matrix[i][j].sign;
            }
        }

        if (!makeDiagonal1(matrix, nums)) {
            if (!makeDiagonal2(buf, nums)) {
                if (!makeDiagonal3(b, nums)){
                    System.out.println("No solution");
                    return;
                }
                buf = b;
                matrix = b;
            }
            matrix = buf;
        }

        makeSimple(matrix);

        calcForward(matrix, nums);
        calcReverse(matrix);

        for (int i = 0; i < n; i++) {
            long m1, n1;
            int k = findIndex(nums, i);
            m1 = matrix[k][k].m * matrix[k][n].n * matrix[k][k].sign * matrix[k][n].sign;
            n1 = matrix[k][k].n * matrix[k][n].m;
            Q r = new Q(n1, m1);
            r.simple();
            System.out.println(r.m * r.sign + "/" + r.n);
        }
    }

    private static int findIndex(int[] nums, int key) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] == key) {
                return i;
            }
        }
        return 0;
    }

    private static boolean makeDiagonal1(Q[][] m, int[] nums) {

        int n = m[0].length - 1;

        findDiagonal11(m);

        int c = 0;
        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                c++;
            }
        }
        if (c == 0) {
            return true;
        }

        findDiagonal2(m, nums);

        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean makeDiagonal2(Q[][] m, int[] nums) {

        int n = m[0].length - 1;

        findDiagonal12(m);

        int c = 0;
        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                c++;
            }
        }
        if (c == 0) {
            return true;
        }

        findDiagonal2(m, nums);

        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean makeDiagonal3(Q[][] m, int[] nums) {

        int n = m[0].length - 1;

        findDiagonal13(m);

        int c = 0;
        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                c++;
            }
        }
        if (c == 0) {
            return true;
        }

        findDiagonal2(m, nums);

        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                return false;
            }
        }
        return true;
    }

    private static void findDiagonal11(Q[][] m) {
        int n = m[0].length - 1;
        for (int i = n - 1; i > 0; i--) {
            if (m[i][i].m == 0) {
                int j;
                for (j = 0; j < n; j++) {
                    if (m[j][i].m != 0) {
                        swapS(m, i, j);
                        break;
                    }
                }
            }
        }
    }

    private static void findDiagonal12(Q[][] m) {
        int n = m[0].length - 1;
        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                int j;
                for (j = 0; j < n; j++) {
                    if (m[j][i].m != 0) {
                        swapS(m, i, j);
                        break;
                    }
                }
            }
        }
    }

    private static void findDiagonal13(Q[][] m) {
        int n = m[0].length - 1;
        for (int i = n - 1; i > 0; i--) {
            if (m[i][i].m == 0) {
                int j;
                for (j = i; j < n; j++) {
                    if (m[j][i].m != 0) {
                        swapS(m, i, j);
                        break;
                    }
                }
            }
        }
    }

    private static void findDiagonal2(Q[][] m, int[] nums) {
        int n = m[0].length - 1;
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (m[i][i].m == 0) {
                int j;
                for (j = i; j < n; j++) {
                    if (m[i][j].m != 0) {
                        swapC(m, i, j, nums);
                        break;
                    }
                }
            }
        }
    }

    private static void makeSimple(Q[][] m) {
        int n = m[0].length - 1;
        for (int i = 0; i < n; i++) {
            long c1 = m[i][i].m * m[i][i].sign;
            long c2 = m[i][i].n;
            for (int j = 0; j < n + 1; j++) {
                m[i][j].mull(c2, c1);
            }
        }
    }

    private static void calcForward(Q[][] m, int [] nums) {
        int n = m[0].length - 1;
        for (int k = 0; k < n - 1; k++) {
            for (int i = k + 1; i < n; i++) {
                if (m[i][k].m != 0) {
                    if (m[k][k].m == 0) {
                        makeDiagonal3(m, nums);
                    }
                    long c1 = m[k][k].n * m[i][k].m * m[k][k].sign * m[i][k].sign * (-1);
                    long c2 = m[i][k].n * m[k][k].m;
                    //System.out.println(m[k][k].m + "/" + m[k][k].n);
                    Q[] h = mullString(m[k], c1, c2, k);
                    addString(m[i], h);
                }
            }
        }
    }

    private static void calcReverse(Q[][] m) {
        int n = m[0].length - 1;
        for (int k = n - 1; k > 0; k--) {
            for (int i = k - 1; i >= 0; i--) {
                if (m[i][k].m != 0) {
                    m[i][k].simple();
                    long c1 = m[k][k].n * m[i][k].m * m[k][k].sign * m[i][k].sign * (-1);
                    long c2 = m[i][k].n * m[k][k].m;
                    Q[] h = mullString(m[k], c1, c2, k);
                    addString(m[i], h);
                }
            }
        }
    }

    private static Q[] mullString(Q[] m, long c1, long c2, int start) {
        int n = m.length;
        Q[] r = new Q[n];
        for (int i = 0; i < n; i++) {
            r[i] = new Q(m[i]);
        }
        for (int i = start; i < n; i++) {
            r[i].mull(c1, c2);
        }
        return r;
    }

    private static void addString(Q[] m, Q[] h) {
        int n = m.length;
        for (int i = 0; i < n; i++) {
            m[i].add(h[i]);
            m[i].simple();
        }
    }

    private static void swapC(Q[][] m, int a, int b, int[] nums) {
        int n = m[0].length - 1;
        Q[][] r = new Q[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++) {
                if (j == a) {
                    r[i][j] = m[i][b];
                } else if (j == b) {
                    r[i][j] = m[i][a];
                } else {
                    r[i][j] = m[i][j];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++) {
                m[i][j] = r[i][j];
            }
        }
        int c = nums[a];
        nums[a] = nums[b];
        nums[b] = c;
    }

    private static void swapS(Q[][] m, int i, int j) {
        Q[] r = m[i];
        m[i] = m[j];
        m[j] = r;
    }

    private static long gcd(long a, long b) {

        a = abs(a);
        b = abs(b);

        while ((a != 0) && (b != 0)) {
            if (a > b) {
                a = a % b;
            } else {
                b = b % a;
            }
        }
        return a + b;
    }

    private static long lcm(long a, long b) {
        a = abs(a);
        b = abs(b);
        if (a == b) {
            return a;
        }
        long c = gcd(a, b);
        return (a * b / c);
    }

    private static void matrixPrint(Q[][] q) {
        int n = q[0].length - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++) {
                System.out.print (q[i][j].m * q[i][j].sign + "/" + q[i][j].n + " ");
            }
            System.out.println();
        }
    }

    private static class Q {
        private long m;
        private long n;
        private long sign;

        public Q(long a) {
            m = abs(a);
            n = 1;
            if (a >= 0) {
                sign = 1;
            } else {
                sign = -1;
            }
            simple();
        }

        public Q(long a, long b) {
            m = abs(a);
            n = abs(b);
            if (a * b >= 0) {
                sign = 1;
            } else {
                sign = -1;
            }
            simple();
        }

        public Q(Q q) {
            m = q.m;
            n = q.n;
            sign = q.sign;
            simple();
        }

        public void mull(long a, long b) {
            m *= abs(a);
            n *= abs(b);
            long s;
            if (a * b == 0) {
                s = 1;
            } else {
                s = (a * b) / (abs(a * b));
            }
            sign *= s;
            simple();
        }

        public void add(Q q) {
            long n1 = lcm(n, q.n);
            m = m * (n1 / n);
            n = n1;
            long a = m * sign + q.m * (n1 / q.n) * q.sign;
            m = abs(a);
            if (m != 0) {
                sign = (a) / abs(a);
            } else {
                sign = 1;
            }
            simple();
        }

        private void simple() {
            if (m == 0) {
                n = 1;
            } else {
                long a = gcd(m, n);
                if (a > 1) {
                    m /= a;
                    n /= a;
                }
            }
        }
    }
}