package MpaAlgos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
class DecidedMax{
	Double potential;
	ArrayList<Integer> decVar;
	ArrayList<Integer> decVarVal;
	@Override
	public String toString() {
		return "DecidedMax [potential=" + potential + ", decVar=" + decVar
				+ ", decVarVal=" + decVarVal + "]";
	}
	public DecidedMax() {
		super();
		this.potential = 0.0;
		this.decVar = new ArrayList<Integer>();
		this.decVarVal = new ArrayList<Integer>();
	}
	
}
class MsgEdge{
	public int otherNode;
	public ArrayList<Integer> sep;
	public HashMap<ArrayList<Integer>,Double> msg;
	public HashMap<ArrayList<Integer>,DecidedMax> max_msg;
	public HashMap<ArrayList<Integer>, DecidedMax> getMax_msg() {
		return max_msg;
	}
	public void setMax_msg(HashMap<ArrayList<Integer>, DecidedMax> max_msg) {
		this.max_msg = max_msg;
	}
	public int getOtherNode() {
		return otherNode;
	}
	public void setOtherNode(int otherNode) {
		this.otherNode = otherNode;
	}
	public ArrayList<Integer> getSep() {
		return sep;
	}
	public void setSep(ArrayList<Integer> sep) {
		this.sep = sep;
	}
	public HashMap<ArrayList<Integer>, Double> getMsg() {
		return msg;
	}
	public void setMsg(HashMap<ArrayList<Integer>, Double> msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "MsgEdge [otherNode=" + otherNode + ", sep=" + sep + ", msg="
				+ msg + "]";
	}
	
	MsgEdge()
	{
		otherNode=0;
		sep=new ArrayList<Integer>();
		msg=new HashMap<ArrayList<Integer>, Double>();
		max_msg=new HashMap<ArrayList<Integer>, DecidedMax>();
	}
}

class MsgNode{
	public HashMap<ArrayList<Integer>, Double> phi=new  HashMap<ArrayList<Integer>,Double>();
	public ArrayList<Integer> clique;
	public ArrayList<MsgEdge> edges;
	
	public int msg_arr;
	public MsgNode() {
		// TODO Auto-generated constructor stub
		clique=new ArrayList<Integer>();
		edges=new ArrayList<MsgEdge>();
		msg_arr=0;
	}
	
	public HashMap<ArrayList<Integer>, Double> getPhi() {
		return phi;
	}

	public void setPhi(HashMap<ArrayList<Integer>, Double> phi) {
		this.phi = phi;
	}

	public int getMsg_arr() {
		return msg_arr;
	}

	public void setMsg_arr(int msg_arr) {
		this.msg_arr = msg_arr;
	}

	public ArrayList<Integer> getClique() {
		return clique;
	}
	public void setClique(ArrayList<Integer> clique) {
		this.clique = clique;
	}
	public ArrayList<MsgEdge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<MsgEdge> edges) {
		this.edges = edges;
	}
	@Override
	public String toString() {
		return "MsgNode [clique=" + clique + ", edges=" + edges + "]";
	}
	
}
// Represents an Edge of Junction Tree
class JT_Edge
{
	int start_node;
	int end_node;
	ArrayList<Integer> separator;
	
	public int getStart_node() {
		return start_node;
	}
	public void setStart_node(int start_node) {
		this.start_node = start_node;
	}
	public int getEnd_node() {
		return end_node;
	}
	public void setEnd_node(int end_node) {
		this.end_node = end_node;
	}
	public ArrayList<Integer> getSeparator() {
		return separator;
	}
	public void setSeparator(ArrayList<Integer> separator) {
		this.separator = separator;
	}
	
	@Override
	public String toString() {
		return "JT_Edge [sn=" + start_node + ", en=" + end_node
				+ ", sep=" + separator + "]";
	}
}

//Represents Nodes and Edges of a Junction Tree
class JunctionTree
{
	HashMap<ArrayList<Integer>,HashMap<ArrayList<Integer>, Double>> phi=new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>,Double>>();
	ArrayList<ArrayList<Integer>> nodes;
	ArrayList<JT_Edge> edges=new ArrayList<JT_Edge>();
	
	public int getDegreeofJTNode(ArrayList<Integer> nd){
		int deg=0;
		int id=nodes.indexOf(nd);
		if(id==-1)
			return 0;
		for(int j=0;j<edges.size();j++){
			if(edges.get(j).getStart_node()==id || edges.get(j).getEnd_node()==id)
				deg++;
		}
		return deg;
	}
	
	public ArrayList<ArrayList<Integer>> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<ArrayList<Integer>> nodes) {
		this.nodes = nodes;
	}
	public ArrayList<JT_Edge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<JT_Edge> edges) {
		this.edges = edges;
	}
	
	public void appendEdge(JT_Edge e){
		edges.add(e);
	}
	
	@Override
	public String toString() {
		String JT_desc="";
		JT_desc+="Clique_ID\tClique_Size\tClique_Nodes\n";
		for(int i=0;i<nodes.size();i++)
			JT_desc+=(i+1)+"\t\t"+nodes.get(i).size()+"\t\t"+nodes.get(i).toString()+"\n";
//		for(int i=0;i<edges.size();i++)
//			JT_desc+=edges.get(i)+"\n";
		return JT_desc;
	}
	
}

