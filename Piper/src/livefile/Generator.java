/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package livefile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author RAJ
 */
public class Generator {
    public static BufferedWriter wr = null;
    public static void main(String args[]) throws IOException, InterruptedException{
        wr = new BufferedWriter(new FileWriter("C:\\Users\\RAJ\\Documents\\NetBeansProjects\\Piper\\build\\classes\\livefile\\input.txt",true));
        int i=0;
        while(i<100){
           wr.write(Integer.toString(i)+"\n");
           wr.flush();
           Thread.sleep((500));
           i++;
        }
        wr.close();
    }

}
