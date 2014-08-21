package mpaananth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class ValueComparator implements Comparator<Integer> {

	Map<Integer, Integer> base;

	public ValueComparator(Map<Integer, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(Integer a, Integer b) {
		if (base.get(a) <= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}

public class UndirectedGraph {
	ArrayList<ArrayList<Integer>> graph;
	ArrayList<Integer> nodeLabels;
	HashMap<Integer, Integer> maxValues;
	int edges;

	void deleteNode(int node, ArrayList<Integer> seen) {
		ArrayList<Integer> al = graph.get(node);
		for (int i = 0; i < al.size() - 1; i++) {
			for (int j = i + 1; j < al.size(); j++) {
				if (!graph.get(al.get(i)).contains(al.get(j))
						&& !seen.contains(al.get(i))
						&& !seen.contains(al.get(j))) {
					ArrayList<Integer> nd = graph.get(al.get(i));
					nd.add(al.get(j));
					edges++;
					graph.set(al.get(i), nd);
					nd = new ArrayList<Integer>();
					nd = graph.get(al.get(j));
					nd.add(al.get(i));
					graph.set(al.get(j), nd);
				}
			}
		}
	}

	public UndirectedGraph() {
		edges = 0;
		graph = new ArrayList<ArrayList<Integer>>();
		nodeLabels = new ArrayList<Integer>();
		maxValues = new HashMap<Integer, Integer>();
	}

	UndirectedGraph(File config) throws IOException {
		edges = 0;
		graph = new ArrayList<ArrayList<Integer>>();
		nodeLabels = new ArrayList<Integer>();
		maxValues = new HashMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(config));
		int nodes = Integer.parseInt(br.readLine().trim());
		int edges = Integer.parseInt(br.readLine().trim());
		for (int i = 0; i < nodes; i++) {
			String s=br.readLine();
			nodeLabels.add(Integer
					.parseInt(s.trim().split("[ ]+")[0]));
			maxValues.put(
					Integer.parseInt(s.trim().split("[ ]+")[0]),
					Integer.parseInt(s.trim().split("[ ]+")[1]));
		}
		// for (int i = 0; i < nodes; i++) {
		// ArrayList<Integer> al = new ArrayList<Integer>();
		// graph.add(al);
		// }
		int[][] temp = new int[nodes][nodes];
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < nodes; j++) {
				temp[i][j] = -1;
			}
		}
		for (int i = 0; i < edges; i++) {
			String edge = br.readLine().trim();
			int a = Integer.parseInt(edge.split("[ ]+")[0]);
			int b = Integer.parseInt(edge.split("[ ]+")[1]);
			temp[a][b] = 1;
			temp[b][a] = 1;
		}
		for (int i = 0; i < nodes; i++) {
			ArrayList<Integer> al = new ArrayList<Integer>();
			for (int j = 0; j < nodes; j++) {
				if (temp[i][j] != -1) {
					al.add(j);
				}
			}
			graph.add(al);
		}
		br.close();
	}
	public void setMaxValues(int[] maxvals){
		HashMap<Integer, Integer> mv=new HashMap<Integer, Integer>();
		for(int i=0;i<maxvals.length;i++){
			mv.put(i, maxvals[i]);
		}
		this.maxValues.clear();
		this.maxValues.putAll(mv);
	}
	public HashMap<Integer, Integer> getMaxValues() {
		return maxValues;
	}
	public void setGraphMatrix(int[][] graph,Integer vars){
		ArrayList<ArrayList<Integer>> gr=new ArrayList<ArrayList<Integer>>();
		for(int i1=0;i1<vars;i1++){
                    gr.add(new ArrayList<Integer>());
                }
		for(int i=0;i<vars;i++){
			for(int j=0;j<vars;j++){
				if(graph[i][j]!=0){
					gr.get(i).add(j);
				}
			}
		}
		this.graph=gr;
	}
	void setGraph(ArrayList<ArrayList<Integer>> graph) {
		this.graph = graph;
	}

	int getExtraEdges(int node, ArrayList<Integer> seen) {
		ArrayList<Integer> al = graph.get(node);
		int count = 0;
		for (int i = 0; i < al.size() - 1; i++) {
			for (int j = i + 1; j < al.size(); j++) {
				if (!graph.get(al.get(i)).contains(al.get(j))
						&& !seen.contains(al.get(i))
						&& !seen.contains(al.get(j))) {
					count++;
				}
			}
		}
		return count;
	}

	int getNodeToBeDeleted(ArrayList<ArrayList<Integer>> graph,
			ArrayList<Integer> seen) {
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		ValueComparator bvc = new ValueComparator(hm);
		TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>(bvc);
		for (int i = 0; i < graph.size(); i++) {
			if (!seen.contains(i)) {
				hm.put(i, getExtraEdges(i, seen));
			}
		}
		tm.putAll(hm);
		return tm.firstEntry().getKey();
	}

	public ArrayList<ArrayList<Integer>> getGraph() {
		return graph;
	}

	public void triangulate() {
		ArrayList<Integer> seen = new ArrayList<Integer>();
		for (int i = 0; i < graph.size(); i++) {
			int node = getNodeToBeDeleted(graph, seen);
			seen.add(node);
			deleteNode(node, seen);
		}
	}

	void printGraph() {
		for (int i = 0; i < graph.size(); i++) {
			ArrayList<Integer> al = graph.get(i);
			for (int j = 0; j < al.size(); j++) {
				System.out.println(i + " " + al.get(j));
			}
		}
	}
}
