import java.math.BigInteger;
import java.util.Scanner;

public class FastFib {
    public static void main(String[] args) {
        BigInteger[][] Q = {
                { BigInteger.ONE, BigInteger.ONE },
                { BigInteger.ONE, BigInteger.ZERO }
        };
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        BigInteger[][] result = new BigInteger[2][2];
        if (n % 2 == 0) {
            result = oddMull(Q, n - 1);
        } else {
            result = evenMull(Q, n - 1);
        }
        System.out.println(result[0][0]);
    }

    public static BigInteger[][] oddMull(BigInteger[][] M, int n) {
        return mull(evenMull(M, n - 1), M);
    }

    public static BigInteger[][] evenMull(BigInteger[][] M, int n) {
        if (n == 0) {
            BigInteger[][] result = {{BigInteger.ONE, BigInteger.ZERO}, {BigInteger.ZERO, BigInteger.ONE}};
            return result;
        } else if ((n / 2) % 2 == 0) {
            return mull(evenMull(M, n/2), evenMull(M, n/2));
        } else {
            return mull(oddMull(M, n/2), oddMull(M, n/2));
        }
    }

    public static BigInteger[][] mull(BigInteger[][] M1, BigInteger[][] M2) {
        BigInteger[][] RM = new BigInteger[2][2];
        RM[0][0] = ((M1[0][0].multiply(M2[0][0])).add(M1[0][1].multiply(M2[1][0])));
        RM[0][1] = ((M1[0][0].multiply(M2[0][1])).add(M1[0][1].multiply(M2[1][1])));
        RM[1][0] = ((M1[1][0].multiply(M2[0][0])).add(M1[1][1].multiply(M2[0][1])));
        RM[1][1] = ((M1[1][0].multiply(M2[0][1])).add(M1[1][1].multiply(M2[1][1])));
        return RM;
    }
}