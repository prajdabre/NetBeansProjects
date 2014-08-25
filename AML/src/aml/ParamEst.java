/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aml;

import MpaAlgos.Data;
import MpaAlgos.Inference;
import MpaAlgos.MPA;
import aml.LBFGS.ExceptionWithIflag;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
//import MpaAlgos.*;
import java.awt.GradientPaint;
import java.util.List;
import mpaananth.*;
import mpaananth.JunctionTree;

/**
 *
 * @author RAJ
 */
public class ParamEst {

    /**
     * @param args the command line arguments
     */
    public static ArrayList<ArrayList<Integer[]>> data = new ArrayList<ArrayList<Integer[]>>();
    public static HashMap<Integer, ArrayList<Integer>> graph = new HashMap<Integer, ArrayList<Integer>>();
    public static int graphmatrix[][];
    public static HashMap<ArrayList<Integer>, Double> indpot;
    public static HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>> fullpot;
    public static Integer recs = 0;
    public static Integer featcount = 0;
    public static Integer vars = 0;
    public static Integer edges = 0;
    public static int varairity[];
    public HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, ArrayList<Integer>>> cliquefeaturevaluemap;
    public double LLvalue = 0;
    public Double gradient[];
    public Double weights[];
    public ArrayList<Integer> curr;
    public ArrayList<ArrayList<Integer>> inits;
    //  public Data mparoutine;
    //  public MPA algo;
    //  public Inference infer;
    public Integer featurecount[];
    public Double learningrate = 0.3;
    public Double eps = 0.00001;
    public static String datafile = null;
    public UndirectedGraph udg;
    public JunctionTree jt;
    public Data mparoutine;
    public MPA algo;
    public Inference infer;

