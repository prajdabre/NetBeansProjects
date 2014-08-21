/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweightwsd;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raj
 */
public class Files {

    static String base = "/home/raj/NetBeansProjects/LightWeightWSD/";
    static String clues_file = base + "word_clues_file_rooted_clues";
    static String clues_file_with_heuristics = base + "word_clues_file_with_heuristics";
    static String clues_file_with_overlapped_clues = base + "overlap_clues_segregated.txt";
    static String gloss_eg_file = base + "glosses.csv";
    static String tagged_file = base + "infile.hi";
    static String wsd_file = base + "outfile.hi";
    static String word_synset_file = base + "word_synset";
    static BufferedReader br_clues;
    static BufferedReader br_clues_with_heuristics;
    static BufferedReader br_clues_with_overlapped_corpus;
    static BufferedReader br_gloss;
    static PrintStream ps_wsd;
    static BufferedReader pos_tagged;
    static String synset_pos_file = base+"posfile.csv";
    public static void init()  {
        try {
            br_clues = new BufferedReader(new FileReader(clues_file));
            br_gloss = new BufferedReader(new FileReader(gloss_eg_file));
            br_clues_with_heuristics = new BufferedReader(new FileReader(clues_file_with_heuristics));
            br_clues_with_overlapped_corpus = new BufferedReader(new FileReader(clues_file_with_overlapped_clues));
            
            
        } catch (FileNotFoundException ex) {
            System.err.println("Files not found OR Error in file opening");
        }
    }
    
}
