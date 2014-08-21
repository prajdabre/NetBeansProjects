/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.commons.collections15.map.FastHashMap;

/**
 *
 * @author raj
 */
public class GenerateSurfaceForms {

    /**
     * @param args the command line arguments
     */
    HashMap<String, ArrayList<String>> cats_to_morphemes_stem = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> morphemes_to_cats_stem = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> cats_to_morphemes_root = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> morphemes_to_cats_root = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> roots_to_stems = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> stems_to_roots = new HashMap<String, ArrayList<String>>();
    Graph<String, String> grammar_as_graph = new DirectedSparseGraph<String, String>();
    Graph<String, String> words_as_graph = new DirectedSparseGraph<String, String>();
    HashSet<String> final_words = new HashSet<String>();
    int count = 0;

    public void load_data_from_serialized_objects() {
        try {
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

        System.out.println(grammar_as_graph.getEdgeCount());
        //System.out.println(grammar_as_graph.getSuccessors("SSY1"));

    }

    public void visualize_graph() {
        SimpleGraphView2 sgv = new SimpleGraphView2(); // This builds the graph
        // Layout<V, E>, VisualizationComponent<V,E>
        Layout<String, String> layout = new CircleLayout<String, String>(grammar_as_graph);
        layout.setSize(new Dimension(1024, 768));
        VisualizationViewer<String, String> vv =
                new VisualizationViewer<String, String>(layout);
        vv.setPreferredSize(new Dimension(1024, 768));
// Show vertex and edge labels
        //System.out.println(sgv.g1.getSuccessors("OF"));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
// Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);
        JFrame frame = new JFrame("Interactive Graph View 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    public void generate_surface_forms() {
        String curr = "DF";
        System.out.print(curr + " ");
        ArrayList<String> outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(curr));
        String word = "";

        while (!outEdges.isEmpty()) {

            word += cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", "")).get(0);
            curr = outEdges.get(0);

            System.out.print(curr + " ");
            outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(curr));
        }

        word += cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", "")).get(0);
        System.out.println(word);
    }

    public void generate_surface_forms(String root, int limit) {
        String curr = "DF";
        System.out.print(curr + " ");
        ArrayList<String> outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(curr));
        String word = "";

        while (!outEdges.isEmpty()) {

            word += cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", "")).get(1);
            curr = outEdges.get(0);

            System.out.print(curr + " ");
            outEdges = new ArrayList<String>(grammar_as_graph.getSuccessors(curr));
        }

