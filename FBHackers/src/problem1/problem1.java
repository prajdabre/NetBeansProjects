/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package problem1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author RAJ
 */
public class problem1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/RAJ/Documents/NetBeansProjects/FBHackers/src/problem1/infile.txt")));
        String line = "";
        int j = 1;
        line = br.readLine();
        int m = Integer.parseInt(line);
        if (m < 5 || m > 50) {
            System.err.print("Too many inputs\n");
            System.exit(1);
        }
        while ((line = br.readLine()) != null) {
            if (line.length() < 2 || line.length() > 500) {
                System.err.print("String too long\n");
                //System.exit(1);
            }
            line = line.toLowerCase();
            // System.out.println(line);
            HashMap<Character, Integer> counter = new HashMap<Character, Integer>();
            int sum = 0;

            for (Character c : line.toCharArray()) {
                //System.out.println(c);
                if (Character.isLetter(c)) {
                    if (counter.containsKey(c)) {
                        counter.put(c, counter.get(c) + 1);
                    } else {
                        counter.put(c, 1);
                    }
                }

            }
            List l = new ArrayList(counter.values());
            Collections.sort(l);
            Object l1[] = l.toArray();
            int curr = 26;
            for (int i = l1.length - 1; i >= 0; i--) {
                sum = sum + ((Integer) l1[i] * curr);
                curr--;
            }
            System.out.println("Case #" + j + ": " + sum);
            j++;

        }

    }
}
