package MpaAlgos;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class MPA 
{
	private Data graph;
	private int edge_added=0;
	private JunctionTree junction_tree=new JunctionTree();
	//private final double constant=1; 

	public JunctionTree getJunction_tree() {
		return junction_tree;
	}

	public void setJunction_tree(JunctionTree junction_tree) {
		this.junction_tree = junction_tree;
	}

	public void setData(Data graph){
		this.graph=graph;
	}
	
	public void printGraph(){
		graph.printAdjMat();
	}
	
	// Triangulating a graph using Minimum degree heuristic
	public void triangulate(){
		ArrayList<Integer> visited=new ArrayList<Integer>(graph.num_nodes);
		for(int i=0;i<graph.num_nodes;i++) {
			//Find minimum degree vertex
			int min=Integer.MAX_VALUE;
			int min_vertex=0;
			X:for(int vertex=0;vertex<graph.num_nodes;vertex++){
				for(Integer l:visited){
					if(l==vertex)
						continue X;
				}
				int sum=0;
				for(int neigh=0;neigh<graph.num_nodes;neigh++){
					sum+=graph.adj_mat[vertex][neigh];
				}
				if(sum<min){
					min=sum;
					min_vertex=vertex;
				}
			}
			
			int vertex=min_vertex;
			ArrayList<Integer> neighbours=new ArrayList<Integer>(graph.num_nodes);
			Y:for(int neigh=0;neigh<graph.num_nodes;neigh++){
				for(Integer I:visited){
					if(neigh==I)
						continue Y;
				}
				if(vertex!=neigh && graph.adj_mat[vertex][neigh]==1)
					neighbours.add(neigh);
			}
			for(int k=0;k<neighbours.size();k++){
				for(int j=k+1;j<neighbours.size();j++){
					if(!(graph.adj_mat[neighbours.get(k)][neighbours.get(j)]==1)){	
						graph.adj_mat[neighbours.get(k)][neighbours.get(j)]=graph.adj_mat[neighbours.get(j)][neighbours.get(k)]=1;
						edge_added+=1;
					}
				}
			}
//			for(Integer v1:neighbours){
//				for(Integer v2:neighbours){
//					if(v1!=v2)
//						graph.adj_mat[v1][v2]=graph.adj_mat[v2][v1]=1;
//				}
//			}
			visited.add(vertex);
		}
	}
	
	public void getCliqueTree() {
		//Maximum Cardinality Search
		//Find Numbering of vertices (wlog  Assuming 0 is selected first vertex)
		
		ArrayList<Integer> index_assigned=new ArrayList<Integer>();
		index_assigned.add(0);
		int[] neigh_cnt=new int[graph.num_nodes];
		
		for(int i=1;i<graph.num_nodes;i++){
			int max_cnt=Integer.MIN_VALUE,max_node=0;
			for(int candidate=0;candidate<graph.num_nodes;candidate++){
				int count=0;
				if(!index_assigned.contains(candidate)){
					for(Integer n:index_assigned){
						if(graph.adj_mat[n][candidate]==1){
							count+=1;
						}	
					}
					if(count>max_cnt){
						max_cnt=count;
						max_node=candidate;
					}
				}		
			}
			index_assigned.add(max_node);
			neigh_cnt[max_node]=max_cnt;
		}
		
//		System.out.println(index_assigned.toString());
//		for(int i=0;i<graph.num_nodes;i++)
//			System.out.print(neigh_cnt[i]);
		
		//Create Cliques
		ArrayList<ArrayList<Integer>> cliqueNodes=new ArrayList<ArrayList<Integer>>();
		
		for(int index=graph.num_nodes-1;index>0;index--){
			ArrayList<Integer> temp =new ArrayList<Integer>();
			if( index ==graph.num_nodes-1 || neigh_cnt[index_assigned.get(index+1)]<=neigh_cnt[index_assigned.get(index)]){				
				temp.add(index_assigned.get(index));
				for(int i=index-1;i>=0;i--){
					if(graph.adj_mat[index_assigned.get(index)][index_assigned.get(i)]==1)
						temp.add(index_assigned.get(i));
				}
				Collections.sort(temp);
				cliqueNodes.add(temp);
			}
		}
//		System.out.println(cliqueNodes.toString());
//		int[] JT_V1 = new int[cliqueNodes.size()*cliqueNodes.size()];
//		int[] JT_V2 = new int[cliqueNodes.size()*cliqueNodes.size()];
//		int[] JT_WT = new int[cliqueNodes.size()*cliqueNodes.size()];
//		int k=0;
		class Edges
		{
			int V1,V2,Weight;
			public String toString(){
				return V1+" "+V2+" "+Weight;
			}
		}
		ArrayList<Edges> edges=new ArrayList<Edges>();
		for(int i=0;i<cliqueNodes.size();i++){
			for(int j=0;j<cliqueNodes.size();j++){
				Edges edge=new Edges();
				edge.V1=i;
				edge.V2=j;
				edge.Weight=intersection(cliqueNodes.get(i), cliqueNodes.get(j)).size();
				edges.add(edge);
			}
		}
		
		Collections.sort(edges,new Comparator<Edges>() {
		public int compare(Edges e1, Edges e2){
			return (e1.Weight<e2.Weight)?1:0;
		}});
//		for(Edges e:edges)
//			System.out.println(e.toString());
		
		//Krushkal Algorithm to find Maximum Spanning Tree
		
		junction_tree.nodes=cliqueNodes;
		int[] arr=new int[cliqueNodes.size()];
		for(int i=0;i<cliqueNodes.size();i++){
			arr[i]=i;
		}
		int k=0,v1,v2,temp,j;
		int i=0;
		
		while(k<cliqueNodes.size()-1){
			v1=edges.get(i).V1;
			v2=edges.get(i).V2;
			if(arr[v1]==arr[v2]){
				i++;
				continue;
			}
			if(arr[v1]<arr[v2]){
				JT_Edge e=new JT_Edge();
				e.setStart_node(v1);
				e.setEnd_node(v2);
				e.setSeparator(intersection(cliqueNodes.get(v1),cliqueNodes.get(v2)));
				junction_tree.appendEdge(e);
				temp=arr[v2];
				for(j=0;j<cliqueNodes.size();j++){
					if(arr[j]==temp){
						arr[j]=arr[v1];
					}
				}
				i++;
				k++;
				continue;
			}
			else if(arr[v2]<arr[v1]){
				JT_Edge e=new JT_Edge();
				e.setStart_node(v1);
				e.setEnd_node(v2);
				e.setSeparator(intersection(cliqueNodes.get(v1),cliqueNodes.get(v2)));
				junction_tree.appendEdge(e);
				temp=arr[v1];
				for(j=0;j<cliqueNodes.size();j++){
					if(arr[j]==temp){
						arr[j]=arr[v2];
					}
				}
	 			i++;
				k++;
				continue;
			}
		}
		
		for(ArrayList<Integer> node:cliqueNodes){
			ArrayList<ArrayList<Integer>> factorList = new ArrayList<ArrayList<Integer>>();
			//Add factors to factor list
			for(ArrayList<Integer> factor: graph.phi.keySet()){
				if(node.containsAll(factor)){
					factorList.add(factor);
				}
			}
			HashMap<ArrayList<Integer>, Double> t_1=new HashMap<ArrayList<Integer>, Double>();
			ArrayList<Integer> t_1_vars = new ArrayList<Integer>();
			
			boolean first=true;
			for(ArrayList<Integer> factor: factorList){
				if(first){
					t_1=graph.phi.get(factor);
					t_1_vars = new ArrayList<Integer>(factor);
					first=false;
				}
				else{
					t_1=factorProduct(t_1_vars, factor, t_1 , graph.phi.get(factor));
				}
			}
			int index=cliqueNodes.indexOf(node);
			cliqueNodes.set(index, t_1_vars);
			junction_tree.phi.put(t_1_vars, t_1);
//			System.out.println(node+" "+t_1);
		
		}
	
		
		//Compute Clique potential Original
//		for(ArrayList<Integer> node:cliqueNodes){
//			ArrayList<ArrayList<Integer>> clique_vals=new ArrayList<ArrayList<Integer>>();
//			for(Integer ele:node){
//				ArrayList<ArrayList<Integer>> temp_vals=new ArrayList<ArrayList<Integer>>();
//				if(clique_vals.isEmpty()){
//					for(int l=0;l<graph.node_vals[ele];l++){
//						temp_vals.add(new ArrayList<Integer>(Arrays.asList(l)));
//					}
//					clique_vals=temp_vals;
//					continue;
//				}
//				
//				for(ArrayList<Integer> vals:clique_vals){
//					for(int l=0;l<graph.node_vals[ele];l++)
//					{
//						ArrayList<Integer> newT= new ArrayList<Integer>(vals);
//						newT.add(l);
//						temp_vals.add(newT);	
//					}
//				}
//				clique_vals=temp_vals;		
//			}
//		
//			HashMap<ArrayList<Integer>, Double> t_1=new HashMap<ArrayList<Integer>, Double>();
//			for(ArrayList<Integer> vals:clique_vals){
//				double pot=1;
//				for(int x=0;x<vals.size();x++){
//					for(int y=x+1;y<vals.size();y++){
//						ArrayList<Integer> t=new ArrayList<Integer>(Arrays.asList(node.get(x),node.get(y)));
//						
//						HashMap<ArrayList<Integer>,Double> t_vals =graph.phi.get(t);
//						if(t_vals!=null){
//							
//							ArrayList<Integer> k1=new ArrayList<Integer>(Arrays.asList(vals.get(x),vals.get(y)));
//							pot*=t_vals.get(k1);
//						}
//						else 
//						{
//							t=new ArrayList<Integer>(Arrays.asList(node.get(y),node.get(x)));
//							t_vals =graph.phi.get(t);
//							if(t_vals!=null){
//								ArrayList<Integer> k1=new ArrayList<Integer>(Arrays.asList(vals.get(y),vals.get(x)));
//								pot*=t_vals.get(k1);
//							}
//							else{
//								pot*=constant;
//							}
//						}
//					}
//					t_1.put(vals, pot);
//				}	
//			}
//			junction_tree.phi.put(node, t_1);
//		}
//		System.out.println(junction_tree.phi.toString());
	}
	
	// Basic Given potential product
	public HashMap<ArrayList<Integer>,Double> factorProduct(ArrayList<Integer>a_var,ArrayList<Integer>b_var,HashMap<ArrayList<Integer>,Double> a,HashMap<ArrayList<Integer>,Double> b)
	{
	//a_var is pass by reference
		HashMap<ArrayList<Integer>,Double> c=new HashMap<ArrayList<Integer>, Double>();
		if(a.keySet().size()==0){
			return b;
		}
		else if(b.keySet().size()==0){
			return a;
		}
		
		HashSet<Integer> newA= new HashSet<Integer>(a_var);
		newA.addAll(b_var);
		ArrayList<Integer> new_vars=new ArrayList<Integer>(newA);
		ArrayList<ArrayList<Integer>> msg_vals=generateVals1(new_vars);
		for (ArrayList<Integer> vals:msg_vals){
			ArrayList<Integer> key1=new ArrayList<Integer>();
			for(Integer i:a_var){
				int index=new_vars.indexOf(i);
				key1.add(vals.get(index));
			}
			ArrayList<Integer> key2=new ArrayList<Integer>();
			for(Integer i:b_var){
				int index=new_vars.indexOf(i);			
				key2.add(vals.get(index));
			}
//			System.out.println(key1.toString()+key2);
			c.put(vals,a.get(key1)*b.get(key2) );
		}
		a_var.clear();
		a_var.addAll(new_vars);//Reassign reference
		return c;
	}
	
	// used only by factorProduct
	public ArrayList<ArrayList<Integer>>generateVals1(ArrayList<Integer> newA)
	{
		
		ArrayList<ArrayList<Integer>> msg_vals=new ArrayList<ArrayList<Integer>>();
		for(Integer ele:newA){
			ArrayList<ArrayList<Integer>> temp_vals=new ArrayList<ArrayList<Integer>>();
			if(msg_vals.isEmpty()){
				for(int l=0;l<graph.node_vals[ele];l++){
					temp_vals.add(new ArrayList<Integer>(Arrays.asList(l)));
				}
				msg_vals=temp_vals;
				continue;
			}
			
			for(ArrayList<Integer> vals:msg_vals){
				for(int l=0;l<graph.node_vals[ele];l++)
				{
					ArrayList<Integer> newT= new ArrayList<Integer>(vals);
					newT.add(l);
					temp_vals.add(newT);	
				}
			}
			msg_vals=temp_vals;		
		}
//		System.out.println(newA);
//		if(newA.containsAll(Arrays.asList(7,6,5,8,3,2)))
//			System.out.println(msg_vals.size());
		return msg_vals;
	}
	
	
	// Find common elements of two ArrayLists
	public <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
		ArrayList<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
	
	// Prints Information about Junction Tree
	public void printJTInfo(){
		int biggest_clique=Integer.MIN_VALUE;
		for(ArrayList<Integer> clique:junction_tree.nodes){
			if(clique.size()>biggest_clique)
				biggest_clique=clique.size();

		}
		int biggest_separator=0;
		for(JT_Edge e:junction_tree.edges){
			if(biggest_separator < e.getSeparator().size())
				biggest_separator = e.getSeparator().size();
		}
		System.out.println("Number of extra edges added = "+edge_added);
		System.out.println("Size of Biggest Clique in JT = "+biggest_clique);
		System.out.println("Size of Biggest Separator set in JT = "+biggest_separator);
		System.out.println(junction_tree.toString());
	}
}
