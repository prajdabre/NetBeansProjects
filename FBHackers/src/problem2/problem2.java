package problem2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Pattern;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author RAJ
 */
public class problem2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/RAJ/Documents/NetBeansProjects/FBHackers/src/problem2/infile.txt")));
        String line = "";
        int caseno = 1;
        int counter=1;
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            line = line.toLowerCase().trim();
            //System.out.println(line);
            
            // line.re
            Pattern p1 = Pattern.compile("[a-z: ]");

            // p2 = Pattern.compile("\\("+p2.pattern()+"\\)");
            Pattern p3 = Pattern.compile("(:\\()");
            Pattern p4 = Pattern.compile("(:\\))");
            Pattern p5 = Pattern.compile("(" + p1.pattern() + "|" + p3.pattern() + "|" + p4.pattern() + ")*");
            Pattern p6 = Pattern.compile("(\\("+p5.pattern()+"\\))*");
            Pattern p7 = Pattern.compile("("+p5.pattern()+"|"+p6+")*");

            // Pattern p3 = Pattern.compile(p2.pattern());
            if (line.matches(p7.pattern())) {
                System.out.println("Case #"+counter+": YES");
            } else {
                System.out.println("Case #"+counter+": NO");
            }
            counter++;
        }
    }
}
