package mpaananth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExactInference {
	static UndirectedGraph ug;
	static JunctionTree jtree;

	public static void main(String[] args) throws IOException {
		File config = new File(args[0]);
		File potentials = new File(args[1]);
		ug = null;
		try {
			ug = new UndirectedGraph(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ug.triangulate();
		System.out.println("Edges : " + ug.edges);
		jtree = new JunctionTree(ug.getGraph(), ug.getMaxValues(), potentials);
		ArrayList<Clique> cliques = jtree.getCliques();
		int[][] cl = jtree.getJunctionTree();
		for (int i = 0; i < cliques.size(); i++) {
			System.out.println(cliques.get(i).getNodes());
		}
		for (int i = 0; i < cl[0].length; i++) {
			for (int j = 0; j < cl[0].length; j++) {
				System.out.print(cl[i][j] + "  ");
			}
			System.out.println("");
		}
		
	}
}
