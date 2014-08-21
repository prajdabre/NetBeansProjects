/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package problem3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author RAJ
 */
public class problem3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

        BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/RAJ/Documents/NetBeansProjects/FBHackers/src/problem3/infile.txt")));
        String line = "";
        int caseno = 1;
        line = br.readLine().trim();
        int T = Integer.parseInt(line);
        //System.out.println(T);

        for (int cno = 1; cno <= T; cno++) {
            line = br.readLine().trim();
            Integer n = Integer.parseInt(line.split(" ")[0]), k = Integer.parseInt(line.split(" ")[1]);
            line = br.readLine().trim();

            Integer a = Integer.parseInt(line.split(" ")[0]), b = Integer.parseInt(line.split(" ")[1]), c = Integer.parseInt(line.split(" ")[2]), r = Integer.parseInt(line.split(" ")[3]);
            //Integer m[] = new Integer[n];
            //m[0] = a;
            List<Integer> prevk = new ArrayList<Integer>();
            List<Integer> sortlist;
            prevk.add(a);
            int temp1=0;
            //System.out.println(numlist[0]);
            for (int i = 1; i < k; i++) {
                temp1 = (b * prevk.get(i-1) + c) % r;
                //System.out.println(i+" "+m[i]);
                prevk.add(temp1);
                //System.out.println(temp1);
            }
            //System.out.println(m[56]);

            for (int i = k; i < 2*k; i++) {
                int indic = 0;
                sortlist = new ArrayList<Integer>(prevk);
                //System.out.println(prevk.size());
                //System.out.println(i+" "+sortlist.size());
                Collections.sort(sortlist);
                // System.out.println(prevk.get(5)+" "+sortlist.get(5));
                if (sortlist.get(0) > 0) {
                    temp1 = sortlist.get(0) - 1;
                    //System.out.println(i+" "+sortlist.get(5)+" "+prevk.get(5)+" "+m[i]);
                } else {

                    for (int j = 1; j < k; j++) {
                        int temp = (Integer) sortlist.get(j) - (Integer) sortlist.get(j - 1);
                        if (temp > 1) {

                            temp1 = (Integer) sortlist.get(j - 1) + 1;
                            //System.out.println(i+" "+sortlist.get(5)+" "+prevk.get(5)+" "+m[i]);
                            indic = 1;
                            break;
                        }
                    }
                    if (indic == 0) {
                        temp1 = (Integer) sortlist.get(k - 1) + 1;
                    }
                }

                //System.out.println(i+" "+sortlist.get(0));
                prevk.remove(0);
                //m[i]=(Integer)sortlist.get(0)-1;
                prevk.add(temp1);
                //System.out.println(temp1);
            }
            //System.out.println(prevk.size());
            int t1=(n-k)%k;
            System.out.println("Case #" + caseno + ": " + prevk.get(t1-2));
            caseno++;
            System.gc();
        }
        br.close();

    }
}
