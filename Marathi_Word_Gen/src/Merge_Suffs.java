
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.collections15.map.FastHashMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author raj
 */
public class Merge_Suffs {

    /**
     * @param args the command line arguments
     */
    public Set<String> sentence_candidates;

    public void combine_suffixes() throws IOException {
        System.out.println("Merging suffixes");
        //System.out.println("Object equality test:" + ((new range(1, 2)).equals(new range(1, 2))));
        String line = "";
        //System.out.println(DataStructures.morphemes_to_surface.containsKey("भारतत"));
        while ((line = Files.suffix_split_file.readLine()) != null) {
            HashMap<range, String> merger_range = new HashMap<range, String>();
            line = line.trim();
            String components[] = line.split(" ");
            //Run an O(N^2) algo over all substrings in a sentence and select largest translatable components
            for (int i = 0; i < components.length;) {
                int nextpos = i + 1;
                String key = components[i].trim();
                if (DataStructures.morphemes_to_surface.containsKey(key.trim())) {
                    System.out.println("Basic key: " + i + " " + i + " " + key);
                    merger_range.put(new range(i, i), DataStructures.morphemes_to_surface.get(key.trim()));
                }
                for (int j = i + 1; j <= i + 4 && j < components.length; j++) {
                    key = (key + components[j].trim()).trim();
                    System.out.println("Longer key: " + i + " " + j + " " + key);
                    if (DataStructures.morphemes_to_surface.containsKey(key.trim())) {
                        System.out.println("Longer key in DS");

                        merger_range.put(new range(i, j), DataStructures.morphemes_to_surface.get(key.trim()));
                        Set<range> keySet = merger_range.keySet();
                        HashMap<range, String> merger_range_new = new HashMap<range, String>(merger_range);
                        for (range r : keySet) {
                            if (r.from == i && r.to < j) {
                                merger_range_new.remove(r);
                                nextpos = j + 1;
                            }
                        }
                        merger_range = merger_range_new;
                    }
                }
                i = nextpos;
            }
            for (range r : merger_range.keySet()) {
                System.out.println(r.from + " " + r.to + " " + merger_range.get(r));
            }
            System.out.println("");
            for (int i = 0; i < components.length;) {
                int outerobtained = 0;
                for (int j = i; j < components.length; j++) {
                    int innerobtained = 0;
                    for (range r : merger_range.keySet()) {
                        if (r.from == i && r.to == j) {
                            Files.surface_word_file.write(merger_range.get(r) + " ");
                            innerobtained = -1;
                            break;
                        }
                    }
                    if (innerobtained != 0) {
                        i = j + 1;
                        outerobtained = -1;
                        break;
                    }
                }
                if (outerobtained == 0) {
                    Files.surface_word_file.write(components[i] + " ");
                    i = i + 1;

                }
            }

            Files.surface_word_file.write("\n");
        }
        Files.surface_word_file.flush();
        Files.surface_word_file.close();
        System.out.println("Merged");
    }

