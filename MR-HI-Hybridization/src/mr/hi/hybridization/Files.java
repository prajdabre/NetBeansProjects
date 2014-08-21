package mr.hi.hybridization;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
    public static String base = "/home/raj/NetBeansProjects/MR-HI-Hybridization/";
    public static String marssffile = "infile.mr";
    public static String hinssffile = "infile.hi";
    public static String finalmarsentfile = "sentences.mr";
    public static String finalhinsentfile = "sentences.hi";
    public static String originalhinsentfile = "sentences.hi.original";
    public static BufferedReader marssfReader = null;
    public static BufferedReader hinssfReader = null;
    public static BufferedWriter finaltranslationWriter_mar = null;
    public static BufferedWriter finaltranslationWriter_hin = null;
    public static BufferedWriter originaltranslationWriter_hin = null;
    public static void open_files(){
        try {
            marssfReader = new BufferedReader(new FileReader(marssffile));
            hinssfReader = new BufferedReader(new FileReader(hinssffile));
            finaltranslationWriter_mar= new BufferedWriter(new FileWriter(finalmarsentfile));
            finaltranslationWriter_hin= new BufferedWriter(new FileWriter(finalhinsentfile));
            originaltranslationWriter_hin= new BufferedWriter(new FileWriter(originalhinsentfile));
            
            System.out.println("Files opened for read and write");
        } catch (Exception ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
