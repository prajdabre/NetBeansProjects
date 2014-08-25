
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Solution {

    public static void main(String args[]) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        int test_cases = 0;
        test_cases = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < test_cases; i++) {
            line = br.readLine().trim();
            int low = Integer.parseInt(line.split(" ")[0]);
            int hi = Integer.parseInt(line.split(" ")[1]);
            int primecands[] = new int[hi + 1];
            for (int j = low; j <= hi; j++) {
                primecands[j] = j;
            }
            primecands[1] = 0;
            for (int k = 2; k <= Math.sqrt(hi); k++) {
                int l = 0;
                if (low > k) {
                    if (low % k == 0) {
                        l = low;
                    } else {
                        l = low + 1;
                    }
                } else {
                    l = 2 * k;
                }

                for (; l <= hi; l = l + k) {
                    if (primecands[l] % k == 0) {
                        primecands[l] = 0;
                    }
                }
            }
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (int j = 0; j <= 9; j++) {
                map.put(j, 0);
            }
            int isprime = 0;
            for (int j = low; j <= hi; j++) {
                if (primecands[j] != 0) {
                    isprime = 1;
                    int prime = primecands[j];
                    while (prime != 0) {
                        map.put(prime % 10, map.get(prime % 10) + 1);
                        prime = prime / 10;
                    }
                }
            }
            if (isprime == 0) {
                System.out.println(-1);
            } else {
                int max = 0;
                int digit = 0;
                for (int j = 0; j <= 9; j++) {
                    if (max <= map.get(j)) {
                        max = map.get(j);
                        digit = j;
                    }
                }

                System.out.println(digit);
            }
        }
    }
}
