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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author raj
 */
public class PMI_FOR_DISCRIMINET {

    /**
     * @param args the command line arguments
     */
    private String corpus_file = "/NetBeansProjects/PMI_FOR_DISCRIMINET/hindicorp_raj10000.txt";
    private String lexicon_file = "/NetBeansProjects/PMI_FOR_DISCRIMINET/lexicon_hindi.txt";
    private String pmi_file = "/NetBeansProjects/PMI_FOR_DISCRIMINET/pmi_hindi.txt";
    private HashMap<String, HashMap<String, Double>> pmi = new HashMap<String, HashMap<String, Double>>();
    private HashMap<String, Double> unigram = new HashMap<String, Double>();
    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private Double total_pairs = 0.0;
    private Double total_unigrams = 0.0;
    private Double total_unigrams_unique = 0.0;
    private Double total_bigrams_count = 0.0;

    public void open_files(String base) throws FileNotFoundException, IOException {
        br = new BufferedReader(new FileReader(base + corpus_file));
    }

    public void get_counts(String base) throws IOException {
        String line = "";
        while ((line = br.readLine()) != null) {
            for (String part : line.split(" ")) {
                total_unigrams++;
                if (unigram.containsKey(part)) {
                    unigram.put(part, unigram.get(part) + 1.0);
                } else {
                    unigram.put(part, 1.0);
                    total_unigrams_unique++;
                }
            }

            String split_line[] = line.split(" ");
            for (int i = 0; i < split_line.length; i++) {
                String before[] = Arrays.copyOfRange(split_line, 0, i);
                String after[] = Arrays.copyOfRange(split_line, i + 1, split_line.length);
                String word = split_line[i];
                if (pmi.containsKey(word)) {
                    HashMap<String, Double> temp = pmi.get(word);
                    for (String b : before) {
                        if (temp.containsKey(b)) {
                            temp.put(b, temp.get(b) + 1.0);
                        } else {
                            temp.put(b, 1.0);
                        }
                    }
                    for (String a : after) {
                        total_bigrams_count++;
                        if (temp.containsKey(a)) {
                            temp.put(a, temp.get(a) + 1.0);
                        } else {
                            temp.put(a, 1.0);
                        }
                    }
                    pmi.put(word, temp);
                } else {
                    HashMap<String, Double> temp = new HashMap<String, Double>();
                    for (String b : before) {
                        if (temp.containsKey(b)) {
                            temp.put(b, temp.get(b) + 1.0);
                        } else {
                            temp.put(b, 1.0);
                        }
                    }
                    for (String a : after) {
                        total_bigrams_count++;
                        if (temp.containsKey(a)) {
                            temp.put(a, temp.get(a) + 1.0);
                        } else {
                            temp.put(a, 1.0);
                        }
                    }
                    pmi.put(word, temp);
                }
            }

        }
        System.out.println("completed reading corpus");
        System.out.println("Total unigrams: " + total_unigrams);
        System.out.println("Total unique unigrams: " + total_unigrams_unique);
        System.out.println("Total number of bigrams: " + total_bigrams_count);

    }

    public void generate_pmi() {
        Iterator it = unigram.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            unigram.put((String) pairs.getKey(), (Double) pairs.getValue() / total_unigrams);
            // avoids a ConcurrentModificationException
        }

//        it = unigram.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pairs = (Map.Entry) it.next();
//            System.out.println(pairs.getKey()+" "+pairs.getValue());
//             // avoids a ConcurrentModificationException
//        }

        it = pmi.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String key = (String) pairs.getKey();
            HashMap<String, Double> temp = (HashMap<String, Double>) pairs.getValue();
            Iterator it_inner = temp.entrySet().iterator();
            while (it_inner.hasNext()) {
                Map.Entry pairs_inner = (Map.Entry) it_inner.next();
                Double joint_prob=(Double) pairs_inner.getValue() / total_bigrams_count;
                Double key_prob = unigram.get(key);
                Double val_prob = unigram.get((String) pairs_inner.getKey());
                Double pmi_value = Math.log10(joint_prob/(key_prob*val_prob))/Math.log10(2);
                temp.put((String) pairs_inner.getKey(), pmi_value);
                // avoids a ConcurrentModificationException
            }
            pmi.put(key, temp);



        }

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        PMI_FOR_DISCRIMINET pmi_computer = new PMI_FOR_DISCRIMINET();
        pmi_computer.open_files("C:/Users/RAJ/Documents");
        pmi_computer.get_counts("C:/Users/RAJ/Documents");
        pmi_computer.generate_pmi();
    }
}
