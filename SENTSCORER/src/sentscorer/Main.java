/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sentscorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 *
 * @author RAJ
 */
public class Main {
    private static String dirpath;
    public static HashMap <String, Float> bigram = new HashMap<String, Float>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        // TODO code application logic here
        dirpath="I:/stuff/Novels";
        Genfilt filter=new Genfilt(".txt");
        
        File dir = new File(dirpath);
        File[] author=dir.listFiles();
        for(File f :author){
            if(f.isDirectory()){

                File[] stors=f.listFiles(filter);
                for(File infile : stors){

                    System.out.println("File name : "+infile.getName() );
                    trainer(infile);
                }
            }
            else{
                if(f.getName().endsWith("txt")){
                System.out.println("File name out: "+f.getName());
                trainer(f);
                }
            }
        }

    }

    private static void trainer(File infile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(infile), "UTF-8"));
        String line = reader.readLine();
        String prev="^";
		while (line != null) {

                        line = line.trim();
			// line= line.replaceAll(" +", " ");
			String[] words = line.split(" ");

			for (String string : words) {
                            if(string.equals("\"")||string.equals("\'"))
                            {continue;}
                            if(bigram.containsKey(string+"/"+prev))
                                bigram.put(string+"/"+prev,bigram.get(string+"/"+prev)+1);
                            else
                                bigram.put(string+"/"+prev,new Float(1));
                        if(string.equals(".") || string.equals("!") || string.equals(":")){
                            prev="^";
                            System.out.println("");
                            }
                            System.out.print(string);
                        }
                        line=reader.readLine();
                }
        reader.close();
    }


    private static class Genfilt implements FilenameFilter{
        String ext;
        public Genfilt(String string) {
            this.ext=string;
        }

        public boolean accept(File dir, String name) {
            return(name.endsWith(ext));
        }
    }

}
