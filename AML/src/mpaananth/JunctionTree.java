package mpaananth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

class Clique {
	private ArrayList<Integer> nodes;
	private ArrayList<Clique> adjacent;
	int cliqueID;
	private HashMap<ArrayList<Integer>, Double> potential;
	HashMap<Integer, HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>> message;
	HashMap<Integer, HashMap<ArrayList<Integer>,Double>> MAPmessage;
	Clique() {
		nodes = new ArrayList<Integer>();
		adjacent = new ArrayList<Clique>();
		potential = new HashMap<ArrayList<Integer>, Double>();
		message = new HashMap<Integer, HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>>();
		MAPmessage = new HashMap<Integer, HashMap<ArrayList<Integer>,Double>>();
	}

	ArrayList<Integer> getNodes() {
		return nodes;
	}

	HashMap<ArrayList<Integer>, Double> getPotential() {
		return potential;
	}

	void calcPotential(
			HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> potentials,
			HashMap<Integer, Integer> maxValues) {
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> temp = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		temp.putAll(potentials);
		for (ArrayList<Integer> key : potentials.keySet()) {
			if (!nodes.containsAll(key)) {
				temp.remove(key);
			}
		}
		ArrayList<ArrayList<Integer>> seq = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < nodes.size(); i++) {
			values.add(maxValues.get(nodes.get(i)));
		}
		seq.addAll(getAllValues(values));
		// System.out.println(seq);
		for (int i = 0; i < seq.size(); i++) {
			double ptnl = 1.0;
			// System.out.println(nodes);
			for (ArrayList<Integer> key : temp.keySet()) {
				// System.out.println(key);
				ArrayList<Integer> valkey = new ArrayList<Integer>();
				for (int k = 0; k < key.size(); k++) {
					valkey.add(seq.get(i).get(nodes.indexOf(key.get(k))));
				}
				// System.out.println(valkey);
				ptnl *= potentials.get(key).get(valkey);
			}
			potential.put(seq.get(i), ptnl);
		}
	}

	private ArrayList<Integer> getNextSeq(ArrayList<Integer> init,
			ArrayList<Integer> maxValues) {
		int i = init.size() - 1;
		while (init.get(i) == (maxValues.get(i) - 1)) {
			init.set(i, 0);
			i--;
		}
		init.set(i, init.get(i) + 1);
		return init;
	}

	private ArrayList<ArrayList<Integer>> getAllValues(
			ArrayList<Integer> maxValues) {
		ArrayList<ArrayList<Integer>> seq = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> last = new ArrayList<Integer>();
		ArrayList<Integer> init = new ArrayList<Integer>();
		for (int i = 0; i < maxValues.size(); i++) {
			init.add(0);
		}
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.addAll(init);
		seq.add(temp);
		for (int i = 0; i < maxValues.size(); i++) {
			last.add(maxValues.get(i) - 1);
		}
		while (!init.equals(last)) {
			temp = new ArrayList<Integer>();
			init = getNextSeq(init, maxValues);
			temp.addAll(init);
			seq.add(temp);
		}
		return seq;
	}

	ArrayList<Clique> getAdjacent() {
		return adjacent;
	}

	void addNode(int node) {
		nodes.add(node);
	}

	void addAdjacent(Clique cl) {
		adjacent.add(cl);
	}

	void printClique() {
		System.out.println(nodes);
	}
}

public class JunctionTree {
	private ArrayList<Clique> cliques;
	private int[][] jtree;
	private int[][] temp;
	int edges;
	private ArrayList<ArrayList<Integer>> graph;
	HashMap<Integer, Integer> maxValues;
	HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> potentials;
	ArrayList<Double> cliquePotentials;

	int chooseRoot() {
		return (int) (Math.random() % cliques.size());
	}
	void setPotentials(HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> potentials){
		this.potentials=potentials;
	}
	private ArrayList<Integer> getNextSeq(ArrayList<Integer> init,
			ArrayList<Integer> maxValues) {
		int i = init.size() - 1;
		while (init.get(i) == (maxValues.get(i) - 1)) {
			init.set(i, 0);
			i--;
		}
		init.set(i, init.get(i) + 1);
		return init;
	}