    public void genperms(List<Integer> clq, List<Integer> air) {
        if (clq.isEmpty()) {
            //System.out.println(curr);

            inits.add((ArrayList<Integer>) curr.clone());

            return;
        }
        //System.out.println(clq);
        //System.out.println(air);
        List<Integer> newclq = clq.subList(1, clq.size());
        List<Integer> newair = air.subList(1, air.size());
        //System.out.println(air.get(0));
        for (int i = 0; i < air.get(0); i++) {
            //System.out.println(air.get(0));
            if (i > 0) {
                //System.out.println(curr + " "  + air.get(0));
                curr.remove(curr.size() - 1);
            }
            curr.add(i);
            //System.out.println(curr);
            genperms(newclq, newair);
        }
        curr.remove(curr.size() - 1);

    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ExceptionWithIflag {
        // TODO code application logic here
        //System.out.println(args[0]);
        // datafile = args[0];
        ParamEst est = new ParamEst();
        est.runstructuredlearningalgo();


    }

    public void runstructuredlearningalgo() throws IOException {
        readdata();
        //est.showdata(data);
        PrepareData();
        int iter;
        for (iter = 0; iter > -1; iter++) {
            Integer terminate = 1;
            constructpottable();
            System.out.println("Potential Tables");
            System.out.println(fullpot);
            Calculate_CPTS1();
            gradient = CalcGradient(); //Gradient update
            weights = UpdateWeight();
            for (int i = 0; i < featcount; i++) {
                System.out.print(gradient[i] + " ");
            }
            System.out.println("");
//            for(int i=0;i<featcount;i++){
//                System.out.print(weights[i]+" ");
//            }
            // System.out.println("");

            if (toterminate(gradient)) {
                break;
            }

        }
        System.out.println("Process converged in " + iter + " iterations! Parameters have been learned:");
        System.out.println("Weights are:");
        for (int i = 0; i < featcount; i++) {
            System.out.println("Feature " + i + " : " + weights[i] + " ");
        }
        System.out.println("");

    }

    public void showgraph() {
        for (Integer al : graph.keySet()) {
            System.out.println(graph.get(al));
        }
    }

    public boolean toterminate(Double[] gradient) {
        Double sum = 0.0;
        for (int i = 0; i < gradient.length; i++) {
            sum = sum + Math.pow(gradient[i], 2);
        }
        if (sum < eps) {
            return true;
        } else {
            return false;
        }
    }

    public int getedgecount() {
        edges = 0;
        for (int i = 0; i < vars; i++) {
            for (int j = 0; j < vars; j++) {
                edges++;
            }
        }
        edges = edges / 2;
        return edges;
    }

    public void PrepareData() {


        featurecount = new Integer[featcount];
        Arrays.fill(featurecount, 0);





        cliquefeaturevaluemap = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, ArrayList<Integer>>>();
        for (int i = 0; i < data.size(); i++) {
            //System.out.println("Data " + i);
            ArrayList<Integer[]> records = data.get(i);
            //int numfeatcount = featcount;
            //System.out.println(numfeatcount);

            Integer labels[] = records.get(0);
            for (int j = 1; j < records.size(); j++) {
                Integer feat_clique[] = records.get(j);
                int featureno = feat_clique[0];
                Integer clique[] = Arrays.copyOfRange(feat_clique, 1, feat_clique.length);
                Arrays.sort(clique);
                MakeGraph(clique);
                ArrayList<Integer> cliquevars = new ArrayList<Integer>(Arrays.asList(clique));
                ArrayList<Integer> assignmenttovars = new ArrayList<Integer>();
                for (Integer varofclique : cliquevars) {
                    assignmenttovars.add(labels[varofclique]);
                }
                //Integer features[] = new Integer[featcount];
                //Arrays.fill(features, 0);
                if (cliquefeaturevaluemap.containsKey(cliquevars)) {
                    HashMap<ArrayList<Integer>, ArrayList<Integer>> featurevaluemap = cliquefeaturevaluemap.get(cliquevars);
                    if (featurevaluemap.containsKey(assignmenttovars)) {
                        ArrayList<Integer> featurelist = featurevaluemap.get(assignmenttovars);
                        if (!featurelist.contains(featureno)) {
                            featurelist.add(featureno);
                        }
                        featurevaluemap.put(assignmenttovars, featurelist);

                    } else {
                        ArrayList<Integer> featurelist = new ArrayList<Integer>();
                        featurelist.add(featureno);
                        featurevaluemap.put(assignmenttovars, featurelist);
                    }


                    cliquefeaturevaluemap.put(cliquevars, featurevaluemap);
                } else {
                    HashMap<ArrayList<Integer>, ArrayList<Integer>> featurevaluemap = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();
                    ArrayList<Integer> featurelist = new ArrayList<Integer>();
                    featurelist.add(featureno);
                    featurevaluemap.put(assignmenttovars, featurelist);
                    cliquefeaturevaluemap.put(cliquevars, featurevaluemap);

                }
                // features[featureno] = 1;
                featurecount[featureno] += 1;

//                String cliquestring = Arrays.toString(clique);
//                if (cliques.containsKey(cliquestring)) {
//                    features = cliques.get(cliquestring);
//                    Arrays.fill(features, 0);
//                    features[featureno] = 1;
//                    cliques.put(cliquestring, features);
//                } else {
//                    features = new Integer[numfeatcount];
//                    Arrays.fill(features, 0);
//                    features[featureno] = 1;
//                    cliques.put(cliquestring, features);
//                }




            }
//                for (String cl : cliques.keySet()) {
//                    System.out.println("clique " + cl + " " + Arrays.toString(cliques.get(cl)));
//                }


        }
        System.out.println(cliquefeaturevaluemap);


        System.out.println("Feature Count");
        System.out.println(Arrays.asList(featurecount));
        System.out.println("Graph Adjacency List");
        showgraph();


    }

    public void constructpottable() {
        fullpot = new HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Double>>();
        for (ArrayList<Integer> clique : cliquefeaturevaluemap.keySet()) {
            inits = new ArrayList<ArrayList<Integer>>();
            curr = new ArrayList<Integer>();
            ArrayList<Integer> airs = new ArrayList<Integer>();
            for (int k1 = 0; k1 < clique.size(); k1++) {
                airs.add(varairity[clique.get(k1)]);
            }
            genperms(clique, airs);
            HashMap<ArrayList<Integer>, ArrayList<Integer>> featurevaluemap = cliquefeaturevaluemap.get(clique);
            for (ArrayList<Integer> assnment : featurevaluemap.keySet()) {
                ArrayList<Integer> feats = featurevaluemap.get(assnment);
                Integer features[] = new Integer[featcount];
                Arrays.fill(features, 0);
                for (Integer k : feats) {
                    features[k] = 1;
                }
                Double potval = Math.pow(Math.E, dotprod(features, weights));
                if (fullpot.containsKey(clique)) {
                    indpot = fullpot.get(clique);
                    if (!indpot.containsKey(assnment)) {
                        indpot.put(assnment, potval);
                    }
                    fullpot.put(clique, indpot);
                } else {
                    indpot = new HashMap<ArrayList<Integer>, Double>();
                    indpot.put(assnment, potval);
                    fullpot.put(clique, indpot);
                }
            }
            for (ArrayList<Integer> remassn : inits) {
                if (fullpot.containsKey(clique)) {
                    indpot = fullpot.get(clique);
                    if (!indpot.containsKey(remassn)) {
                        indpot.put(remassn, 1.0);
                    }
                    fullpot.put(clique, indpot);
                } else {
                    indpot = new HashMap<ArrayList<Integer>, Double>();
                    indpot.put(remassn, 1.0);
                    fullpot.put(clique, indpot);
                }
            }
            //System.out.println(inits);

        }
    }

    public Double[] CalcGradient() {
        Double featexpcount[] = new Double[featcount];
        Double grad[] = new Double[featcount];
        Arrays.fill(grad, 0.0);

        Arrays.fill(featexpcount, 0.0);
        for (ArrayList<Integer> clique : cliquefeaturevaluemap.keySet()) {
            HashMap<ArrayList<Integer>, ArrayList<Integer>> featurevaluemap = cliquefeaturevaluemap.get(clique);
            HashMap<ArrayList<Integer>, Double> marginaloverclique;
            //marginaloverclique=infer.infer(clique); // For each feature do: Find clique it appears in. For each value in clique it fires get prob value. Here the whole Probability table for the clique is in the vairable.
            marginaloverclique = jt.marginalQuery(clique).get(clique);
            for (ArrayList<Integer> assnment : featurevaluemap.keySet()) {
                ArrayList<Integer> feats = featurevaluemap.get(assnment);
                for (Integer k : feats) {
                    featexpcount[k] = featexpcount[k] + marginaloverclique.get(assnment);
                }
            }
        }
        for (int i = 0; i < featcount; i++) {
            grad[i] = featurecount[i] - featexpcount[i] * recs;
        }
        return grad;
    }

    public Double dotprod(Integer[] a, Double[] b) {
        Double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    public void Calculate_CPTS1() { // This is where I call Sharnagat's code.
        udg = new UndirectedGraph();
        udg.setGraphMatrix(graphmatrix, vars);
        udg.setMaxValues(varairity);
        udg.triangulate();
        jt = new JunctionTree();
        jt.makeJunctionTree(udg.getGraph(), udg.getMaxValues(), fullpot);


    }

    public void Calculate_CPTS() { // This is where I call Sharnagat's code.
        mparoutine = new Data();
        mparoutine.adj_mat = graphmatrix; //Set Graph
        mparoutine.phi = fullpot; // Set full POT table
        mparoutine.num_nodes = vars; //Num of Variables
        mparoutine.num_edges = getedgecount();//Num of edges
        mparoutine.node_vals = varairity; // Airity of each vairable in an array
        //mparoutine.printAdjMat();
        algo = new MPA();
        algo.setData(mparoutine); //Init MPA routine
        algo.triangulate(); // Triangulate
        algo.getCliqueTree(); //Calc Clique tree for iter
        //algo.printJTInfo();
        infer = new Inference(algo.getJunction_tree(), mparoutine);
        infer.computeMsgs();//Do MP

    }

    public void readdata() throws IOException {
        datafile = "C:\\Users\\RAJ\\Downloads\\Compressed\\11305R001_11305R003\\11305R001_11305R003\\HW4\\learning_input2";
        BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));

        String line = "";
        line = br.readLine().trim();
        recs = Integer.parseInt(line);
        line = br.readLine().trim();
        vars = Integer.parseInt(line);
        line = br.readLine().trim();
        featcount = Integer.parseInt(line);
        varairity = new int[vars];
        gradient = new Double[featcount];
        weights = new Double[featcount];
        Arrays.fill(gradient, 0.0);
        Arrays.fill(weights, 0.0);
        Arrays.fill(varairity, 0);
        graphmatrix = new int[vars][vars];
        for (int i = 0; i < vars; i++) {
            line = br.readLine().trim();
            int airity = Integer.parseInt(line.trim());
            varairity[i] = airity;
        }

        for (int i = 0; i < recs; i++) {
            ArrayList<Integer[]> currdata = new ArrayList<Integer[]>();
            line = br.readLine().trim();

            String temp[] = line.split(" ");

            Integer varlabels[] = new Integer[temp.length];
            for (int j = 0; j < temp.length; j++) {
                varlabels[j] = Integer.parseInt(temp[j]);
            }
            currdata.add(varlabels);
            line = br.readLine().trim();
            int featurerecs = Integer.parseInt(line);
            for (int j = 0; j < featurerecs; j++) {
                line = br.readLine();
                temp = line.trim().split(" ");
                int featureind = Integer.parseInt(temp[0]);
                String toconnect[] = temp[1].split(",");
                Integer varsaffected[] = new Integer[toconnect.length + 1];
                varsaffected[0] = featureind;
                for (int l1 = 0; l1 < toconnect.length; l1++) {
                    varsaffected[l1 + 1] = Integer.parseInt(toconnect[l1]);
                }
                currdata.add(varsaffected);
            }
            data.add(currdata);
        }
    }

