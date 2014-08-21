/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweightwsd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author raj
 */
public class ParseTestFiles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        String base = Files.base+"/HIN-HEALTH";
        File dir = new File(base);
        if(dir.isDirectory()){
            for(File f:dir.listFiles()){
                System.out.println(f.getName());
                BufferedReader br = new BufferedReader(new FileReader(f));
                BufferedWriter bw = new BufferedWriter(new FileWriter(base+"-TEST/"+f.getName()+".withoutctx"));
                BufferedWriter bw1 = new BufferedWriter(new FileWriter(base+"-TEST/"+f.getName()+".wordsonly"));
                String line="";
                while((line=br.readLine())!=null){
                    line = line.trim();
                    line = line.replaceAll("[ ][ ]+", " ");
                    if(line.contains("ctx")){
                        continue;
                    } else{
                        bw.write(line+"\n");
                        bw.flush();
                        String[] components = line.split(" ");
                        for(String part:components){
                            String inner_components[] = part.split("#");
                            bw1.write(inner_components[0]+" ");
                            bw1.flush();
                        }
                        bw1.write("\n");
                        bw1.flush();
                    }
                }
                
            }
        }
    }
}
