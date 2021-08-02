import java.util.Scanner;
import java.util.Arrays;
import static java.lang.Math.*;

public class MaxNum {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Numbers[] bigNum = new Numbers[n];
        for (int i = 0; i < n; i++) {
            bigNum[i] = new Numbers(in.next());
        }

        Arrays.sort(bigNum);

        for (int i = 0; i < n; i++) {
            System.out.print(bigNum[i].getNumber());
        }
    }

    private static class Numbers implements Comparable<Numbers>{
        private String number;
        public Numbers(String n) {
            number = n;
        }

        public String getNumber() {
            return number;
        }

        public int compareTo(Numbers o) {
            int m = min(number.length(), o.number.length());
            for (int i = 0; i < m; i++) {
                if (number.charAt(i) != o.number.charAt(i)) {
                    return (o.number.charAt(i) - number.charAt(i));
                }
            }
            if (Integer.parseInt(number + o.number) < Integer.parseInt(o.number+number)) {
                return 1;
            } else if (Integer.parseInt(number+o.number) == Integer.parseInt(o.number+number)) {
                return 0;
            }
            return -1;
        }
    }
}