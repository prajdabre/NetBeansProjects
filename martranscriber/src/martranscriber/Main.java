/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package martranscriber;

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author RAJ
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static HashMap<String,String> trscpfwd = new HashMap<String, String>();
    public static HashMap<String,String> trscpbkwd = new HashMap<String, String>();
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        BufferedReader rin = new BufferedReader(new FileReader(new File("./src/martranscriber/transcriptions.txt")));
        String line = "";
        while((line=rin.readLine())!=null){
            trscpfwd.put(line.split("\t")[0], line.split("\t")[1]);
            System.out.println(line.split("\t")[0]+" "+line.split("\t")[1]);
            trscpfwd.put(line.split("\t")[1], line.split("\t")[0]);
        }
        FileOutputStream fos = new FileOutputStream("./src/martranscriber/fwdtranscription");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(trscpfwd);
        oos.flush();
        oos.close();
        fos = new FileOutputStream("./src/martranscriber/bkwdtranscription");
        oos = new ObjectOutputStream(fos);
        oos.writeObject(trscpbkwd);
        oos.flush();
        oos.close();
    }

}
