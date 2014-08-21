
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author RAJ
 */
public class r1p1 {

    /**
     * @param args the command line arguments
     */
    public static double fact(int num) {
        double fact = 1, i;
        for (i = 1; i <= num; i++) {
            fact = fact * i;
        }
        return fact;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/RAJ/Documents/NetBeansProjects/FBHackers/src/infile-1.txt")));
        String line = "";
        int cases = Integer.parseInt(br.readLine().trim());
        //System.out.println(cases);
        for (int i = 0; i < cases; i++) {
            line = br.readLine().trim();
            int n = Integer.parseInt(line.split(" ")[0]);
            int k = Integer.parseInt(line.split(" ")[1]);
            ArrayList<Integer> a = new ArrayList<Integer>();
            line = br.readLine().trim();
            int count = 0;
            for (String s : line.split(" ")) {
                a.add(Integer.parseInt(s));
                count++;
            }
            Collections.sort(a);
            //System.out.println(a.get(a.size()-1));
            double sum = 0;
            for (int j = 1; j <= (n-k+1); j++) {
                double mulfact = (fact(n-j) / (fact(k-1) ));
                System.out.println(fact(k-1));
                mulfact = mulfact /fact(n-j-k+1);
                
                sum=sum+(mulfact * a.get(n-j));
            }
            System.out.println("Case #"+(i+1)+": "+(int)(sum%1000000007));

        }

    }
}
