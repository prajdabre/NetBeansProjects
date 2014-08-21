/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;

/**
 *
 * @author raj
 */
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

//Service Implementation
@WebService(endpointInterface = "transducerinverter.WordGenServiceInterface")
public final class WordGenService implements WordGenServiceInterface {

    HashMap<String, ArrayList<String>> cats_to_morphemes_stem = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> morphemes_to_cats_stem = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> cats_to_morphemes_root = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> morphemes_to_cats_root = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> roots_to_stems = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> stems_to_roots = new HashMap<String, ArrayList<String>>();
    Graph<String, String> grammar_as_graph = new DirectedSparseGraph<String, String>();
    Graph<String, String> words_as_graph = new DirectedSparseGraph<String, String>();

    public void load_data_from_serialized_objects() {
        try {
            System.out.println("Loading objects into memory");
            FileInputStream fileIn = null;
            ObjectInputStream in = null;
            fileIn = new FileInputStream("cats_to_morphemes_stem.ser");
            in = new ObjectInputStream(fileIn);
            cats_to_morphemes_stem = (HashMap<String, ArrayList<String>>) in.readObject();

            fileIn = new FileInputStream("cats_to_morphemes_root.ser");
            in = new ObjectInputStream(fileIn);
            cats_to_morphemes_root = (HashMap<String, ArrayList<String>>) in.readObject();

            fileIn = new FileInputStream("morphemes_to_cats_stem.ser");
            in = new ObjectInputStream(fileIn);
            morphemes_to_cats_stem = (HashMap<String, ArrayList<String>>) in.readObject();

            fileIn = new FileInputStream("morphemes_to_cats_root.ser");
            in = new ObjectInputStream(fileIn);
            morphemes_to_cats_root = (HashMap<String, ArrayList<String>>) in.readObject();

            fileIn = new FileInputStream("roots_to_stems.ser");
            in = new ObjectInputStream(fileIn);
            roots_to_stems = (HashMap<String, ArrayList<String>>) in.readObject();

            fileIn = new FileInputStream("stems_to_roots.ser");
            in = new ObjectInputStream(fileIn);
            stems_to_roots = (HashMap<String, ArrayList<String>>) in.readObject();

            fileIn = new FileInputStream("grammar_as_graph.ser");
            in = new ObjectInputStream(fileIn);
            grammar_as_graph = (Graph<String, String>) in.readObject();

        } catch (Exception ex) {
            Logger.getLogger(GenerateSurfaceForms.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Objects loaded");
        //System.out.println(grammar_as_graph.getEdgeCount());
        //System.out.println(grammar_as_graph.getSuccessors("SSY1"));

    }

    public WordGenService() {
        load_data_from_serialized_objects();
    }

    @Override
    public String getSurfaceForm(String root) {

        String curr = root;
        HashSet<String> final_words = new HashSet<String>();
        if (!morphemes_to_cats_root.containsKey(curr)) {
            //final_words.add(root);
            return root;
        }

        ArrayList<String> cats = morphemes_to_cats_root.get(curr);
        
        System.out.println(cats.size());
        for (int i = 0; i < 3 || i < cats.size(); i++) {
            int word_counts = 0;
            String cat = cats.get(i);
            
            System.out.println(cat);
            Stack<String> dfs_stack = new Stack<String>();
            ArrayList<String> curr_word = new ArrayList<String>();
            curr_word.add(root);
            ArrayList<String> outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(cat));
            System.out.println(outEdges.size());
            for (int j = outEdges.size()-1; j >= 0; j--) {
                dfs_stack.push(outEdges.get(j));
            }
            int quit_current=0;
            int iter=0;
            String prev_cat = cat;
            ArrayList<String> cats_already_done = new ArrayList<String>();
            cats_already_done.add(cat);
            while (!dfs_stack.empty()) {
                
                String curr_cat = dfs_stack.pop();
                if(cats_already_done.contains(curr_cat)){
                    continue;
                }
                System.out.println(curr_cat);
                String curr_morpheme = cats_to_morphemes_stem.get(curr_cat.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", "")).get(0);
                curr_word.add(curr_morpheme);
                cats_already_done.add(curr_cat);
                if (grammar_as_graph.getSuccessorCount(curr_cat) == 0) {
                    String word = "";
                    for (int k = 0; k < curr_word.size(); k++) {
                        word += curr_word.get(k);
                    }
                    System.out.println(word);
                    final_words.add(word);
                    word_counts++;
                    curr_word.remove(curr_word.size() - 1);
                    cats_already_done.remove(cats_already_done.size()-1);
                    if (word_counts >= 5 || curr_word.size() <= 0) {
                        quit_current =1;
                        break;
                    }
                } else {
                    outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(curr_cat));
                    for (int j = outEdges.size()-1; j >= 0; j--) {
                        dfs_stack.push(outEdges.get(j));
                    }
                }
                prev_cat = curr_cat;
            }
            
            if(quit_current==1){
                continue;
            }
        }
        String words_as_string="";
        for(String s: final_words){
            words_as_string+=s+"#";
        }
        words_as_string = words_as_string.substring(0, words_as_string.length()-2);
        return words_as_string;
        //System.out.print(curr + " ");
        
    }
    
    @Override
    public String getSurfaceFormMod(String root) {

        String curr = root;
        HashSet<String> final_words = new HashSet<String>();
        if (!morphemes_to_cats_root.containsKey(curr)) {
            //final_words.add(root);
            return root;
        }
        curr = roots_to_stems.get(root).get(1);
        System.out.println(roots_to_stems.get(root).get(0));
        ArrayList<String> cats_temp = morphemes_to_cats_stem.get(curr);
        ArrayList<String> cats = new ArrayList<String>();
        for(String s: cats_temp){
            cats.add(s+"-0");
        }
        if(cats.contains("DF-0")){
            cats.add("DF-END");
        }
        ArrayList<String> infls = roots_to_stems.get(root);
        System.out.println(cats.size());
        for (int i = 0; i < 2 && i < cats.size(); i++) {
            int word_counts = 0;
            String cat = cats.get(i);
            System.out.println(cat);
            Stack<String> dfs_stack = new Stack<String>();
            ArrayList<String> curr_word = new ArrayList<String>();
            curr_word.add(curr);
            ArrayList<String> outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(cat));
            //System.out.println(outEdges.size());
            for (int j = outEdges.size()-1; j >= 0; j--) {
                dfs_stack.push(outEdges.get(j));
            }
            System.out.println(outEdges);
            int quit_current=0;
            int iter=0;
            String prev_cat = cat;
            int prev_level=0;
            ArrayList<String> cats_already_done = new ArrayList<String>();
            cats_already_done.add(cat);
            while (!dfs_stack.empty()) {
                //System.out.println("WHat?");
                String curr_cat = dfs_stack.peek();
                System.out.println(curr_cat);
                if(curr_cat.contains("-END")){
                    System.out.println(curr_cat);
                    ArrayList<String> stems = cats_to_morphemes_stem.get(curr_cat.split("-")[0]);
                    String curr_morpheme="";
                    for(int pos = 0; pos<stems.size();pos++){
                        if(!curr_word.contains(stems.get(pos))){
                            curr_morpheme=stems.get(pos);
                        }
                    }
                    curr_word.add(curr_morpheme);
                    String word = "";
                    for (int k = 0; k < curr_word.size(); k++) {
                        word += curr_word.get(k);
                    }
                    final_words.add(word);
                    word_counts++;
                    curr_word.remove(curr_word.size() - 1);
                    dfs_stack.pop();
                    if(word_counts==5){
                        quit_current=1;
                        break;
                    }
                    
                } else if(Integer.parseInt(curr_cat.split("-")[1])==prev_level+1){
                    System.out.println(curr_cat);
                    outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(curr_cat));
                    System.out.println(outEdges);
                    for (int j = outEdges.size()-1; j >= 0; j--) {
                        dfs_stack.push(outEdges.get(j));
                    }
                    ArrayList<String> stems = cats_to_morphemes_stem.get(curr_cat.split("-")[0]);
                    String curr_morpheme="";
                    for(int pos = 0; pos<stems.size();pos++){
                        if(!curr_word.contains(stems.get(pos))){
                            curr_morpheme=stems.get(pos);
                        }
                    }
                    
                    curr_word.add(curr_morpheme);
                    prev_level++;
                } else if (Integer.parseInt(curr_cat.split("-")[1])==prev_level-1 || Integer.parseInt(curr_cat.split("-")[1])==prev_level){
                    curr_word.remove(curr_word.size() - 1);
                    dfs_stack.pop();
                    prev_level--;
                }
                
            }
            
            if(quit_current==1){
                continue;
            }
        }
        String words_as_string="";
        for(String s: final_words){
            words_as_string+=s+"#";
        }
        words_as_string = words_as_string.substring(0, words_as_string.length()-1);
        return words_as_string;
        //System.out.print(curr + " ");
        
    }
}
