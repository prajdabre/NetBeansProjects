/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;
/*
 * SimpleGraphView.java
 *
 * Created on March 8, 2007, 7:49 PM
 *
 * Copyright March 8, 2007 Grotto Networking
 */

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Dr. Greg M. Bernstein
 */
public class SimpleGraphView2 {

    Graph<Integer, String> g;
    Graph<String, String> g1;

    /** Creates a new instance of SimpleGraphView */
    public SimpleGraphView2() {
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        // Note showing the use of a SparseGraph rather than a SparseMultigraph
        //g = new SparseGraph<Integer, String>();
        // Add some vertices. From above we defined these to be type Integer.
        g1 = new DirectedSparseGraph<String, String>();

        read_graph_from_file_and_update();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleGraphView2 sgv = new SimpleGraphView2(); // This builds the graph
        // Layout<V, E>, VisualizationComponent<V,E>
        Layout<String, String> layout = new CircleLayout<String, String>(sgv.g1);
        layout.setSize(new Dimension(1024, 768));
        VisualizationViewer<String, String> vv =
                new VisualizationViewer<String, String>(layout);
        vv.setPreferredSize(new Dimension(1024, 768));
// Show vertex and edge labels
        System.out.println(sgv.g1.getSuccessors("OF"));
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

    private void read_graph_from_file_and_update() {
        HashSet<String> hs = new HashSet<String>();

        try {
            int j = 1;
            BufferedReader br = new BufferedReader(new FileReader("expanded_rules.txt"));
            String line = "";
            this.g1.addVertex("end");
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.trim().length() == 0) {
                    break;
                }
                String components[] = line.replaceAll("[ ][ ]+", " ").split(" ");
                if (components[0].equalsIgnoreCase("NOMINAL") || components[0].equalsIgnoreCase("CARDINAL")
                        || components[0].equalsIgnoreCase("PRONOUN") || components[0].equalsIgnoreCase("ADJ")
                        || components[0].equalsIgnoreCase("ADVS") || components[0].equalsIgnoreCase("INT")
                        || components[0].equalsIgnoreCase("CONS") || components[0].equalsIgnoreCase("COMS")
                        || components[0].equalsIgnoreCase("AKYS") || components[0].equalsIgnoreCase("KRS")
                        || components[0].equalsIgnoreCase("PARTS") || components[0].equalsIgnoreCase("EOFCLS")) {
                    {
                        for (int i = 1; i < components.length; i++) {
                            hs.add(components[i]);
                        }
                        if (!g1.containsEdge("start" + components[1])) {
                            g1.addEdge("start" + components[1], "start", components[1]);
                        }
                        if (components.length == 2) {
                            if (!g1.containsEdge(components[1] + "end")) {
                                g1.addEdge(components[1] + "end", components[1], "end");
                                //System.out.println("New Edge");

                            }
                            continue;
                        }
                        if (components.length == 1) {
                            continue;
                        }
                        int i;
                        for (i = 1; i < components.length - 1; i++) {
                            if (!g1.containsEdge(components[i] + components[i + 1])) {
                                g1.addEdge(components[i] + components[i + 1], components[i], components[i + 1]);
                                //System.out.println("New Edge");

                            }
                        }
                        if (!g1.containsEdge(components[i] + "end")) {
                            g1.addEdge(components[i] + "end", components[i], "end");
                            //System.out.println("New Edge");

                        }
                        j++;
                        if (j == 5) {
                            //break;
                        }
                    }
                }
            }
            System.out.println(hs.size());
        } catch (Exception ex) {
            Logger.getLogger(SimpleGraphView2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
