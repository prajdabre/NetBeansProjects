// Assignment 3: Message Passing Algorithm for Inference
// Team: 11305R013 Rahul Sharnagat
//		 113050037  Gaurish Chaudhari

package MpaAlgos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {
//		
		Data graph=new Data();
		//graph.inputGraph("graph");
		//graph.inputPotentials("potentials");
		graph.inputGraph(args[0]);
		graph.inputPotentials(args[1]);
		MPA algo=new MPA();
		algo.setData(graph);
		algo.triangulate();
//		algo.printGraph();
		algo.getCliqueTree();
		algo.printJTInfo();
		Inference infer =new Inference(algo.getJunction_tree(),graph);
		infer.computeMsgs();
		
		System.out.print("Enter nodes whose marginal probability table is required: ");
		BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		ArrayList<Integer> ip=new ArrayList<Integer>();
		try {
			String[] l=br.readLine().split(" ");
			for(int i=0;i<l.length;i++)
				ip.add(Integer.parseInt(l[i]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		infer.infer(new ArrayList<Integer>(ip));
		infer.findMaxMarginal();
//		infer.messagePassing(algo.getJunction_tree());
	}
	
}