	private ArrayList<ArrayList<Integer>> getAllValues(
			ArrayList<Integer> maxValues) {
		ArrayList<ArrayList<Integer>> seq = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> last = new ArrayList<Integer>();
		ArrayList<Integer> init = new ArrayList<Integer>();
		for (int i = 0; i < maxValues.size(); i++) {
			init.add(0);
		}
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.addAll(init);
		seq.add(temp);
		for (int i = 0; i < maxValues.size(); i++) {
			last.add(maxValues.get(i) - 1);
		}
		while (!init.equals(last)) {
			temp = new ArrayList<Integer>();
			init = getNextSeq(init, maxValues);
			temp.addAll(init);
			seq.add(temp);
		}
		return seq;
	}

	public void doMessagePassing() {
		int root = chooseRoot();
		collect(root, root);
		distribute(root, root);
	}

	ArrayList<Integer> getNeighbors(int node) {
		ArrayList<Integer> nbrs = new ArrayList<Integer>();
		for (int i = 0; i < cliques.size(); i++) {
			if (jtree[node][i] != -1) {
				nbrs.add(i);
			}
		}
		return nbrs;
	}

	/*
	 * private boolean isEqualinPlaces(ArrayList<Integer> a1, ArrayList<Integer>
	 * a2, ArrayList<Integer> ind1, ArrayList<Integer> ind2) { if (ind1.size()
	 * != ind2.size()) return false; for (int i = 0; i < ind1.size(); i++) { if
	 * (a1.get(ind1.get(i)) != a2.get(ind2.get(i))) return false; } return true;
	 * }
	 */

	HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> multiplyPotential(
			HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> m1,
			HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> m2) {
		HashMap<ArrayList<Integer>, Double> temp = new HashMap<ArrayList<Integer>, Double>();
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> prod = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		HashMap<ArrayList<Integer>, Double> msg1 = m1.get(m1.keySet()
				.iterator().next());
		HashMap<ArrayList<Integer>, Double> msg2 = m2.get(m2.keySet()
				.iterator().next());
		Set<Integer> node = new HashSet<Integer>();
		node.addAll(m1.keySet().iterator().next());
		node.addAll(m2.keySet().iterator().next());
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		nodes.addAll(node);
		ArrayList<ArrayList<Integer>> seq = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < nodes.size(); i++) {
			values.add(maxValues.get(nodes.get(i)));
		}
		seq.addAll(getAllValues(values));
		// System.out.println(seq);
		for (int i = 0; i < seq.size(); i++) {
			ArrayList<Integer> ind1 = new ArrayList<Integer>();
			ArrayList<Integer> ind2 = new ArrayList<Integer>();
			for (int j = 0; j < m1.keySet().iterator().next().size(); j++) {
				ind1.add(seq.get(i).get(
						nodes.indexOf(m1.keySet().iterator().next().get(j))));
			}
			for (int j = 0; j < m2.keySet().iterator().next().size(); j++) {
				ind2.add(seq.get(i).get(
						nodes.indexOf(m2.keySet().iterator().next().get(j))));
			}
			temp.put(seq.get(i), msg1.get(ind1) * msg2.get(ind2));
		}
		prod.put(nodes, temp);
		return prod;
	}

	HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> getMarginal(
			ArrayList<Integer> marginalNodes,
			HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg) {
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> marginal = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		ArrayList<Integer> values = new ArrayList<Integer>();
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		nodes.addAll(msg.keySet().iterator().next());
		nodes.removeAll(marginalNodes);
		marginalNodes.clear();
		marginalNodes.addAll(nodes);
		for (int i = 0; i < marginalNodes.size(); i++)
			values.add(maxValues.get(marginalNodes.get(i)));
		ArrayList<ArrayList<Integer>> seq = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> ind = new ArrayList<Integer>();
		for (int i = 0; i < marginalNodes.size(); i++) {
			ind.add(msg.keySet().iterator().next()
					.indexOf(marginalNodes.get(i)));
		}
		seq = getAllValues(values);
		HashMap<ArrayList<Integer>, Double> potentls = msg.get(msg.keySet()
				.iterator().next());
		HashMap<ArrayList<Integer>, Double> marg = new HashMap<ArrayList<Integer>, Double>();
		for (int j = 0; j < seq.size(); j++) {
			marg.put(seq.get(j), 0.0);
		}
		Iterator<ArrayList<Integer>> it = potentls.keySet().iterator();
		while (it.hasNext()) {
			ArrayList<Integer> vals = it.next();
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for (int i = 0; i < ind.size(); i++)
				temp.add(vals.get(ind.get(i)));
			Double d = marg.get(temp);
			d += potentls.get(vals);
			marg.put(temp, d);
		}
		marginal.put(marginalNodes, marg);
		return marginal;
	}

	void passMessage(int from, int to) {
		Clique fro = cliques.get(from);
		Clique tow = cliques.get(to);
		ArrayList<Integer> toSum = new ArrayList<Integer>();
		toSum.addAll(fro.getNodes());
		toSum.removeAll(tow.getNodes());
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> m1 = fro.message
				.get(tow.cliqueID);
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		msg.putAll(m1);
		for (int i = 0; i < fro.getAdjacent().size(); i++) {
			if (fro.getAdjacent().get(i).cliqueID != tow.cliqueID) {
				msg = multiplyPotential(msg,
						fro.getAdjacent().get(i).message.get(from));
			}
		}
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg2 = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		msg2.put(fro.getNodes(), fro.getPotential());
		msg = multiplyPotential(msg, msg2);
		msg = getMarginal(toSum, msg);
		fro.message.put(to, msg);
	}

	HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> getBeliefTable(
			int cliqueID) {
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		msg.put(cliques.get(cliqueID).getNodes(), cliques.get(cliqueID)
				.getPotential());
		for (int i = 0; i < cliques.get(cliqueID).getAdjacent().size(); i++) {
			msg = multiplyPotential(msg, cliques.get(cliqueID).getAdjacent()
					.get(i).message.get(cliqueID));
		}
		return msg;
	}

	HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> normalize(
			HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg) {
		HashMap<ArrayList<Integer>, Double> temp = new HashMap<ArrayList<Integer>, Double>();
		double Z = 0.0;
		temp.putAll(msg.get(msg.keySet().iterator().next()));
		for (ArrayList<Integer> key : temp.keySet()) {
			Z += temp.get(key);
		}
		//System.out.println(Z);
		for (ArrayList<Integer> key : temp.keySet()) {
			double pt = temp.get(key);
			temp.put(key, pt / Z);
		}
		msg.put(msg.keySet().iterator().next(), temp);
		return msg;
	}

	public HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> marginalQuery(
			ArrayList<Integer> nodes) {
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> result = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		boolean flag = false;
		Clique cl = null;
		for (int i = 0; i < cliques.size(); i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.addAll(nodes);
			temp.removeAll(cliques.get(i).getNodes());
			if (temp.size() == 0) {
				flag = true;
				cl = cliques.get(i);
			}
		}
		if (!flag) {
			for (int i = 0; i < cliques.size(); i++) {
				if (nodes.size() > 0) {
					ArrayList<Integer> nds = new ArrayList<Integer>();
					nds.addAll(cliques.get(i).getNodes());
					int sz = nds.size();
					nds.removeAll(nodes);
					if (nds.size() < sz) {
						if (result.size() == 0) {
							result = getMarginal(nds, getBeliefTable(i));
							nodes.removeAll(result.keySet().iterator().next());
						} else {
							result = multiplyPotential(
									getMarginal(nds, getBeliefTable(i)), result);
							nodes.removeAll(result.keySet().iterator().next());
						}
					}
				}
			}
		} else {
			ArrayList<Integer> nds = new ArrayList<Integer>();
			nds.addAll(cl.getNodes());
			nds.removeAll(nodes);
			result = getMarginal(nds, getBeliefTable(cl.cliqueID));
		}
		// return result;
		return normalize(result);
	}

	void updateBelief(int node) {
		Clique cl = cliques.get(node);
		HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		msg.put(cl.getNodes(), cl.getPotential());
		for (int i = 0; i < cl.getAdjacent().size(); i++) {
			msg = multiplyPotential(msg,
					cl.getAdjacent().get(i).message.get(cl.cliqueID));
		}
	}

	void collect(int one, int two) {
		ArrayList<Integer> nbrs = getNeighbors(two);
		for (int i = 0; i < nbrs.size(); i++) {
			if (nbrs.get(i) != one) {
				collect(two, nbrs.get(i));
				passMessage(nbrs.get(i), two);
			}
		}
	}

	void distribute(int one, int two) {
		ArrayList<Integer> nbrs = getNeighbors(two);
		for (int i = 0; i < nbrs.size(); i++) {
			if (nbrs.get(i) != one) {
				passMessage(two, nbrs.get(i));
				distribute(two, nbrs.get(i));
			}
		}
	}
        public JunctionTree(){
            
        }
	public JunctionTree(ArrayList<ArrayList<Integer>> graph,
			HashMap<Integer, Integer> maxValues, File potential)
			throws IOException {
		edges = 0;
		this.graph = new ArrayList<ArrayList<Integer>>();
		this.maxValues = new HashMap<Integer, Integer>();
		this.maxValues.putAll(maxValues);
		potentials = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
		this.graph = graph;
		cliquePotentials = new ArrayList<Double>();
		cliques = new ArrayList<Clique>();
		generateCliques();
		jtree = new int[cliques.size()][cliques.size()];
		generateFullGraph();
		generateMaxSpanningTree();
		for (int i = 0; i < cliques.size(); i++) {
			for (int j = 0; j < cliques.size(); j++) {
				if (jtree[i][j] != -1) {
					cliques.get(i).addAdjacent(cliques.get(j));
				}
			}
		}
		initializeMessages();
		readPotentials(potential);
		calcCliquePotentials();
		// calcNormalizer();
		doMessagePassing();
	}

        public void makeJunctionTree(ArrayList<ArrayList<Integer>> graph,
			HashMap<Integer, Integer> maxValues, HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> potential)
			{
		edges = 0;
		this.graph = new ArrayList<ArrayList<Integer>>();
		this.maxValues = new HashMap<Integer, Integer>();
		this.maxValues.putAll(maxValues);
		potentials = potential;
		this.graph = graph;
		cliquePotentials = new ArrayList<Double>();
		cliques = new ArrayList<Clique>();
		generateCliques();
		jtree = new int[cliques.size()][cliques.size()];
		generateFullGraph();
		generateMaxSpanningTree();
		for (int i = 0; i < cliques.size(); i++) {
			for (int j = 0; j < cliques.size(); j++) {
				if (jtree[i][j] != -1) {
					cliques.get(i).addAdjacent(cliques.get(j));
				}
			}
		}
		initializeMessages();
		
		calcCliquePotentials();
		// calcNormalizer();
		doMessagePassing();
	}
	// void calcNormalizer() {
	// HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg =
	// new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
	// Set<Integer> nds = new HashSet<Integer>();
	// ArrayList<Integer> nodes = new ArrayList<Integer>();
	// for (int i = 0; i < cliques.size(); i++) {
	// nds.addAll(cliques.get(i).getNodes());
	// if (msg.size() == 0) {
	// msg = getBeliefTable(i);
	// }
	// msg = multiplyPotential(msg, getBeliefTable(i));
	// }
	// nodes.addAll(nds);
	// msg = getMarginal(nodes, msg);
	// Z = msg.get(msg.keySet().iterator().next()).get(
	// msg.get(msg.keySet().iterator().next()).keySet().iterator()
	// .next());
	// }

	void readPotentials(File potential) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(potential));
		String line = "";
		String[] n;
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		HashMap<ArrayList<Integer>, Double> hm = new HashMap<ArrayList<Integer>, Double>();
		while ((line = br.readLine()) != null) {
			if (line.startsWith("#")) {
				n = line.split("#")[1].trim().split("[ ]+");
				if (nodes.size() > 0) {
					potentials.put(nodes, hm);
					hm = new HashMap<ArrayList<Integer>, Double>();
				}
				nodes = new ArrayList<Integer>();
				for (int i = 0; i < n.length; i++) {
					nodes.add(Integer.parseInt(n[i]));
				}
			} else {
				ArrayList<Integer> lst = new ArrayList<Integer>();
				String[] vals = line.split("[ ]+");
				for (int i = 0; i < vals.length - 1; i++) {
					lst.add(Integer.parseInt(vals[i]));
				}
				hm.put(lst, Double.parseDouble(vals[vals.length - 1]));
			}
		}
		potentials.put(nodes, hm);
		br.close();
	}

	HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> getPotentials() {
		return potentials;
	}

	private void generateMaxSpanningTree() {
		temp = new int[cliques.size()][cliques.size()];
		for (int i = 0; i < cliques.size(); i++) {
			for (int j = 0; j < cliques.size(); j++) {
				temp[i][j] = -1;
			}
		}
		depthFirstSearch(jtree, 0);
	}

	void depthFirstSearch(int[][] gr, int source) {
		boolean[] visited = new boolean[gr[0].length];
		for (int i = 0; i < visited.length; i++)
			visited[i] = false;
		for (int i = 0; i < gr[0].length; i++)
			if (!visited[i])
				depthFirstSearch(gr, i, source, visited);
		jtree = temp;
	}

	void depthFirstSearch(int[][] gr, int source, int dest, boolean[] visited) {

		if (!visited[source]) {
			visited[source] = true;
			if (source != dest) {
				temp[source][dest] = temp[dest][source] = jtree[source][dest];
			}
			int[] adj = gr[source];
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			ValueComparator bvc = new ValueComparator(hm);
			for (int i = 0; i < adj.length; i++)
				hm.put(i, adj[i]);
			TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>(bvc);
			tm.putAll(hm);
			Integer[] keys = tm.keySet().toArray(new Integer[hm.size()]);
			for (int i = tm.size() - 1; i >= 0; i--) {
				if (keys[i] != -1)
					depthFirstSearch(gr, keys[i], source, visited);
			}
		}
	}

	private void generateFullGraph() {
		for (int i = 0; i < cliques.size(); i++)
			for (int j = 0; j < cliques.size(); j++)
				jtree[i][j] = -1;
		for (int i = 0; i < cliques.size() - 1; i++) {
			for (int j = i + 1; j < cliques.size(); j++) {
				int com = getIntersection(cliques.get(i), cliques.get(j));
				jtree[i][j] = jtree[j][i] = com;
			}
		}
	}

	private int getIntersection(Clique a, Clique b) {
		Set<Integer> s1 = new HashSet<Integer>();
		Set<Integer> s2 = new HashSet<Integer>();
		s1.addAll(a.getNodes());
		s2.addAll(b.getNodes());
		int bef = s1.size();
		s1.removeAll(s2);
		if (bef == s1.size())
			return -1;
		return bef - s1.size();
	}

	private void generateCliques() {
		ArrayList<Integer> seen = new ArrayList<Integer>();
		UndirectedGraph ug = new UndirectedGraph();
		ug.setGraph(graph);
		Set<Integer> rem = new HashSet<Integer>();
		while (rem.size() < graph.size()) {
			Clique clq = new Clique();
			int next = ug.getNodeToBeDeleted(graph, seen);
			clq.addNode(next);
			rem.add(next);
			seen.add(next);
			ArrayList<Integer> adj = graph.get(next);
			for (int j = 0; j < adj.size(); j++) {
				if (!seen.contains(adj.get(j))) {
					rem.add(adj.get(j));
					clq.addNode(adj.get(j));
				}
			}
			cliques.add(clq);
		}
		for (int i = 0; i < cliques.size() - 1; i++) {
			for (int j = i + 1; j < cliques.size(); j++) {
				if (cliques.get(i).getNodes()
						.containsAll(cliques.get(j).getNodes())) {
					cliques.remove(j);
				}
				if (cliques.get(j).getNodes()
						.containsAll(cliques.get(i).getNodes())) {
					cliques.remove(i);
				}
			}
		}
		for (int i = 0; i < cliques.size(); i++) {
			cliques.get(i).cliqueID = i;
		}
	}

	ArrayList<Clique> getCliques() {
		return cliques;
	}

	int[][] getJunctionTree() {
		return jtree;
	}

	void calcCliquePotentials() {
		for (int i = 0; i < cliques.size(); i++) {
			cliques.get(i).calcPotential(potentials, maxValues);
		}
	}

	void initializeMessages() {
		for (int i = 0; i < cliques.size(); i++) {
			for (int j = 0; j < cliques.get(i).getAdjacent().size(); j++) {
				ArrayList<Integer> seperator = new ArrayList<Integer>();
				seperator.addAll(cliques.get(i).getNodes());
				seperator.retainAll(cliques.get(i).getAdjacent().get(j)
						.getNodes());
				ArrayList<Integer> values = new ArrayList<Integer>();
				for (int k = 0; k < seperator.size(); k++) {
					values.add(maxValues.get(seperator.get(k)));
				}
				ArrayList<ArrayList<Integer>> allValues = new ArrayList<ArrayList<Integer>>();
				allValues = getAllValues(values);
				HashMap<ArrayList<Integer>, Double> msg = new HashMap<ArrayList<Integer>, Double>();
				for (int k = 0; k < allValues.size(); k++) {
					msg.put(allValues.get(k), 1.0);
				}
				HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> nodeValues = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
				nodeValues.put(seperator, msg);
				cliques.get(i).message.put(
						cliques.get(i).getAdjacent().get(j).cliqueID,
						nodeValues);
			}
		}
	}
}
