/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections15.map.FastHashMap;

/**
 *
 * @author raj
 */
public class GenerateMappings {

    /**
     * @param args the command line arguments
     */
    ReadGrammar rg = null;
    HashMap<String, ArrayList<String>> cats_to_morphemes_stem = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> morphemes_to_cats_stem = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> cats_to_morphemes_root = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> morphemes_to_cats_root = new HashMap<String, ArrayList<String>>();
    HashMap<String, HashMap<String, ArrayList<String>>> wordforms = new HashMap<String, HashMap<String, ArrayList<String>>>();
    HashMap<String, ArrayList<String>> roots_to_stems = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> stems_to_roots = new HashMap<String, ArrayList<String>>();
    Graph<String, String> grammar_as_graph = new DirectedSparseGraph<String, String>();
    HashSet<String> word_starters = null;
    //ArrayList<String> cms = null;

    public void initialize() {
        rg = new ReadGrammar();
        rg.read_terminals();
        word_starters = new HashSet<String>();
        String temp[] = new String[]{"ADJ_OF", "ADV", "AP_OF", "CAR", "CHA_ADV", "CHA_OF", "COM", "DF", "E_OF", "NST_CL", "NST_OPN", "OF", "OF_CHA", "OF_PL", "OF_REQ", "PART", "PN1_OF", "PN1_OF_PL", "PN2_ADJ_OF", "PN2_OF", "PN2_OF_PL", "PN3_OF", "PP", "PP_CL", "PP_OPN", "PP_REQ", "SW_OF", "SW_OF_PL", "V1", "V101", "V103", "V111", "V112", "V121", "V122", "V131", "V132", "V141", "V142", "V143", "V14_KR1", "V151", "V152", "V161", "V162", "V16_KR1", "V171", "V172", "V173", "V17_KR1", "V18", "V191", "V192", "V193", "V1_KR", "V1_KR1", "V2", "V20", "V211", "V212", "V21_KR1", "V221", "V222", "V22_KR1", "V3", "V3_KR", "V4", "V4_KR", "V5", "V5_KR", "V6", "V6_KR", "V71", "V72", "V73", "V81", "V82", "V83", "V91", "V92", "V93"};
        word_starters.addAll(Arrays.asList(temp));
        //temp = new String[] {"आ","ई","ईल","ऊन","ची","च्या","त","दा","ना","नी","ने","नंतर","बाबत","ला","शी","स","हून"};
        //cms.addAll(Arrays.asList(temp));
    }

    public void read_and_write_all_grammar() {
        this.rg.read_nominal_grammar();
        this.rg.read_verbal_grammar();
        this.rg.expand_grammar();
    }

