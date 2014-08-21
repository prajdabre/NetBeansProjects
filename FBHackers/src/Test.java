/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int a[]={1};
        Test t = new Test();
        t.increment(a);
        System.out.println(a[a.length-1]);
    }
    void increment(int a[]){
        a[a.length-1]++;
    }

}
