/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweightwsd;

import iitb.cfilt.cpost.ConfigReader;
import iitb.cfilt.cpost.dmstemmer.MAResult;
import iitb.cfilt.cpost.dmstemmer.MorphOutput2;
import iitb.cfilt.cpost.dmstemmer.NewStemmer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author raj
 */
public class Clue_Morpher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        //DataStructures.read_synset_pos();
        HashMap<String, Set<String>> word_roots = new HashMap<String, Set<String>>();
        HashSet<String> root_words = new HashSet<String>();
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(Files.word_synset_file));
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String components[] = line.split("\t");
            root_words.add(components[1]);
        }
        System.out.println(root_words.size());
        
        try {
            
            HashMap<String, Set<String>> gloss_eg = new HashMap<String, Set<String>>();
            Files.init();
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
            Set<String> clues = new HashSet<String>();
            while ((line = Files.br_clues.readLine()) != null) {
                String[] components = line.split("\\|\\|\\|");
                synset_id = components[0];
                word = components[1];
                clues.addAll(Arrays.asList(components[2].split("\t")));
                if (gloss_eg.containsKey(synset_id)) {
                    clues.addAll(gloss_eg.get(synset_id)); //Add the gloss and example words as clues
                }
                
            }
            
            
            ConfigReader.read("/home/raj/NetBeansProjects/HinMA/HindiLinguisticResources/hindiConfig1");
            NewStemmer ns = new NewStemmer();
            MAResult mar = null;
            System.out.println(clues.size());
            //clues.retainAll(root_words);
            //System.out.println(clues.size());
//            mar = ns.stem("लोगों");
//                int size = mar.getSize();
//                //String rootsCat[] = new String[size];
//                if (size == 0) {
//                    System.out.println("blib");
//                } else {
//                    
//                    Vector<MorphOutput2> x = mar.getMorphOutputs();
//                    for (int k = 0; k < size; k++) {
//                        System.out.println(x.get(k).getRoot());
//                        
//                    }
//                    
//                    
//                }
//                System.exit(1);
            
            int i = 1;
            for (String s : clues) {
                //System.out.println(s);
                if (root_words.contains(s)) {
                    if (word_roots.containsKey(s)) {
                        Set<String> rooted = word_roots.get(s);
                        rooted.add(s);
                        word_roots.put(s, rooted);
                    } else {
                        Set<String> rooted = new HashSet<String>();
                        rooted.add(s);
                        word_roots.put(s, rooted);
                    }
                }
                mar = ns.stem(s);
                int size = mar.getSize();
                //String rootsCat[] = new String[size];
                Set<String> new_roots = new HashSet<String>();
                if (size == 0) {
                    new_roots.add(s);
                } else {
                    
                    Vector<MorphOutput2> x = mar.getMorphOutputs();
                    for (int k = 0; k < size; k++) {
                        new_roots.add(x.get(k).getRoot());
                        
                    }
                    
                    
                }
                
                if (word_roots.containsKey(s)) {
                    Set<String> rooted = word_roots.get(s);
                    rooted.addAll(new_roots);
                    word_roots.put(s, rooted);
                } else {
                    Set<String> rooted = new HashSet<String>();
                    rooted.addAll(new_roots);
                    word_roots.put(s, rooted);
                }
                //System.out.println(i);
            if(i%100 == 0){
                System.out.println(i+" clues have been morphed");
                
                //break;
            }
            i++;
            }
            Files.init();
            BufferedWriter bw = new BufferedWriter(new FileWriter("word_clues_file_rooted_clues"));
            while ((line = Files.br_clues.readLine()) != null) {
                String[] components = line.split("\\|\\|\\|");
                synset_id = components[0];
                word = components[1];
                clues = new HashSet<String>();
                clues.addAll(Arrays.asList(components[2].split("\t")));
                if (gloss_eg.containsKey(synset_id)) {
                    clues.addAll(gloss_eg.get(synset_id)); //Add the gloss and example words as clues
                }
                String final_clues = "";
                for(String s:clues){
                    if(word_roots.containsKey(s)){
                        Set<String> rooted_clues = word_roots.get(s);
                        for(String s1:rooted_clues){
                            final_clues+=s1+"\t";
                        }
                    } else {
                        final_clues+=s+"\t";
                    }
                    
                    
                }
                final_clues = final_clues.trim();
                bw.write(synset_id+"|||"+word+"|||"+final_clues+"\n");
                bw.flush();
                
            }
            bw.close();
            
            
            
            
            
        } catch (IOException ex) {
            System.err.println("Error in reading file");
        }
    }
}
