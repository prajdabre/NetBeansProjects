/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pmi_for_discriminet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author raj
 */
public class PMI_FOR_DISCRIMINET {

    /**
     * @param args the command line arguments
     */
    private String corpus_file = "/home/raj/NetBeansProjects/PMI_FOR_DISCRIMINET/hindicorp_raj.txt";
    private String lexicon_file = "/home/raj/NetBeansProjects/PMI_FOR_DISCRIMINET/lexicon_hindi.txt";
    private String pmi_file = "/home/raj/NetBeansProjects/PMI_FOR_DISCRIMINET/pmi_hindi.txt";
    private HashMap<String,HashMap<String,Double>> pmi = new HashMap<String, HashMap<String, Double>>();
    private HashMap<String,Double> unigram = new HashMap<String, Double>();
    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private Double total_pairs = 0.0;
    private Double total_unigrams = 0.0;
    private Double total_unigrams_unique = 0.0;
    public void open_files() throws FileNotFoundException, IOException{
        br = new BufferedReader(new FileReader(corpus_file));
    }
    
    public void get_words() throws IOException{
        String line="";
        while((line=br.readLine())!=null){
            for(String part : line.split(" ")){
                total_unigrams++;
                if(unigram.containsKey(part)){
                    unigram.put(part, unigram.get(part)+1.0);
                } else{
                    unigram.put(part, 1.0);
                    total_unigrams_unique++;
                }
            }
        }
        System.out.println("completed reading corpus");
        System.out.println("Total unigrams: "+total_unigrams);
        
    }
    public void count_pmi(){
        
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        PMI_FOR_DISCRIMINET pmi_computer = new PMI_FOR_DISCRIMINET();
        pmi_computer.open_files();
        pmi_computer.get_words();
        
    }
}
