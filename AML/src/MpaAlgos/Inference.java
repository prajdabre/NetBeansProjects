package MpaAlgos;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


//class MyArrayList<T> extends ArrayList<T> {
//	
//	public MyArrayList(){
//		super();
//	}
//	
//	public boolean equals(ArrayList<T> list){
//		if(this.size()!=list.size())
//			return false;
//		for(T a:this){
//			if(!list.contains(a))
//				return false;
//		}
//		return true;
//	}
//}

//public class Inference {
//	public HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> msg = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>,Double>>();
//	
//	public void messagePassing(JunctionTree jt){
//		
//		for(ArrayList<Integer> n: jt.nodes){
//			int i=jt.nodes.indexOf(n);
//			int j=-1;
//			int deg=0;
//			for(int k=0;k<jt.edges.size();k++){
//			
//				if(jt.edges.get(k).getStart_node()==i || jt.edges.get(k).getEnd_node()==i)
//					if(! msg.containsKey(new ArrayList<Integer>(Arrays.asList(i,k)))){
//						deg++;
//						j=k;
//					}
//			}
//			
//			if(deg==1){
//				// Pass message i-->j
//				
//			}
//		}
//	}
//	
//}
public class Inference{
	public ArrayList<MsgNode> msgList;
	public int[] node_vals;
	Double Z=1.0;
	public Inference(JunctionTree jt,Data graph)
	{
		//System.out.println(jt.toString());
		node_vals=graph.node_vals;
		msgList=new ArrayList<MsgNode>();
		for(int i =0 ;i<jt.getNodes().size();i++){
			MsgNode tmp_node=new MsgNode();
			tmp_node.setClique(jt.getNodes().get(i));
			tmp_node.setPhi(jt.phi.get(jt.getNodes().get(i)));
			for(int j=0;j<jt.getEdges().size();j++){
				MsgEdge tmpMsgEdge=new MsgEdge(); 
				if(jt.getEdges().get(j).getStart_node()==i){
					tmpMsgEdge.setOtherNode(jt.getEdges().get(j).getEnd_node());
					tmpMsgEdge.setSep(jt.getEdges().get(j).getSeparator());
					tmp_node.getEdges().add(tmpMsgEdge);
				}
				else if(jt.getEdges().get(j).getEnd_node()==i){
					tmpMsgEdge.setOtherNode(jt.getEdges().get(j).getStart_node());
					tmpMsgEdge.setSep(jt.getEdges().get(j).getSeparator());
					tmp_node.getEdges().add(tmpMsgEdge);
				}
				
			}
			msgList.add(tmp_node);
		}
		
	}
	public void computeMsgs(){
		
		for(int i=0;i<2*(msgList.size()-1);i++)
		{
			for(MsgNode m:msgList){
	//			if msgs are one less than number of neighbours ,node is ready to send msg
				for(MsgEdge e:m.getEdges()){
					boolean ready=true;
					//If msg is already present continue
					for(int p=0;p<msgList.get(e.getOtherNode()).getEdges().size();p++){
						if(msgList.get(e.getOtherNode()).getEdges().get(p).getOtherNode()==msgList.indexOf(m)){
							if(msgList.get(e.getOtherNode()).getEdges().get(p).getMsg().size()!=0){
								ready=false;
							}
						}
					}
					//If other edges on this node have their msgs
					for(MsgEdge oe:m.getEdges()){
						if(oe.equals(e)){
							continue;
						}
	//					oe.getMsg()==null : then this is not ready
						if(oe.getMsg().size()==0){
							ready=false;
							break;
						}
					}
					// 'e' edge is ready to send msg
					if(ready){
						//int otherNode=e.getOtherNode();
						HashMap<ArrayList<Integer>,Double> msgTable = new HashMap<ArrayList<Integer>, Double>();
						ArrayList<Integer> magTabVars=new ArrayList<Integer>();
						for(MsgEdge oe:m.getEdges()){
							if(oe.equals(e)){
								continue;
							}
							if(msgTable.size()==0){
								msgTable=oe.getMsg();
								magTabVars.addAll(oe.getSep());
							}
							else{
								msgTable=product(magTabVars,oe.getSep(),msgTable,oe.getMsg());
							}
						}
//						if(msgTable.size()==0)
//							magTabVars=m.getClique();
						msgTable=product(magTabVars,m.getClique(),msgTable,m.getPhi());
					
						//ArrayList<Integer> marg=subtraction(m.getClique(),e.getSep());
						msgTable=sumMarginal(magTabVars,msgTable,e.getSep());
	//					Set message in receiving node
						for(int p=0;p<msgList.get(e.getOtherNode()).getEdges().size();p++){
							if(msgList.get(e.getOtherNode()).getEdges().get(p).getOtherNode()==msgList.indexOf(m)){
								msgList.get(e.getOtherNode()).getEdges().get(p).setMsg(msgTable);
							}
						}
					}
				}		
			}
		}
//		System.out.println("NOde struct:"+msgList.toString());
		//System.out.println(msgList.toString());
	}
	public HashMap<ArrayList<Integer>,Double> product(ArrayList<Integer>a_var,ArrayList<Integer>b_var,HashMap<ArrayList<Integer>,Double> a,HashMap<ArrayList<Integer>,Double> b)
	{
	//a_var is pass by reference
		HashMap<ArrayList<Integer>,Double> c=new HashMap<ArrayList<Integer>, Double>();
		if(a.keySet().size()==0){
			a_var.clear();
			a_var.addAll(b_var);
			return b;
		}
		else if(b.keySet().size()==0){
			return a;
		}
		
		HashSet<Integer> newA= new HashSet<Integer>(a_var);
		newA.addAll(b_var);
		ArrayList<Integer> new_vars=new ArrayList<Integer>(newA);
		ArrayList<ArrayList<Integer>> msg_vals=generateVals(new_vars);
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
			//System.out.println(a.get(key1));
			//System.out.println(b.get(key2));
                        if(a.get(key1)==null){
                            c.put(vals,1.0*b.get(key2) );
                        } else
			c.put(vals,a.get(key1)*b.get(key2) );
		}
		a_var.clear();
		a_var.addAll(new_vars);//Reassign reference
		return c;
	}
	public ArrayList<ArrayList<Integer>>generateVals(ArrayList<Integer> newA)
	{
		
		ArrayList<ArrayList<Integer>> msg_vals=new ArrayList<ArrayList<Integer>>();
		for(Integer ele:newA){
			ArrayList<ArrayList<Integer>> temp_vals=new ArrayList<ArrayList<Integer>>();
			if(msg_vals.isEmpty()){
				for(int l=0;l<node_vals[ele];l++){

					temp_vals.add(new ArrayList<Integer>(Arrays.asList(l)));
				}
				msg_vals=temp_vals;
				continue;
			}
			
			for(ArrayList<Integer> vals:msg_vals){
				for(int l=0;l<node_vals[ele];l++)
				{
					ArrayList<Integer> newT= new ArrayList<Integer>(vals);
					newT.add(l);
					temp_vals.add(newT);	
				}
			}
			msg_vals=temp_vals;		
		}
		return msg_vals;
	}
	public HashMap<ArrayList<Integer>,Double> sumMarginal(ArrayList<Integer> vars,HashMap<ArrayList<Integer>,Double> a,ArrayList<Integer> x)
	{
		HashMap<ArrayList<Integer>,Double> c=new HashMap<ArrayList<Integer>, Double>();
		ArrayList<ArrayList<Integer>> mar_vals=generateVals(x);
		for(ArrayList<Integer> vals:mar_vals){
			c.put(vals, 0.0);
			for(ArrayList<Integer> key:a.keySet()){
				boolean isEqual=true;
				for(Integer i:x){
					if(key.get(vars.indexOf(i))!=vals.get(x.indexOf(i)))
						isEqual=false;
				}
				if(isEqual){
					c.put(vals,c.get(vals)+a.get(key));
				}
			}		
		}
		return c;
	}
	
	public HashMap<ArrayList<Integer>,Double> infer(ArrayList<Integer> s){
//		Assumption: s belongs to same node
		HashMap<ArrayList<Integer>,Double> msgTable = new HashMap<ArrayList<Integer>, Double>();
		ArrayList<Integer> magTabVars=new ArrayList<Integer>();
		for(MsgNode m:msgList){
			if(m.getClique().containsAll(s)){
				for(MsgEdge e:m.getEdges()){
					if(msgTable.size()==0){
						msgTable=e.getMsg();
						magTabVars.addAll(e.getSep());
					}
					else{
						msgTable=product(magTabVars,e.getSep(),msgTable,e.getMsg());
					}
				}
				msgTable=product(magTabVars, m.getClique(), msgTable, m.getPhi());
//				ArrayList<Integer> rem=new ArrayList<Integer>(m.getClique());
//				rem.removeAll(s);
				msgTable=sumMarginal(magTabVars, msgTable, s);
				break;
			}
		}
		String jp=" ";
//		for(Integer i :s)
//		{
//			System.out.print("X_"+i+"\t");
//			jp+="X_"+i+" ";
//		}
	
//		System.out.print("probability("+jp+")\n");
		Z=0.0;

		for(ArrayList<Integer> key:msgTable.keySet()){
			Z+=msgTable.get(key);
			
		}
//		ArrayList<ArrayList<Integer>> keys=new ArrayList<ArrayList<Integer>>(msgTable.keySet());
		for(ArrayList<Integer> key:msgTable.keySet() ){
//			for(Integer i :key){
//				System.out.print(i+"\t");
//			}
			//System.out.print(msgTable.get(key)/Z+"\n");
                    msgTable.put(key,msgTable.get(key)/Z);
		}

                return msgTable;
	}
	public void findMaxMarginal()
	{
		HashMap<ArrayList<Integer>, DecidedMax> max_table;
		MsgNode start=msgList.get(0);
		max_table=computeMaxMsg(start, -1);
//		System.out.println(max_table.toString());
		Double max_pot=0.0;
		ArrayList<Integer> max_key=new ArrayList<Integer>();
		for(ArrayList<Integer> key : max_table.keySet())
		{
			if(max_table.get(key).potential>max_pot)
			{
				max_pot=max_table.get(key).potential;
				max_key=key;
			}
		}
		ArrayList<Integer> temp =new ArrayList<Integer>(start.getClique());
		temp.removeAll(max_table.get(max_key).decVar);
		//Still to print something
		int[] max_assignment=new int[temp.size()+max_table.get(max_key).decVar.size()];
		for(int i=0;i<temp.size();i++){
			max_assignment[temp.get(i)]=max_key.get(i);
		}
		for(int i=0;i<max_table.get(max_key).decVar.size();i++){
			max_assignment[max_table.get(max_key).decVar.get(i)]=max_table.get(max_key).decVarVal.get(i);
		}
		System.out.print("\nMAP Assignemt Vector: ");
		for(int i=0;i<max_assignment.length;i++){
			System.out.print(i+" : "+max_assignment[i]+", ");
		}
		System.out.println("\nProbability of MAP Configaration: "+max_table.get(max_key).potential/Z);
		
	}
	public HashMap<ArrayList<Integer>, DecidedMax> computeMaxMsg(MsgNode node,int parent){
		ArrayList<HashMap<ArrayList<Integer>, DecidedMax>> max_msg=new ArrayList<HashMap<ArrayList<Integer>,DecidedMax>>();
		ArrayList<Integer> max_edge_index=new ArrayList<Integer>();
		for(MsgEdge me:node.getEdges()){
			if(parent==me.getOtherNode())
				continue;
			else{
				max_msg.add(computeMaxMsg(msgList.get(me.getOtherNode()), msgList.indexOf(node)));
				max_edge_index.add(node.getEdges().indexOf(me));
			}
		}
		//If this is leaf node
		if(max_msg.size()==0){
			return maxMarginal(node.getClique(), node.getPhi(), node.getEdges().get(0).getSep());
		}
		else
		{
//			take product of messages
			HashMap<ArrayList<Integer>,DecidedMax> running_prod;
			running_prod=max_msg.get(0);
			ArrayList<Integer> prod_var=new ArrayList<Integer>(node.getEdges().get(max_edge_index.get(0)).getSep());
			for(int i=1;i<max_msg.size();i++){
				running_prod=maxProduct(prod_var, node.getEdges().get(max_edge_index.get(i)).getSep(), running_prod, max_msg.get(i));
			}
//			take product with clique
			HashMap<ArrayList<Integer>,DecidedMax> convertedPhi=new HashMap<ArrayList<Integer>, DecidedMax>();
			for(ArrayList<Integer> key:node.getPhi().keySet()){
				DecidedMax temp=new DecidedMax();
				temp.potential=node.getPhi().get(key);
				convertedPhi.put(key, temp);
			}
			running_prod=maxProduct(prod_var, node.getClique(), running_prod, convertedPhi);
//			maxMarginal
//			find parent
			ArrayList<Integer> separator=null;
			for(MsgEdge me:node.getEdges()){
				if(parent==me.getOtherNode()){
					separator=me.getSep();
					break;
				}
			}
			if(parent!=-1)
				running_prod=maxMarginal1(prod_var, running_prod,separator );
//			return
			return running_prod;
		}
		
	}
	public HashMap<ArrayList<Integer>,DecidedMax> maxMarginal(ArrayList<Integer> vars,HashMap<ArrayList<Integer>,Double> a,ArrayList<Integer> x)
	{
		HashMap<ArrayList<Integer>,DecidedMax> c=new HashMap<ArrayList<Integer>, DecidedMax>();
		ArrayList<ArrayList<Integer>> mar_vals=generateVals(x);
		DecidedMax dm=new DecidedMax();
		ArrayList<Integer> temp=new ArrayList<Integer>(vars);
		temp.removeAll(x);
		// a - argument (clique node assignments) , c - return parameter (max_marginal)
		for(ArrayList<Integer> vals:mar_vals){
			c.put(vals, new DecidedMax());
			for(ArrayList<Integer> key:a.keySet()){
				boolean isEqual=true;
				for(Integer i:x){
					if(key.get(vars.indexOf(i))!=vals.get(x.indexOf(i))){
						isEqual=false;
						break;
					}
				}
				if(isEqual){
					dm=new DecidedMax();
					dm.decVar=temp;
					ArrayList<Integer> tempVal=new ArrayList<Integer>();
					for(Integer k:temp){
						tempVal.add(key.get(vars.indexOf(k)));
					}
					dm.decVarVal=(c.get(vals).potential<=a.get(key))?tempVal: c.get(vals).decVarVal;
					dm.potential=(c.get(vals).potential<=a.get(key))?a.get(key): c.get(vals).potential;
					c.put(vals, dm);
				}
			}		
		}
		return c;
	}
	public HashMap<ArrayList<Integer>,DecidedMax> maxMarginal1(ArrayList<Integer> vars,HashMap<ArrayList<Integer>,DecidedMax> a,ArrayList<Integer> x)
	{
		HashMap<ArrayList<Integer>,DecidedMax> c=new HashMap<ArrayList<Integer>, DecidedMax>();
		ArrayList<ArrayList<Integer>> mar_vals=generateVals(x);
		DecidedMax dm=new DecidedMax();
		ArrayList<Integer> temp=new ArrayList<Integer>(vars);
		temp.removeAll(x);
		// a - argument (clique node assignments) , c - return parameter (max_marginal)
		for(ArrayList<Integer> vals:mar_vals){
			c.put(vals, new DecidedMax());
			for(ArrayList<Integer> key:a.keySet()){
				boolean isEqual=true;
				for(Integer i:x){
					if(key.get(vars.indexOf(i))!=vals.get(x.indexOf(i)))
					{	isEqual=false;
						break;
					}
				}
				if(isEqual){
					dm=new DecidedMax();
					ArrayList<Integer> tempVal=new ArrayList<Integer>();
					dm.decVar.addAll(a.get(key).decVar);
					tempVal.addAll(a.get(key).decVarVal);
					dm.decVar.addAll(temp);		
					for(Integer k:temp){
						tempVal.add(key.get(vars.indexOf(k)));
					}
//					if((c.get(vals).potential<=a.get(key))){
//						c.get(vals).decVar
//					}
					dm.decVarVal=(c.get(vals).potential<=a.get(key).potential)?tempVal: c.get(vals).decVarVal;
					dm.potential=(c.get(vals).potential<=a.get(key).potential)?a.get(key).potential: c.get(vals).potential;
					c.put(vals, dm);
				}
			}		
		}
		return c;
	}
	public HashMap<ArrayList<Integer>,DecidedMax> maxProduct(ArrayList<Integer>a_var,ArrayList<Integer>b_var,HashMap<ArrayList<Integer>,DecidedMax> a,HashMap<ArrayList<Integer>,DecidedMax> b)
	{
	//a_var is pass by reference
		HashMap<ArrayList<Integer>,DecidedMax> c=new HashMap<ArrayList<Integer>, DecidedMax>();
		if(a.keySet().size()==0){
			a_var.clear();
			a_var.addAll(b_var);
			return b;
		}
		else if(b.keySet().size()==0){
			return a;
		}
		
		HashSet<Integer> newA= new HashSet<Integer>(a_var);
		newA.addAll(b_var);
		ArrayList<Integer> new_vars=new ArrayList<Integer>(newA);
		ArrayList<ArrayList<Integer>> msg_vals=generateVals(new_vars);
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
			DecidedMax dm=new DecidedMax();
			dm.potential=a.get(key1).potential*b.get(key2).potential;
			ArrayList<Integer> tempVars=new ArrayList<Integer>(a.get(key1).decVar);
			tempVars.addAll(b.get(key2).decVar);
			ArrayList<Integer> tempVal=new ArrayList<Integer>(a.get(key1).decVarVal);
			tempVal.addAll(b.get(key2).decVarVal);
			dm.decVar=tempVars;
			dm.decVarVal=tempVal;
			c.put(vals,dm);
		}
		a_var.clear();
		a_var.addAll(new_vars);//Reassign reference
		return c;
	}

	public <T> ArrayList<T> subtraction(ArrayList<T> list1, ArrayList<T> list2) {
//		compute list1 - list2
		ArrayList<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(!list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
}
