import java.util.Scanner;

public class Kth {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long k = in.nextLong();

        if (k < 9) {
            System.out.println(k + 1);
            return;
        }

        long nine = 9;
        long symbols = 1;
        long overmax = 0;
        long overmin = 0;
        while (k >= overmax) {
            overmin = overmax;
            overmax += nine * symbols;
            nine *= 10;
            symbols++;
        }
        symbols--;
        overmin--;
        overmax--;

        long result = 0;
        result = ((k - overmin - 1) / symbols) + (long)(Math.pow(10, symbols - 1));

        long num;
        num = (symbols - ((k - overmin) % symbols)) % symbols;


        long answer = 0;
        for (long i = 0; i <= num; i++) {
            answer = result % 10;
            result /= 10;
        }

        System.out.println(answer);
    }
}