    public void showdata(ArrayList<ArrayList<int[]>> data) {
        for (ArrayList<int[]> s : data) {
            for (int[] a : s) {
                for (int j : a) {
                    System.out.print(j + " ");
                }
                System.out.println("");
            }
        }
    }

    public Double[] UpdateWeight() {
        Double newweights[] = new Double[featcount];
        for (int i = 0; i < featcount; i++) {
            newweights[i] = weights[i] + (learningrate * gradient[i]);
        }
        return newweights;
    }

    public void MakeGraph(Integer[] clique) {
        ArrayList<Integer> cliquedata = new ArrayList<Integer>(Arrays.asList(clique));

        for (Integer i : cliquedata) {
            for (Integer j : cliquedata) {
                if (j == i) {
                    continue;
                } else {

                    graphmatrix[i][j] = 1;
                    graphmatrix[j][i] = 1;
                    if (graph.containsKey(i)) {
                        ArrayList<Integer> neighbors = graph.get(i);
                        if (!neighbors.contains(j)) {
                            neighbors.add(j);
                        }
                        graph.put(i, neighbors);
                    } else {
                        ArrayList<Integer> neighbors = new ArrayList<Integer>();
                        neighbors.add(j);
                        graph.put(i, neighbors);
                    }

                }
            }

        }
    }
}
//        for (int i = 0; i < featcount; i++) {
//            System.out.println("Feature " + i + " " + featurecount[i]);
//        }
//        for (int i[] : featurerecords) {
//            String temp = "";
//            for (int j : i) {
//                temp = temp + String.valueOf(j) + " ";
//            }
//            System.out.println(temp);
//        }
//        double w[] = new double[featcount];
//        for (int m = 0; m < w.length; m++) {
//            w[m] = Math.random();
//        }
//        for(int i=0;i<vars;i++){
//            for(int j=0;j<vars;j++){
//                System.out.print(graph[i][j]+" ");
//            }
//            System.out.println("");
//        }
//        QuadraticFun fun = new QuadraticFun();
//        Minimizer alg = new Minimizer();
//        ArrayList bounds = new ArrayList();
//        bounds.add(new Bound(new double(10D), null));
//        alg.setBounds(bounds); Result result = alg.run(fun, new double[] { 40D });
//        System.out.println((new StringBuilder()).append("The final result: ").append(result).toString());
//                for(String s1: toconnect){
//                    for(String s2:toconnect){
//                        if(!s1.equals(s2)){
//                            graph[Integer.parseInt(s1)][Integer.parseInt(s2)]=1;
//                        }
//                    }
//                }

