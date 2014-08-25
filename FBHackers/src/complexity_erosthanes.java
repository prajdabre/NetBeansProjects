/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class complexity_erosthanes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Double sum = 0.0;
        Double lim = 10000000000000000.0;
        for(int i=2;i<=Math.sqrt(lim);i++){
            sum+=1.0/i;
        }
        System.out.println(sum+" "+(sum-Math.log10(lim)));
    }

}
