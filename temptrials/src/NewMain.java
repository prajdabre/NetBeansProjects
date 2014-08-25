/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       int a1=2,b1=1;
       int a[];
       testrun a2=new testrun();
       a=a2.a(a1, b1);
        System.out.println(a[0]+" "+a[1]);

    }


}

class testrun

{
    int[] a(int i, int j)
    {
        int a[]=new int[2];
    int temp;
    temp=i;
    i=j;
    j=temp;
        System.out.println(i+" "+j);
        a[0]=i;a[1]=j;
        return(a);
    }


}