        word += cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", "")).get(0);
        System.out.println(word);
    }

    public void generate_full_words_graph() {

        for (String s : grammar_as_graph.getVertices()) {
            System.out.println("Creating vertices for Category: " + s);
            for (String node : cats_to_morphemes_stem.get(s.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                if (!words_as_graph.containsVertex(node + "_" + s)) {
                    words_as_graph.addVertex(node + "_" + s);
                }
            }
        }

        for (String curr : grammar_as_graph.getVertices()) {
            System.out.println("Creating edges for Category: " + curr);
            ArrayList<String> preds = new ArrayList<String>();
            if (grammar_as_graph.getPredecessorCount(curr) > 0) {
                preds = new ArrayList<String>(grammar_as_graph.getPredecessors(curr));
            }
            ArrayList<String> succs = new ArrayList<String>();
            if (grammar_as_graph.getSuccessorCount(curr) > 0) {
                succs = new ArrayList<String>(grammar_as_graph.getSuccessors(curr));
            }
            System.out.println("Saishou wa predecessors");
            if (preds.size() > 0) {
                for (String pred : preds) {
                    for (String pred_node : cats_to_morphemes_stem.get(pred.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                        for (String curr_node : cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                            if (!words_as_graph.containsEdge("e_" + pred_node + "_" + pred + "_" + curr_node + "_" + curr)) {
                                words_as_graph.addEdge("e_" + pred_node + "_" + pred + "_" + curr_node + "_" + curr, pred_node + "_" + pred, curr_node + "_" + curr);
                            }
                        }

                    }
                }
            }
            System.out.println("Owatta");
            System.out.println("Sugi wa successors");
            if (succs.size() > 0) {
                for (String succ : succs) {
                    for (String succ_node : cats_to_morphemes_stem.get(succ.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                        for (String curr_node : cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                            if (!words_as_graph.containsEdge("e_" + curr_node + "_" + curr + "_" + succ_node + "_" + succ)) {
                                words_as_graph.addEdge("e_" + curr_node + "_" + curr + "_" + succ_node + "_" + succ, curr_node + "_" + curr, succ_node + "_" + succ);
                            }
                        }

                    }
                }
            }
            System.out.println("Owatta");

        }

    }

    public void generate_full_words_graph_ambiguous() {

        for (String s : grammar_as_graph.getVertices()) {
            System.out.println("Creating vertices for Category: " + s);
            for (String node : cats_to_morphemes_stem.get(s.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                if (!words_as_graph.containsVertex(node)) {
                    words_as_graph.addVertex(node);
                }
            }
        }

        for (String curr : grammar_as_graph.getVertices()) {
            System.out.println("Creating edges for Category: " + curr);
            ArrayList<String> preds = new ArrayList<String>();
            if (grammar_as_graph.getPredecessorCount(curr) > 0) {
                preds = new ArrayList<String>(grammar_as_graph.getPredecessors(curr));
            }
            ArrayList<String> succs = new ArrayList<String>();
            if (grammar_as_graph.getSuccessorCount(curr) > 0) {
                succs = new ArrayList<String>(grammar_as_graph.getSuccessors(curr));
            }
            System.out.println("Saishou wa predecessors");
            if (preds.size() > 0) {
                for (String pred : preds) {
                    for (String pred_node : cats_to_morphemes_stem.get(pred.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                        for (String curr_node : cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                            if (!words_as_graph.containsEdge("e_" + pred_node + "_" + curr_node)) {
                                words_as_graph.addEdge("e_" + pred_node + "_" + curr_node, pred_node, curr_node);
                            }
                        }

                    }
                }
            }
            System.out.println("Owatta");
            System.out.println("Sugi wa successors");
            if (succs.size() > 0) {
                for (String succ : succs) {
                    for (String succ_node : cats_to_morphemes_stem.get(succ.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                        for (String curr_node : cats_to_morphemes_stem.get(curr.replaceAll("SSY1", "SSY").replaceAll("PP_OPN1", "PP_OPN").replaceAll("-END", ""))) {
                            if (!words_as_graph.containsEdge("e_" + curr_node + "_" + succ_node)) {
                                words_as_graph.addEdge("e_" + curr_node + "_" + succ_node, curr_node, succ_node);
                            }
                        }

                    }
                }
            }
            System.out.println("Owatta");

        }

    }

    public void detect_loops() {
        System.out.println("Detecting loops\nGetting begin nodes");
        Set<String> beginning_vertices = new HashSet<String>();
        for (String s : grammar_as_graph.getVertices()) {
            if (grammar_as_graph.getPredecessorCount(s) == 0) {
                beginning_vertices.add(s);
            }
        }
        System.out.println("Detecting loops");
        for (String s : beginning_vertices) {
            System.out.println("For " + s);
            Set<String> already_visited = new HashSet<String>();
            already_visited.add(s);
            Stack<String> st = new Stack<String>();
            st.addAll(grammar_as_graph.getSuccessors(s));
            while (!st.empty()) {
                System.out.println(st);
                System.out.println(already_visited);
                String current = st.pop();
                System.out.println(current);
                if (already_visited.contains(current)) {
                    System.out.println("Loop");
                    System.exit(1);
                }
                already_visited.add(current);
                if ((grammar_as_graph.getSuccessorCount(current) > 0)) {
                    st.addAll(grammar_as_graph.getSuccessors(current));
                } else {
                    System.out.println("End");
                    already_visited.remove(current);
                }
            }
        }
    }

    public void check_loops_simple() {
        for (String s : grammar_as_graph.getVertices()) {

            HashSet<String> preds = new HashSet<String>(grammar_as_graph.getPredecessors(s));
            HashSet<String> succs = new HashSet<String>(grammar_as_graph.getSuccessors(s));
            //System.out.println(preds);
            //System.out.println(succs);
            preds.retainAll(succs);
            if (preds.size() > 0) {
                System.out.println("Loop for: " + s + " " + preds);
            }
        }
    }

    public void serialize_word_graph() {
        try {
            FileOutputStream fileOut = null;

            System.out.println("Serializing");

            fileOut = new FileOutputStream("words_as_graph" + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(words_as_graph);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + "words_as_graph" + ".ser");


            System.out.println("Serialization complete");
        } catch (Exception ex) {
            Logger.getLogger(GenerateSurfaceForms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generate_all_surfaces() {
        ArrayList<String> starters = new ArrayList<String>();
        for (String s : grammar_as_graph.getVertices()) {
            if (grammar_as_graph.getPredecessorCount(s) == 0) {
                starters.add(s);
            }
        }
        ArrayList<String> curr_word = new ArrayList<String>();
        for (String s : starters) {
            expand_current_rule(s, curr_word);

        }
    }

    public void expand_current_rule(String symbol, ArrayList<String> curr_word) {

        for (String word : cats_to_morphemes_stem.get(symbol.split("-")[0])) {
            curr_word.add(word);
            curr_word.add("<" + symbol + ">");
            if (grammar_as_graph.getSuccessorCount(symbol) == 0) {
                String fin_word = "";
                for (int k = 0; k < curr_word.size(); k++) {
                    fin_word += curr_word.get(k);
                }
                //
                final_words.add(fin_word);
                count++;
                if(count%1000==0){
                    //System.out.println(count+" Surfaces done");
                    //System.out.println(fin_word);
                }
                curr_word.remove(curr_word.size() - 1);
                curr_word.remove(curr_word.size() - 1);
            } else {
                for (String nextsymbol : grammar_as_graph.getSuccessors(symbol)) {
                    expand_current_rule(nextsymbol, curr_word);
                }
                curr_word.remove(curr_word.size() - 1);
                curr_word.remove(curr_word.size() - 1);
            }
        }
    }
    
    public void write_all_surfaces(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Files.all_surfaces)));
            for(String s: final_words){
                bw.write(s+"\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateSurfaceForms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        GenerateSurfaceForms gsf = new GenerateSurfaceForms();
        gsf.load_data_from_serialized_objects();
        //gsf.visualize_graph();
        gsf.generate_all_surfaces();
        gsf.write_all_surfaces();
        //gsf.generate_full_words_graph();
        //gsf.detect_loops();
        //System.out.println(gsf.grammar_as_graph.getSuccessors("V20"));
        //gsf.generate_surface_forms();
        //gsf.generate_full_words_graph();
        //gsf.serialize_word_graph();
        //gsf.check_loops_simple();

    }
}
