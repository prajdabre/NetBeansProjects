/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pmi_for_discriminet;

import java.util.Arrays;

/**
 *
 * @author RAJ
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String s="raj george mummy daddy";
        String s1[] = Arrays.copyOfRange(s.split(" "), 0, 0);
        System.out.println(Arrays.toString(s1));
        for(String s2: s1){
            System.out.println(s2);
        }
        String s3 = "raj";
        System.out.println(s3.split("_").length);

    }

}