    public void load_all_morphemes_in_memory() {
        System.out.println("Loading all morphemes");
        for (String s : rg.ds.terminals_list.keySet()) {
            BufferedReader br = null;
            try {
                ArrayList<String> morphemes = new ArrayList<String>();
                br = new BufferedReader(new FileReader(rg.ds.terminals_list.get(s).get(0)));
                String line = "";
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    morphemes.add(line);
                    if (this.morphemes_to_cats_stem.containsKey(line)) {
                        //System.out.println("Here");
                        ArrayList<String> cats = this.morphemes_to_cats_stem.get(line);
                        cats.add(s);
                        this.morphemes_to_cats_stem.put(line, cats);
                    } else {
                        ArrayList<String> cats = new ArrayList<String>();
                        cats.add(s);
                        this.morphemes_to_cats_stem.put(line, cats);
                    }
                }
                this.cats_to_morphemes_stem.put(s, morphemes);

            } catch (Exception ex) {
                Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // for(String s:this.cats_to_morphemes.keySet()){
        //     System.out.println(s+" : "+this.cats_to_morphemes.get(s));
        //  }

        //for (String s : this.morphemes_to_cats_stem.keySet()) {
        //    System.out.println(s + " : " + this.morphemes_to_cats_stem.get(s));
        //}
        System.out.println("Morphemes loaded");
    }

    public void root_infl_pair_add(Set<String> roots, String form) {
        //System.out.println("Generating root word and stem pairs");
        for (String s : roots) {
            if (roots_to_stems.containsKey(s)) {
                ArrayList<String> stems = roots_to_stems.get(s);
                if (!stems.contains(form)) {
                    stems.add(form);
                }
                roots_to_stems.put(s, stems);
            } else {
                ArrayList<String> stems = new ArrayList<String>();

                stems.add(form);

                roots_to_stems.put(s, stems);
            }


        }


        if (stems_to_roots.containsKey(form)) {
            ArrayList<String> root_list = stems_to_roots.get(form);

            for (String s : roots) {
                if (!root_list.contains(s)) {
                    root_list.add(s);
                }
            }
            stems_to_roots.put(form, root_list);

        } else {
            ArrayList<String> root_list = new ArrayList<String>();
            for (String s : roots) {
                if (!root_list.contains(s)) {
                    root_list.add(s);
                }
            }
            stems_to_roots.put(form, root_list);
        }
    }

    public void reduce_stems_to_root_forms() {
        System.out.println("Reducing to root form");
        for (String s : this.cats_to_morphemes_stem.keySet()) {
            if (!word_starters.contains(s)) {
                this.cats_to_morphemes_root.put(s, this.cats_to_morphemes_stem.get(s));

            } else {
                ArrayList<String> word_forms_stem = this.cats_to_morphemes_stem.get(s);
                ArrayList<String> word_forms_root = new ArrayList<String>();
                for (String s1 : word_forms_stem) {
                    ArrayList<String> analyses = this.wordforms.get(s).get(s1);
                    for (String an : analyses) {
                        String components[] = an.split("#");
                        for (String parts : components) {
                            String root = parts.split(",")[0];
                            if (!word_forms_root.contains(root)) {
                                word_forms_root.add(root);
                            }
                        }
                    }
                }

                this.cats_to_morphemes_root.put(s, word_forms_root);
            }

        }

        System.out.println("Categories to roots have been generated\nNow creating roots to categories");

        for (String s : this.morphemes_to_cats_stem.keySet()) {
            ArrayList<String> cats_of_stem = this.morphemes_to_cats_stem.get(s);
            for (String s1 : cats_of_stem) {
                if (!word_starters.contains(s1)) {
                    if (this.morphemes_to_cats_root.containsKey(s)) {
                        ArrayList<String> root_cats = this.morphemes_to_cats_root.get(s);
                        if (!root_cats.contains(s1)) {
                            root_cats.add(s1);
                        }
                        this.morphemes_to_cats_root.put(s, root_cats);
                    } else {
                        ArrayList<String> root_cats = new ArrayList<String>();
                        root_cats.add(s1);
                        this.morphemes_to_cats_root.put(s, root_cats);
                    }
                } else {
                    ArrayList<String> analyses = this.wordforms.get(s1).get(s);
                    for (String an : analyses) {
                        String components[] = an.split("#");
                        for (String parts : components) {
                            String root = parts.split(",")[0];
                            if (this.morphemes_to_cats_root.containsKey(root)) {
                                ArrayList<String> root_cats = this.morphemes_to_cats_root.get(root);
                                if (!root_cats.contains(s1)) {
                                    root_cats.add(s1);
                                }
                                this.morphemes_to_cats_root.put(root, root_cats);
                            } else {
                                ArrayList<String> root_cats = new ArrayList<String>();
                                root_cats.add(s1);
                                this.morphemes_to_cats_root.put(root, root_cats);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Reduction to root form complete");
        System.out.println("Length of morphemes_to_cats_stem is " + this.morphemes_to_cats_stem.size());
        System.out.println("Length of morphemes_to_cats_root is " + this.morphemes_to_cats_root.size());
        System.out.println("Length of cats_to_morphemes_stem is " + this.cats_to_morphemes_stem.size());
        System.out.println("Length of cats_to_morphemes_root is " + this.cats_to_morphemes_root.size());
        //for (String s : this.cats_to_morphemes_root.keySet()) {
        //    System.out.println(s + " : " + this.cats_to_morphemes_root.get(s));
        //}
    }

    public void serialize_data() {
        FileOutputStream fileOut = null;
        //String outobjects[] = new String[]{"morphemes_to_cats_stem", "morphemes_to_cats_root", "cats_to_morphemes_stem", "cats_to_morphemes_root"};
        HashMap<String, Object> data = new FastHashMap<String, Object>();
        data.put("morphemes_to_cats_stem", morphemes_to_cats_stem);
        data.put("morphemes_to_cats_root", morphemes_to_cats_root);
        data.put("cats_to_morphemes_stem", cats_to_morphemes_stem);
        data.put("cats_to_morphemes_root", cats_to_morphemes_root);
        data.put("roots_to_stems", roots_to_stems);
        data.put("stems_to_roots", stems_to_roots);
        data.put("grammar_as_graph", grammar_as_graph);
        System.out.println("Serializing");
        for (String s : data.keySet()) {
            try {
                fileOut = new FileOutputStream(s + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(data.get(s));
                out.close();
                fileOut.close();
                System.out.println("Serialized data is saved in " + s + ".ser");
            } catch (Exception ex) {
                Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileOut.close();
                } catch (Exception ex) {
                    Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Serialization complete");
        //System.out.println(cats_to_morphemes_stem.get("CM"));
    }

    public void load_expanded_grammar() {
        System.out.println("Converting grammar to graph");
        try {
            String line = "";
            while ((line = Files.expanded_gammar_file.readLine()) != null) {
                if (line.length() == 0 || line.equals("")) {
                    continue;
                } else {
                    int is_ssy = 0;
                    int is_ppopn = 0;
                    String components[] = line.trim().split(" ");
                    if (components.length == 2) {
                        if (!grammar_as_graph.containsVertex(components[1] + "-END")) {
                            grammar_as_graph.addVertex(components[1] + "-END");
                        }
                        continue;
                    }

                    if (!grammar_as_graph.containsVertex(components[1])) {
                        grammar_as_graph.addVertex(components[1]);
                    }
                    if (components[1].equals("SSY")) {
                        is_ssy = 1;
                    }

                    if (components[1].equals("PP_OPN")) {
                        is_ppopn = 1;
                    }

                    for (int i = 2; i < components.length - 1; i++) {

                        if (components[i].equals("SSY") && is_ssy == 0) {
                            is_ssy = 1;
                        } else if (components[i].equals("SSY") && is_ssy == 1) {
                            components[i] = "SSY1";
                        }

                        if (components[i].equals("PP_OPN") && is_ppopn == 0) {
                            is_ppopn = 1;
                        } else if (components[i].equals("PP_OPN") && is_ppopn == 1) {
                            components[i] = "PP_OPN1";
                        }
                        if (!grammar_as_graph.containsVertex(components[i])) {
                            grammar_as_graph.addVertex(components[i]);
                        }
                        if (!grammar_as_graph.containsEdge("e_" + components[i - 1] + "_" + components[i])) {
                            grammar_as_graph.addEdge("e_" + components[i - 1] + "_" + components[i], components[i - 1], components[i]);
                        }
                    }

                    components[components.length - 1] = components[components.length - 1] + "-END";
                    if (!grammar_as_graph.containsVertex(components[components.length - 1])) {
                        grammar_as_graph.addVertex(components[components.length - 1]);
                    }
                    if (!grammar_as_graph.containsEdge("e_" + components[components.length - 1 - 1] + "_" + components[components.length - 1])) {
                        grammar_as_graph.addEdge("e_" + components[components.length - 1 - 1] + "_" + components[components.length - 1], components[components.length - 1 - 1], components[components.length - 1]);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Finished converting grammar to graph");
    }
    
    public void load_expanded_grammar_depth() {
        System.out.println("Converting grammar to graph");
        try {
            String line = "";
            while ((line = Files.expanded_gammar_file.readLine()) != null) {
                if (line.length() == 0 || line.equals("")) {
                    continue;
                } else {
                    int is_ssy = 0;
                    int is_ppopn = 0;
                    int depth=0;
                    String components[] = line.trim().split(" ");
                    if (components.length == 2) {
                        if (!grammar_as_graph.containsVertex(components[1] + "-END")) {
                            grammar_as_graph.addVertex(components[1] + "-END");
                        }
                        continue;
                    }
                    
                    if (!grammar_as_graph.containsVertex(components[1] + "-" +Integer.toString(depth))) {
                        grammar_as_graph.addVertex(components[1] + "-" +Integer.toString(depth));
                    }
                    
                    depth++;
                    int i=2;
                    for (i = 2; i < components.length - 1; i++) {

                        
                        if (!grammar_as_graph.containsVertex(components[i] + "-" +Integer.toString(depth))) {
                            grammar_as_graph.addVertex(components[i] + "-" +Integer.toString(depth));
                        }
                        if (!grammar_as_graph.containsEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-" +Integer.toString(depth))) {
                            grammar_as_graph.addEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-" +Integer.toString(depth), components[i-1] + "-" +Integer.toString(depth-1), components[i] + "-" +Integer.toString(depth));
                            
                        }
                        depth++;
                    }
                    
                    if (!grammar_as_graph.containsVertex(components[i] + "-END")) {
                        grammar_as_graph.addVertex(components[i] + "-END");
                    }
                    
                    if (!grammar_as_graph.containsEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-END")) {
                            grammar_as_graph.addEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-END" , components[i-1] + "-" +Integer.toString(depth-1), components[i] + "-END");
                            
                        }
                    

                   
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Finished converting grammar to graph");
    }

    
    public void load_expanded_grammar_depth_upto_level(int num,ArrayList<String> types) {
        
        System.out.println("Converting grammar to graph");
        try {
            String line = "";
            while ((line = Files.expanded_gammar_file.readLine()) != null) {
                if (line.length() == 0 || line.equals("")) {
                    continue;
                } else {
                    int is_ssy = 0;
                    int is_ppopn = 0;
                    int depth=0;
                    String components[] = line.trim().split(" ");
                    if(components.length>num){
                        //System.out.println(line);
                        continue;
                    }
                    //System.out.println(types);
                    //System.out.println(types.size());
                    if(types.size()>0){
                        
                        if(!types.contains(components[0])){
                            //System.out.println(components[0]);
                            continue;
                        } else {
                            //System.out.println(components[0]);
                        }
                    }
                    if (components.length == 2) {
                        if (!grammar_as_graph.containsVertex(components[1] + "-END")) {
                            grammar_as_graph.addVertex(components[1] + "-END");
                        }
                        continue;
                    }
                    
                    if (!grammar_as_graph.containsVertex(components[1] + "-" +Integer.toString(depth))) {
                        grammar_as_graph.addVertex(components[1] + "-" +Integer.toString(depth));
                    }
                    
                    depth++;
                    int i=2;
                    for (i = 2; i < components.length - 1; i++) {

                        
                        if (!grammar_as_graph.containsVertex(components[i] + "-" +Integer.toString(depth))) {
                            grammar_as_graph.addVertex(components[i] + "-" +Integer.toString(depth));
                        }
                        if (!grammar_as_graph.containsEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-" +Integer.toString(depth))) {
                            grammar_as_graph.addEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-" +Integer.toString(depth), components[i-1] + "-" +Integer.toString(depth-1), components[i] + "-" +Integer.toString(depth));
                            
                        }
                        depth++;
                    }
                    
                    if (!grammar_as_graph.containsVertex(components[i] + "-END")) {
                        grammar_as_graph.addVertex(components[i] + "-END");
                    }
                    
                    if (!grammar_as_graph.containsEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-END")) {
                            grammar_as_graph.addEdge("e_" + components[i-1] + "-" +Integer.toString(depth-1) + "_" + components[i] + "-END" , components[i-1] + "-" +Integer.toString(depth-1), components[i] + "-END");
                            
                        }
                    

                   
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Finished converting grammar to graph");
    }
    
    public void check_loops() {
        System.out.println("Checking for loops");
        try {
            String line = "";
            while ((line = Files.expanded_gammar_file.readLine()) != null) {
                //System.out.println("Here");
                if (line.length() == 0 || line.equals("")) {
                    continue;
                } else {
                    Set<String> cats_to_check = cats_to_morphemes_root.keySet();
                    //System.out.println(cats_to_check);
                    //cats_to_check.remove("SSY");
                    for (String s1 : cats_to_check) {
                        String components[] = line.trim().split(" ");
                        int ssycount = 0;
                        for (String s : components) {
                            if (s.equals(s1)) {
                                ssycount++;
                            }
                        }
                        if (ssycount > 2) {
                            System.out.println(s1 + " Problemo");
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Finished converting grammar to graph");
    }

    public void read_infl_forms_into_datastructures() {
        try {
            String line;
            //System.out.println(MA.USER_HOME);
            while ((line = Files.repo_file.readLine()) != null) {
                //System.out.println(str);
                Pattern pEnd = Pattern.compile(">");
                Matcher mEnd = pEnd.matcher(line);
                line = mEnd.replaceAll("");

                Pattern pStart = Pattern.compile("<");
                Matcher mStart = pStart.matcher(line);
                line = mStart.replaceAll("");
                String bits[] = line.split(";");
                String group = bits[0];
                String form = bits[1];
                String analysis = bits[2];
                String analysis_split[] = analysis.trim().split("#");
                Set<String> roots = new HashSet<String>();
                for (String s : analysis_split) {
                    roots.add(s.split(",")[0]);
                }

                root_infl_pair_add(roots, form);
                if (wordforms.containsKey(group)) {
                    HashMap<String, ArrayList<String>> wordform = wordforms.get(group);
                    if (wordform.containsKey(form)) {
                        ArrayList<String> analyses = wordform.get(form);
                        if (!analyses.contains(analysis)) {
                            analyses.add(analysis);
                            wordform.put(form, analyses);
                        }
                    } else {
                        ArrayList<String> analyses = new ArrayList<String>();
                        analyses.add(analysis);
                        wordform.put(form, analyses);
                    }
                    wordforms.put(group, wordform);
                } else {
                    ArrayList<String> analyses = new ArrayList<String>();
                    analyses.add(analysis);
                    HashMap<String, ArrayList<String>> wordform = new HashMap<String, ArrayList<String>>();
                    wordform.put(form, analyses);
                    wordforms.put(group, wordform);
                }

//                if (cats_to_morphemes_stem.containsKey(group)) {
//                    ArrayList<String> stems = cats_to_morphemes_stem.get(group);
//                    if (!stems.contains(form)) {
//                        stems.add(form);
//                    }
//                    cats_to_morphemes_stem.put(group, stems);
//                } else {
//                    ArrayList<String> stems = new ArrayList<String>();
//                    stems.add(form);
//                    cats_to_morphemes_stem.put(group, stems);
//                }
//                
//                System.out.println("Cats to stems mapping done");
//                
//                if (cats_to_morphemes_root.containsKey(group)) {
//                    ArrayList<String> roots = cats_to_morphemes_root.get(group);
//                    String components[] = analysis.split("#");
//                    for (String s : components) {
//                        String root = s.split(",")[0];
//                        if (!roots.contains(root)) {
//                            roots.add(root);
//                        }
//                    }
//                    cats_to_morphemes_root.put(group, roots);
//                } else {
//                    ArrayList<String> roots = new ArrayList<String>();
//                    String components[] = analysis.split("#");
//                    for (String s : components) {
//                        String root = s.split(",")[0];
//                        roots.add(root);
//                    }
//                    cats_to_morphemes_root.put(group, roots);
//                }
//
//                System.out.println("Cats to roots mapping done");
//                
//                if (morphemes_to_cats_stem.containsKey(form)) {
//                    ArrayList<String> cats = morphemes_to_cats_stem.get(form);
//                    if (!cats.contains(group)) {
//                        cats.add(group);
//                    }
//                    morphemes_to_cats_stem.put(form, cats);
//                } else {
//                    ArrayList<String> cats = new ArrayList<String>();
//                    cats.add(group);
//                    morphemes_to_cats_stem.put(form, cats);
//                }
//
//                System.out.println("Stems to cats mapping done");
//                
//                String components[] = analysis.split("#");
//                for (String s : components) {
//                    String root = s.split(",")[0];
//                    if (morphemes_to_cats_root.containsKey(root)) {
//                        ArrayList<String> cats = morphemes_to_cats_root.get(root);
//                        if (!cats.contains(root)) {
//                            cats.add(root);
//                        }
//                        morphemes_to_cats_root.put(root, cats);
//                    } else {
//                        ArrayList<String> cats = new ArrayList<String>();
//                        cats.add(root);
//                        morphemes_to_cats_root.put(root, cats);
//                    }
//                }
//                
//                System.out.println("roots to cats mapping done");
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateMappings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        Files.open_files();
        GenerateMappings gf = new GenerateMappings();
        gf.initialize();
        //gf.read_and_write_all_grammar();
        //System.out.println(gf.rg.ds.terminals_list);
        gf.read_infl_forms_into_datastructures();
        gf.load_all_morphemes_in_memory();
        gf.reduce_stems_to_root_forms();
        //gf.load_expanded_grammar_depth();
        ArrayList<String> types = new ArrayList<String>();
        types.add("NOMINAL");
        //types.add("AKYS");
        System.out.println(types.size());
        //Files.expanded_gammar_file = new BufferedReader(new FileReader(new File(Files.expanded_grammar)));
        gf.load_expanded_grammar_depth_upto_level(3,types);
        gf.serialize_data();
        //gf.check_loops();
    }
}