    public void combine_suffixes_optimal() throws IOException {
        System.out.println("Merging suffixes");

        String line = "";
        while ((line = Files.suffix_split_file.readLine()) != null) {
            line = line.trim().replaceAll("[ ][ ]+", " ");
            String components[] = line.split(" ");
            //HashMap<range, String> merger_range = new HashMap<range, String>();
            HashMap<Integer, HashMap<Integer, String>> merger_range = new HashMap<Integer, HashMap<Integer, String>>();

            for (int i = 0; i < components.length; i++) {
                int atleast_one = 0;
                for (int j = 0; j < components.length; j++) {
                    String key = "";

                    key = combine_range_string(i, j, components);
                    if (DataStructures.morphemes_to_surface.containsKey(key)) {
                        atleast_one = 1;
                        String surface = DataStructures.morphemes_to_surface.get(key);

                        if (merger_range.containsKey(i)) {
                            HashMap<Integer, String> end_point_and_morphemes = merger_range.get(i);
                            end_point_and_morphemes.put(j, surface);
                            merger_range.put(i, end_point_and_morphemes);
                        } else {
                            HashMap<Integer, String> end_point_and_morphemes = new HashMap<Integer, String>();
                            end_point_and_morphemes.put(j, surface);
                            merger_range.put(i, end_point_and_morphemes);
                        }

                    }
                }


                if (merger_range.containsKey(i)) {
                    HashMap<Integer, String> end_point_and_morphemes = merger_range.get(i);
                    end_point_and_morphemes.put(i, components[i]);
                    merger_range.put(i, end_point_and_morphemes);
                } else {
                    HashMap<Integer, String> end_point_and_morphemes = new HashMap<Integer, String>();
                    end_point_and_morphemes.put(i, components[i]);
                    merger_range.put(i, end_point_and_morphemes);
                }


            }
            sentence_candidates = new HashSet<String>();

            HashMap<Integer, String> beginners = merger_range.get(0);

            for (Integer end_point : beginners.keySet()) {
                get_all_surface_sentences(merger_range, components.length, beginners.get(end_point), end_point + 1);
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("temp")));
            for (String sent_cand : sentence_candidates) {
                bw.write(sent_cand + "\n");
            }
            bw.flush();
            bw.close();
            final ProcessBuilder pb = new ProcessBuilder("bash", "-c", "/home/raj/tools/kenlm/bin/query -n /home/raj/hi-mar/lm_marathi/marathi_lm.bin <temp");
            //final ProcessBuilder pb = new ProcessBuilder("bash", "-c", "/home/raj/tools/moses/bin/query  /home/raj/hi-mar/lm_marathi/marathimonolingual.blm null <temp");
            Process p = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            HashMap<String, Double> sent_score = new HashMap<String, Double>();
            ValueComparator bvc = new ValueComparator(sent_score);
            TreeMap<String, Double> sorted_sent_score = new TreeMap<String, Double>(bvc);


            for (String sent_cand : sentence_candidates) {
                String line_inner=input.readLine();
                System.out.println(line_inner);
                String scores[] = line_inner.split("\t");
                //System.out.println(scores);
                double ll = Double.parseDouble(scores[scores.length - 1].split(" ")[1]);
                sent_score.put(sent_cand, ll);

            }
            
            sorted_sent_score.putAll(sent_score);
            //System.out.println(sorted_sent_score);
            System.out.println("Best translated candidates");
            int count = 0;
            for (Map.Entry<String, Double> entry : sorted_sent_score.entrySet()) {
                String key = entry.getKey();
                Double value = entry.getValue();
                System.out.println(key);
                count++;
                if(count==3){
                    break;
                }
            }


            input.close();





        }
        Files.surface_word_file.flush();
        Files.surface_word_file.close();
        System.out.println("Merged");
    }

    public String combine_range_string(int from, int to, String str_array[]) {
        String merged = "";
        for (int i = from; i <= to; i++) {
            merged += str_array[i];
        }
        return merged;
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        DataStructures.load_map();
        Files.open_files();
        Merge_Suffs msuff = new Merge_Suffs();
        msuff.combine_suffixes_optimal();
    }

    public void get_all_surface_sentences(HashMap<Integer, HashMap<Integer, String>> merger_range, int length, String sent_till_now, int end_point) {
        if (end_point >= length) {
            sentence_candidates.add(sent_till_now);
            //System.out.println(sent_till_now);
            return;
        } else {
            HashMap<Integer, String> next = merger_range.get(end_point);
            //System.out.println(next);
            for (Integer next_end : next.keySet()) {
                get_all_surface_sentences(merger_range, length, sent_till_now + " " + next.get(next_end), next_end + 1);
            }
        }
    }
}

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;

    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

class range {

    public int from;
    public int to;

    public range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public boolean equals(range r1) {
        if (this.from == r1.from && this.to == r1.to) {
            return true;
        } else {
            return false;
        }
    }
}