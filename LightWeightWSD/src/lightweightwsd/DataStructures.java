/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweightwsd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raj
 */
public class DataStructures {

    static HashMap<String, HashMap<String, Set<String>>> word_id_clues = new HashMap<String, HashMap<String, Set<String>>>();
    static HashMap<String, HashMap<String, HashMap<String, double[]>>> word_id_clues_with_heuristics = new HashMap<String, HashMap<String, HashMap<String, double[]>>>();
    static HashMap<String, HashMap<String, HashMap<String, Integer>>> word_id_clues_with_overlapped_clues = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
    static HashMap<String, String> synset_pos = new HashMap<String, String>();
    // For each word we store sense_id and clue_words as a pair in the hash map

    public static void read_synset_pos() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(Files.synset_pos_file));
        String line = "";
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String synsetid = line.split("\t")[0];
            String pos = line.split("\t")[1];
            if (pos.equals("null")) {
                continue;
            }

            synset_pos.put(synsetid, pos);
        }
    }

    public static void load_clues_with_heuristics() throws FileNotFoundException, IOException {
        read_synset_pos();
        String line = "";
        while ((line = Files.br_clues_with_heuristics.readLine()) != null) {
            String components[] = line.trim().split(",");
            //System.out.println(components.length);
            if (components.length < 9) {
                continue;
            }
            String sid = components[0];
            String word = components[1];
            String clue = components[2];
            //System.out.println(sid+","+word);

            double counts[] = new double[]{Double.parseDouble(components[3]), Double.parseDouble(components[4]),
                Double.parseDouble(components[5]), Double.parseDouble(components[6]),
                Double.parseDouble(components[7]), Double.parseDouble(components[8])};
            String pos = synset_pos.get(sid);
            if (word_id_clues_with_heuristics.containsKey(word + "_" + pos)) {
                HashMap<String, HashMap<String, double[]>> sid_clues_temp = word_id_clues_with_heuristics.get(word + "_" + pos);
                if (sid_clues_temp.containsKey(sid)) {
                    HashMap<String, double[]> clues_heur = sid_clues_temp.get(sid);
                    clues_heur.put(clue, counts);
                    sid_clues_temp.put(sid, clues_heur);
                } else {
                    HashMap<String, double[]> clues_heur = new HashMap<String, double[]>();
                    clues_heur.put(clue, counts);
                    sid_clues_temp.put(sid, clues_heur);
                }
                word_id_clues_with_heuristics.put(word + "_" + pos, sid_clues_temp);

            } else {
                HashMap<String, HashMap<String, double[]>> sid_clues_temp = new HashMap<String, HashMap<String, double[]>>();
                HashMap<String, double[]> clues_heur = new HashMap<String, double[]>();
                clues_heur.put(clue, counts);
                sid_clues_temp.put(sid, clues_heur);
                word_id_clues_with_heuristics.put(word + "_" + pos, sid_clues_temp);
            }
        }
    }

    public static void load_overlap_clues() throws FileNotFoundException, IOException {
        read_synset_pos();
        String line = "";
        HashMap<String, HashMap<String, Integer>> syn_clues = new HashMap<String, HashMap<String, Integer>>();
        while ((line = Files.br_clues_with_overlapped_corpus.readLine()) != null) {
            String components[] = line.trim().split("\t");
            String sid = components[0];
            String clue = components[1];
            Integer overlap_count = Integer.parseInt(components[2]);
            if (syn_clues.containsKey(sid)) {
                HashMap<String, Integer> clue_count = syn_clues.get(sid);
                clue_count.put(clue, overlap_count);
                syn_clues.put(sid, clue_count);
            } else {
                HashMap<String, Integer> clue_count = new HashMap<String, Integer>();
                clue_count.put(clue, overlap_count);
                syn_clues.put(sid, clue_count);
            }

        }
        

        BufferedReader br = new BufferedReader(new FileReader(Files.word_synset_file));
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String components[] = line.split("\t");
            if(word_id_clues_with_overlapped_clues.containsKey(components[1]+"_"+components[2])){
                HashMap<String,HashMap<String,Integer>> sid_clueset= word_id_clues_with_overlapped_clues.get(components[1]+"_"+components[2]);
                HashMap<String, Integer> clue_count = syn_clues.get(components[0]);
                if(clue_count==null){
                    clue_count = new HashMap<String, Integer>();
                }
                sid_clueset.put(components[0], clue_count);
                word_id_clues_with_overlapped_clues.put(components[1]+"_"+components[2], sid_clueset);
            } else {
                HashMap<String,HashMap<String,Integer>> sid_clueset= new HashMap<String, HashMap<String, Integer>>();
                HashMap<String, Integer> clue_count = syn_clues.get(components[0]);
                if(clue_count==null){
                    clue_count = new HashMap<String, Integer>();
                }
                sid_clueset.put(components[0], clue_count);
                word_id_clues_with_overlapped_clues.put(components[1]+"_"+components[2], sid_clueset);
            }
        }

    }

    public static void load_clues() throws FileNotFoundException, IOException {
        read_synset_pos();
        String line = "";
        try {

            HashMap<String, Set<String>> gloss_eg = new HashMap<String, Set<String>>();

            while ((line = Files.br_gloss.readLine()) != null) { // To get the clue words from example and gloss
                String components[] = line.split("\t");
                String sid = components[0];
                components[1] = components[1].replaceAll("[\\-\\+\\.\\^\\,\\?\"\\/]", "");
                String clues = components[1].replace("\\:", " ");
                Set<String> cluestemp = new HashSet<String>(Arrays.asList(clues.split(" ")));
                cluestemp.remove("");
                cluestemp.remove(" ");
                cluestemp.remove("  ");
                cluestemp.remove("   ");
                gloss_eg.put(sid, cluestemp);
            }
            String synset_id = "";
            String word = "";
            Set<String> clues;
            while ((line = Files.br_clues.readLine()) != null) {
                String[] components = line.split("\\|\\|\\|");
                synset_id = components[0];
                word = components[1] + "_" + synset_pos.get(synset_id);
                clues = new HashSet<String>(Arrays.asList(components[2].split("\t")));
                if (gloss_eg.containsKey(synset_id)) {
                    clues.addAll(gloss_eg.get(synset_id)); //Add the gloss and example words as clues
                }
                if (word_id_clues.containsKey(word)) {
                    HashMap<String, Set<String>> temp = word_id_clues.get(word);
                    temp.put(synset_id, clues);
                    word_id_clues.put(word, temp);
                } else {
                    HashMap<String, Set<String>> temp = new HashMap<String, Set<String>>();
                    temp.put(synset_id, clues);
                    word_id_clues.put(word, temp);
                }
            }
            BufferedReader br = new BufferedReader(new FileReader(Files.word_synset_file));
            while ((line = br.readLine()) != null) {
                line = line.trim();
                String components[] = line.split("\t");
                if (!word_id_clues.containsKey(components[1] + "_" + components[2])) {
                    HashMap<String, Set<String>> temp = new HashMap<String, Set<String>>();
                    temp.put(components[0], new HashSet<String>());
                    word_id_clues.put(components[1] + "_" + components[2], temp);
                }
            }


        } catch (IOException ex) {
            System.err.println("Error in reading file");
        }
    }
}
