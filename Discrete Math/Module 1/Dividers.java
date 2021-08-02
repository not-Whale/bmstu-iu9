import java.util.Scanner;
import static java.lang.Math.*;

public class Dividers {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long x = in.nextLong();
        int size = 1024 * (int)(log(x)/log(E) + 1);
        long[] arr = new long[size];
        long[] primes = new long[size];
        int k = 0;
        int p = 0;
        boolean prime = true;
        System.out.println("graph {");
        int xsqrt;
        xsqrt = (int)sqrt(x);
        for (int i = 1; i <= xsqrt; i++) {
            if (x % i == 0) {
                arr[k] = i;
                k++;
                System.out.println(i);
                if (i*i != x) {
                    System.out.println(x / i);
                    arr[k] = x / i;
                    k++;
                }
            }
        }
        for (int i = 1; i < k; i++) {
            int sqrt = (int)sqrt(arr[i]);
            for (int j = 2; j <= sqrt; j++) {
                if (arr[i] % j == 0) {
                    prime = false;
                    break;
                }
            }
            if (prime) {
                primes[p] = arr[i];
                p++;
            } else {
                prime = true;
            }
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < p; j++) {
                if ((arr[i] % primes[j]) == 0) {
                    long b = arr[i]/primes[j];
                    System.out.println(arr[i] + " -- " + b);
                }
            }
        }
        System.out.println("}");
    }
}