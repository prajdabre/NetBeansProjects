/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import javax.swing.JFrame;

import edu.uci.ics.jung.visualization.decorators.*;
import org.apache.commons.collections15.Transformer;
/**
 *
 * @author raj
 */
public class Visualize_Grammar {

    /**
     * @param args the command line arguments
     */
    static int edgeCount = 0;
    private static DirectedSparseMultigraph<MyNode, MyLink> g;
    private static MyNode n1;
    private static MyNode n2;
    private static MyNode n3;
    private static MyNode n4;
    private static MyNode n5;

    public static void main(String[] args) {
        // TODO code application logic here
        g = new DirectedSparseMultigraph<MyNode, MyLink>();
// Create some MyNode objects to use as vertices
        n1 = new MyNode(1);
        n2 = new MyNode(2);
        n3 = new MyNode(3);
        n4 = new MyNode(4);
        n5 = new MyNode(5); // note n1-n5 declared elsewhere.
// Add some directed edges along with the vertices to the graph
        g.addEdge(new MyLink(2.0, 48), n1, n2, EdgeType.DIRECTED); // This method
        g.addEdge(new MyLink(2.0, 48), n2, n3, EdgeType.DIRECTED);
        g.addEdge(new MyLink(3.0, 192), n3, n5, EdgeType.DIRECTED);
        g.addEdge(new MyLink(2.0, 48), n5, n4, EdgeType.DIRECTED); // or we can use
        g.addEdge(new MyLink(2.0, 48), n4, n2); // In a directed graph the
        g.addEdge(new MyLink(2.0, 48), n3, n1); // first node is the source
        g.addEdge(new MyLink(10.0, 48), n2, n5);// and the second the destination


        Layout<Integer, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300, 300));
        BasicVisualizationServer<Integer, String> vv =
                new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(350, 350));
// Setup up a new vertex to paint transformer...
        Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {

            public Paint transform(Integer i) {
                return Color.GREEN;
            }
        };
// Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer =
                new Transformer<String, Stroke>() {

                    public Stroke transform(String s) {
                        return edgeStroke;
                    }
                };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
}

class MyNode {

    int id; // good coding practice would have this as private

    public MyNode(int id) {
        this.id = id;
    }

    public String toString() { // Always a good idea for debuging
        return "V" + id;
// JUNG2 makes good use of these.
    }
}

class MyLink {

    double capacity; // should be private
    double weight; // should be private for good practice
    int id;

    public MyLink(double weight, double capacity) {
        this.id = Visualize_Grammar.edgeCount++; // This is defined in the outer class.
        this.weight = weight;
        this.capacity = capacity;
    }

    public String toString() { // Always good for debugging
        return "E" + id;
    }
}