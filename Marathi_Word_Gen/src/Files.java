
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author raj
 */
public class Files {
    public static String base = "/home/raj/NetBeansProjects/Marathi_Word_Gen/";
    public static String mapfile = "marathimonolingual.tokenized.mr.done";
    public static String infile = "/home/raj/public_html/infile";
    public static String outfile = "/home/raj/public_html/outfile1";
    public static BufferedReader br = null;
    public static BufferedReader suffix_split_file = null;
    public static BufferedWriter surface_word_file = null;
    
    public static void open_files(){
        try {
            suffix_split_file = new BufferedReader(new FileReader(new File(infile)));
            surface_word_file = new BufferedWriter(new FileWriter(new File(outfile)));
            System.out.println("Files opened for read and write");
        } catch (Exception ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