// Read input data files

public class Data 
{
	public HashMap<ArrayList<Integer>,HashMap<ArrayList<Integer>, Double>> phi=new HashMap<ArrayList<Integer>,HashMap<ArrayList<Integer>,Double>>();
	public int[] node_vals;
	public int[][] adj_mat;
	public int num_nodes;
	public int num_edges;
	
	// Read input from first file
	void inputGraph(String filename){
		 FileInputStream df=null;
		 try{
			 df= new FileInputStream(filename);
	         InputStreamReader Stream = new InputStreamReader(df);
	         BufferedReader reader=new BufferedReader(Stream);
	         num_nodes=Integer.parseInt(reader.readLine());
	         node_vals=new int[num_nodes];
	         num_edges=Integer.parseInt(reader.readLine());
	         adj_mat=new int[num_nodes][num_nodes];
	         for (int i=0;i<num_nodes;i++){
	        	 String[] inp=reader.readLine().split(" ");
	        	 node_vals[Integer.parseInt(inp[0])]=Integer.parseInt(inp[1]);
	         }
	         for(int i=0;i<num_edges;i++){
	        	 String[] inp=reader.readLine().split(" ");
	        	 int vert1=Integer.parseInt(inp[0]);
	        	 int vert2=Integer.parseInt(inp[1]);
	        	 adj_mat[vert1][vert2]=1;
	        	 adj_mat[vert2][vert1]=1;
	         }
		 }
		 catch (Exception E){
			 E.printStackTrace();
		 }
		 
	}
	void inputPotentials1(String filename){
		 FileInputStream df=null;
		 try{
			 df= new FileInputStream(filename);
	         InputStreamReader Stream = new InputStreamReader(df);
	         BufferedReader reader=new BufferedReader(Stream);
	         for(int i=0;i<num_edges;i++){
	        	 String[] words= reader.readLine().split(" ");
	        	 ArrayList<Integer> e=new ArrayList<Integer>();
	        	 e.add(Integer.parseInt(words[1]));
	        	 e.add(Integer.parseInt(words[2]));
	        	      	 
	        	 int j=node_vals[Integer.parseInt(words[1])]*node_vals[Integer.parseInt(words[2])];
	        	 HashMap<ArrayList<Integer>, Double> temp= new HashMap<ArrayList<Integer>, Double>();
	        	 while(j>0){
	        		 
	        		 words=reader.readLine().split(" ");
	        		 ArrayList<Integer> key=new ArrayList<Integer>();
	        		 key.add(Integer.parseInt(words[0]));
	        		 key.add(Integer.parseInt(words[1]));
	        		 temp.put(key,Double.parseDouble(words[2]));
	        		 j--;
	        		 
	        	 }
	        	 phi.put(e,temp);
	         }
	         
//	         System.out.println(phi.toString());
		 }
		 catch (Exception E){
			 E.printStackTrace();
		 }
		 
	}
	
	void inputPotentials(String filename){
		 FileInputStream df=null;
		 try{
			 df= new FileInputStream(filename);
	         InputStreamReader Stream = new InputStreamReader(df);
	         BufferedReader reader=new BufferedReader(Stream);
	         
	         String line=null;
	         while ((line = reader.readLine()) != null) {
	        	 String[] words=line.split(" ");
	        	 
	        	 ArrayList<Integer> factorVars=new ArrayList<Integer>();
	        	 factorVars.clear();
	        	 for(int i=1;i<words.length;i++){
	        		factorVars.add(Integer.parseInt(words[i]));
	        	 }
	        	 
	        	 int noe=1;
        		 for(int x:factorVars){
        			 noe*=node_vals[x];
        		 }
        		 HashMap<ArrayList<Integer>, Double> temp= new HashMap<ArrayList<Integer>, Double>();
        		 for(int i=0;i<noe;i++){
        			 line=reader.readLine();
        			 words=line.split(" ");
        			 
        			 ArrayList<Integer> key=new ArrayList<Integer>();
        			 for(int j=0;j<words.length-1;j++){
            			 key.add(Integer.parseInt(words[j]));
        			 } 
        			 temp.put(key,Double.parseDouble(words[words.length-1]));		 	        	 
        		 }
        		 phi.put(factorVars,temp);
	         } 
	         
	        
//	         System.out.println("Phi:"+phi.toString());
		 }
		 catch (Exception E){
			 E.printStackTrace();
		 }
		 
	}
	
	// Prints Adjacency Matrix of a graph
	public  void printAdjMat(){
		for(int i=0;i<num_nodes;i++)
		{	for(int j=0;j<num_nodes;j++)
			{	
				System.out.print(String.valueOf(adj_mat[i][j])+" ");
			}
			System.out.println("");
		}
	}
}
