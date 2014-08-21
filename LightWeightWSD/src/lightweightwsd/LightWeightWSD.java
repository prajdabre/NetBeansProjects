/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweightwsd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import iitb.cfilt.cpost.ConfigReader;
import iitb.cfilt.cpost.dmstemmer.MAResult;
import iitb.cfilt.cpost.dmstemmer.MorphOutput2;
import iitb.cfilt.cpost.dmstemmer.NewStemmer;

import iitb.cfilt.cpost.dmstemmer.MorphResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.rmi.CORBA.Util;
import java.util.Date;

/**
 *
 * @author raj
 */
public class LightWeightWSD {

    private NewStemmer ns;
    private String word;
    private MAResult mar;
    private int size;
    public BufferedWriter error_logger;
    public BufferedWriter match_logger;
    public BufferedWriter mismatch_logger;
    public BufferedWriter accuracies_logger;
    public BufferedWriter zero_tagged_logger;
    int correctsensetag = 0;
    int totalwords = 0;
    int correctverb = 0;
    int totalverb = 0;
    int correctnoun = 0;
    int totalnoun = 0;
    int correctadverb = 0;
    int totaladverb = 0;
    int correctadjective = 0;
    int totaladjective = 0;

    /**
     * @param args the command line arguments
     */
    /**
     * This method initializes the morph analyzer (bahuguna's)
     */
    public void initialisation() {
        try {
            ConfigReader.read("/home/raj/NetBeansProjects/HinMA/HindiLinguisticResources/hindiConfig1");
            ns = new NewStemmer();
            Date d = new Date();
            error_logger = new BufferedWriter(new FileWriter("LOGS/Error_Log " + d.toString(), true));

            error_logger.write(d.toString() + "\n");
            error_logger.flush();

            match_logger = new BufferedWriter(new FileWriter("MATCH/Match_Log " + d.toString(), true));
            match_logger.write(d.toString() + "\n");
            match_logger.flush();

            mismatch_logger = new BufferedWriter(new FileWriter("MISMATCH/Mismatch_Log " + d.toString(), true));
            mismatch_logger.write(d.toString() + "\n");
            mismatch_logger.flush();

            accuracies_logger = new BufferedWriter(new FileWriter("SCORES/Accuracies_Log " + d.toString(), true));
            accuracies_logger.write(d.toString() + "\n");
            accuracies_logger.flush();

            zero_tagged_logger = new BufferedWriter(new FileWriter("ZEROTAGGED/Zeroes_Log " + d.toString(), true));
            zero_tagged_logger.write(d.toString() + "\n");
            zero_tagged_logger.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method does a background run of the postagger of hindi
     * Right not we are not using manshri's pos tagger
     * Please replace it if you need
     * Just change the bash file param and make sure that the output is in a file called infile.hi 
     */
    public void initial_processing(String fname) {
        try {
            System.out.println("Tagging and Morphing Data");
            String base = "/home/raj/NetBeansProjects/LightWeightWSD";
            Runtime r = Runtime.getRuntime();
            String[] cmd = {
                "/bin/sh",
                base + "/hindi-pos-tagger-2.0/run_hin_tagger.sh", fname
            };
            Process p = r.exec(cmd);
            p.waitFor();
//            BufferedReader b = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//            String line = "";
//
//            while ((line = b.readLine()) != null) {
//                System.out.println(line);
//            }

            Files.pos_tagged = new BufferedReader(new FileReader(fname + ".hi"));
            Files.ps_wsd = new PrintStream(new FileOutputStream(base + "/HIN-TOURISM-RESULTS/" + (new File(fname)).getName() + ".sensetagged"), true, "UTF8");
        } catch (InterruptedException ex) {
            Logger.getLogger(LightWeightWSD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LightWeightWSD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method does basic overlap based wsd.
     * For each word: 
     * 1. Do morph using bahuguna's analyzer and get root
     * 2. if it is a verb then add "na" to make it in base form
     * 3. for each sense in the clue word list for the current root:
     *      3a. Get the overlap of the word's context with the current clues list
     *      3b. If this overlap is bigger then this is the best sense so far
     * 4. Repost the sense with max overlap
     */
    public void perform_wsd() throws IOException {
        try {
            String line = "";


            while ((line = Files.pos_tagged.readLine()) != null) {
                // System.out.println(line);

                for (String part : line.split(" ")) {

                    Set<String> context = new HashSet<String>(Arrays.asList(line.split(" ")));
                    context.remove(part);
                    String word = part.split("\\|\\|\\|")[0];
                    String root = part.split("\\|\\|\\|")[1];
                    String pos = part.split("\\|\\|\\|")[2];
                    Files.ps_wsd.print(word + "#" + root + "#" + pos);
                    if (!pos.equals("NN") && !pos.equals("NNP") && !pos.equals("VM") && !pos.equals("VAUX") && !pos.equals("JJ") && !pos.equals("JJP") && !pos.equals("RB")) { //modified
                        Files.ps_wsd.print("#0 ");
                        continue;
                    }

                    if ((pos.equals("VM") || pos.equals("VAUX"))) {
                        root += "ना";
                    }

                    mar = ns.stem(word);


                    size = mar.getSize();
                    //String rootsCat[] = new String[size];
                    if (size == 0) {
                        Files.ps_wsd.print("#0 ");
                        continue;
                    }
                    Vector<MorphOutput2> x = mar.getMorphOutputs();
                    for (int k = 0; k < size; k++) {
                        root = x.get(k).getRoot();
                        //System.out.println(root);
                        if ((pos.contains("NN")) && (x.get(k).getStemmerResult().getCategory().equals("noun") || x.get(k).getStemmerResult().getCategory().equals("proper noun"))) {
                            break;
                        } else if ((pos.equals("VM") && x.get(k).getStemmerResult().getCategory().equals("verb")) || (pos.equals("VAUX") && x.get(k).getStemmerResult().getCategory().equals("verb_aux"))) {
                            root += "ना";
                            break;
                        }
                        //rootsCat[k] = x.get(k).getRoot();

                        //rootsCat[k] = rootsCat[k] + "/" + x.get(k).getStemmerResult().getCategory();
                    }

                    if (DataStructures.word_id_clues.containsKey(root)) {

                        HashMap<String, Set<String>> temp = DataStructures.word_id_clues.get(root);
                        String winsense = "";
                        int maxoverlapcount = -1;
                        Set<String> tempcontext = new HashSet<String>(context);
                        for (String key : temp.keySet()) {
                            tempcontext.retainAll(temp.get(key));
                            if (tempcontext.size() > maxoverlapcount) {
                                winsense = key;
                                maxoverlapcount = tempcontext.size();
                            }
                        }
                        Files.ps_wsd.print("#" + winsense + " ");
                    } else {
                        Files.ps_wsd.print("#- ");
                    }


                }
                Files.ps_wsd.println();
                Files.ps_wsd.flush();
            }
        } catch (IOException ex) {
            //Logger.getLogger(LightWeightWSD.class.getName()).log(Level.SEVERE, null, ex);
        }
        Files.ps_wsd.close();
        Files.pos_tagged.close();
    }

    public void perform_wsd(File fname) throws IOException {
        zero_tagged_logger.write("File " + fname.getName() + "\n");
        BufferedReader br = new BufferedReader(new FileReader(Files.base + "/HIN-TOURISM-TEST/" + fname.getName().split("\\.")[0] + ".txt.withoutctx"));
        Files.ps_wsd = new PrintStream(new FileOutputStream(Files.base + "/HIN-TOURISM-RESULTS/" + fname.getName() + ".sensetagged"), true, "UTF8");
        String line = "";
        int j = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String linecomponents[] = line.split(" ");
            String contextfull[] = new String[linecomponents.length * 3];
            int i = 0;
            for (String component : linecomponents) {
                String[] parts = component.split("#");
                String root = parts[1];
                if (root.contains("{")) {
                    String roots[] = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",");
                    for (String s : roots) {
                        contextfull[i] = s;
                        i++;
                    }
                } else {
                    contextfull[i] = parts[0];
                    i++;
                }

            }
            j++;
            for (String component : linecomponents) {
                Set<String> context = new HashSet<String>(Arrays.asList(contextfull));
                //System.out.println(context.size());
                //context = new HashSet<String>();
                String[] parts = component.split("#");
                String word = parts[0];
                String root = parts[1];
                String pos = parts[2];
                String wordno = parts[3];
                String goldsensetag = parts[4];

                context.remove(word);
                //System.out.println(context);
                //System.exit(1);
                if (goldsensetag.equals("0")) {
                    Files.ps_wsd.print(component + " ");
                } else {
                    if (root.contains("{")) {
                        String roots[] = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",");
                        int inner_done = 0;
                        for (String root_inner : roots) {
                            if (DataStructures.word_id_clues.containsKey(root_inner + "_" + pos)) {
                                //System.out.println("Here");
                                HashMap<String, Set<String>> temp = DataStructures.word_id_clues.get(root_inner + "_" + pos);
                                if (temp.size() == 1) {
                                    Files.ps_wsd.print(word + "#" + root_inner + "#" + pos + "#" + wordno + "#" + temp.entrySet().iterator().next().getKey() + " ");
                                    inner_done = 1;
                                    break;
                                } else {
                                    String winsense = "";
                                    int maxoverlapcount = -1;

                                    for (String key : temp.keySet()) {
                                        Set<String> tempcontext = new HashSet<String>(context);


                                        tempcontext.retainAll(temp.get(key));
//                                if(tempcontext.size()>0){
//                                    System.out.println("Here");
//                                    System.out.println(tempcontext.size());
//                                }
                                        if (tempcontext.size() > maxoverlapcount) {
                                            //System.out.print("Here ");
                                            winsense = key;
                                            maxoverlapcount = tempcontext.size();
                                        }

                                    }
                                    //System.out.println("");
                                    Files.ps_wsd.print(word + "#" + root_inner + "#" + pos + "#" + wordno + "#" + winsense + " ");
                                    inner_done = 1;
                                    break;
                                }
                            }
                        }

                        if (inner_done == 0) {

                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + "0 ");
                            zero_tagged_logger.write("Line number: " + j + ": " + component + "\n");
                            zero_tagged_logger.flush();


                        }
                    } else {
                        if (DataStructures.word_id_clues.containsKey(root + "_" + pos)) {
                            //System.out.println("Here");
                            HashMap<String, Set<String>> temp = DataStructures.word_id_clues.get(root + "_" + pos);
                            if (temp.size() == 1) {
                                Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + temp.entrySet().iterator().next().getKey() + " ");

                            } else {
                                String winsense = "";
                                int maxoverlapcount = -1;

                                for (String key : temp.keySet()) {
                                    Set<String> tempcontext = new HashSet<String>(context);


                                    tempcontext.retainAll(temp.get(key));
//                                if(tempcontext.size()>0){
//                                    System.out.println("Here");
//                                    System.out.println(tempcontext.size());
//                                }     

                                    if (tempcontext.size() > maxoverlapcount) {
                                        //System.out.print("Here ");
                                        winsense = key;
                                        maxoverlapcount = tempcontext.size();
                                    }

                                }
                                //System.out.println("");
                                Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + winsense + " ");

                            }
                        } else {
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + "0 ");
                            zero_tagged_logger.write("Line number: " + j + ": " + component + "\n");
                            zero_tagged_logger.flush();
                        }
                    }


                }

            }
            Files.ps_wsd.print("\n");
        }
        Files.ps_wsd.close();
    }

    public void perform_wsd_wfs(File fname) throws IOException {
        zero_tagged_logger.write("File " + fname.getName() + "\n");
        BufferedReader br = new BufferedReader(new FileReader(Files.base + "/HIN-TOURISM-TEST/" + fname.getName().split("\\.")[0] + ".txt.withoutctx"));
        Files.ps_wsd = new PrintStream(new FileOutputStream(Files.base + "/HIN-TOURISM-RESULTS/" + fname.getName() + ".sensetagged"), true, "UTF8");
        String line = "";
        int j = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String linecomponents[] = line.split(" ");
            String contextfull[] = new String[linecomponents.length * 3];
            int i = 0;
            for (String component : linecomponents) {
                String[] parts = component.split("#");
                String root = parts[1];
                if (root.contains("@{")) {
                    String roots[] = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",");
                    for (String s : roots) {
                        contextfull[i] = s;
                        i++;
                    }
                } else {
                    contextfull[i] = parts[0];
                    i++;
                }

            }
            j++;
            for (String component : linecomponents) {
                Set<String> context = new HashSet<String>(Arrays.asList(contextfull));
                //System.out.println(context.size());
                context = new HashSet<String>();
                String[] parts = component.split("#");
                String word = parts[0];
                String root = parts[1];
                String pos = parts[2];
                String wordno = parts[3];
                String goldsensetag = parts[4];

                context.remove(word);
                //System.out.println(context);
                //System.exit(1);
                if (goldsensetag.equals("0")) {
                    Files.ps_wsd.print(component + " ");
                } else {
                    if (root.contains("{$")) {
                        String roots[] = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",");
                        int inner_done = 0;
                        for (String root_inner : roots) {
                            if (DataStructures.word_id_clues.containsKey(root_inner + "_" + pos)) {
                                //System.out.println("Here");
                                HashMap<String, Set<String>> temp = DataStructures.word_id_clues.get(root_inner + "_" + pos);
                                if (temp.size() == 1) {
                                    Files.ps_wsd.print(word + "#" + root_inner + "#" + pos + "#" + wordno + "#" + temp.entrySet().iterator().next().getKey() + " ");
                                    inner_done = 1;
                                    break;
                                } else {
                                    String winsense = "";
                                    int maxoverlapcount = -1;

                                    Set<String> all_sids = temp.keySet();
                                    ArrayList<Integer> sids = new ArrayList<Integer>();
                                    for (String s11 : all_sids) {
                                        sids.add(Integer.parseInt(s11));
                                    }
                                    Collections.sort(sids);

                                    Files.ps_wsd.print(word + "#" + root_inner + "#" + pos + "#" + wordno + "#" + sids.get(0) + " ");
                                    inner_done = 1;
                                    break;
                                }
                            }
                        }

                        if (inner_done == 0) {

                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + "0 ");
                            zero_tagged_logger.write("Line number: " + j + ": " + component + "\n");
                            zero_tagged_logger.flush();


                        }
                    } else {
                        if (DataStructures.word_id_clues.containsKey(root + "_" + pos)) {
                            //System.out.println("Here");
                            HashMap<String, Set<String>> temp = DataStructures.word_id_clues.get(root + "_" + pos);
                            if (temp.size() == 1) {
                                Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + temp.entrySet().iterator().next().getKey() + " ");

                            } else {
                                String winsense = "";
                                int maxoverlapcount = -1;

                                Set<String> all_sids = temp.keySet();
                                ArrayList<Integer> sids = new ArrayList<Integer>();
                                for (String s11 : all_sids) {
                                    sids.add(Integer.parseInt(s11));
                                }
                                Collections.sort(sids);

                                
                                //System.out.println("");
                                Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + sids.get(0) + " ");

                            }
                        } else {
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + "0 ");
                            zero_tagged_logger.write("Line number: " + j + ": " + component + "\n");
                            zero_tagged_logger.flush();
                        }
                    }


                }

            }
            Files.ps_wsd.print("\n");
        }
        Files.ps_wsd.close();
    }

    public void perform_wsd_morph(File fname) throws IOException {
        zero_tagged_logger.write("File " + fname.getName() + "\n");
        BufferedReader br = new BufferedReader(new FileReader(Files.base + "/HIN-TOURISM-TEST/" + fname.getName().split("\\.")[0] + ".txt.withoutctx"));
        Files.ps_wsd = new PrintStream(new FileOutputStream(Files.base + "/HIN-TOURISM-RESULTS/" + fname.getName() + ".sensetagged"), true, "UTF8");
        String line = "";
        int j = 0;
        while ((line = br.readLine()) != null) {
            System.out.println("Line number: " + (j + 1));
            line = line.trim();
            String linecomponents[] = line.split(" ");
            String contextfull[] = new String[linecomponents.length * 3];
            int i = 0;
            for (String component : linecomponents) {
                String[] parts = component.split("#");
                String root = parts[1];
                if (root.contains("{")) {
                    String roots[] = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",");
                    for (String s : roots) {
                        contextfull[i] = s;
                        i++;
                    }
                } else {
                    contextfull[i] = word;
                    i++;
                }

            }
            j++;
            for (String component : linecomponents) {
                Set<String> context = new HashSet<String>(Arrays.asList(contextfull));
                //System.out.println(context.size());
                //context = new HashSet<String>();
                String[] parts = component.split("#");
                String word = parts[0];
                String root = parts[1];
                String pos = parts[2];
                String wordno = parts[3];
                String goldsensetag = parts[4];
                context.remove(word);
                //System.out.println(context);
                //System.exit(1);
                if (goldsensetag.equals("0")) {
                    Files.ps_wsd.print(component + " ");
                } else {
                    if (root.contains("{")) {
                        root = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",")[0];
                    }
                    if (DataStructures.word_id_clues.containsKey(root + "_" + pos)) {
                        //System.out.println("Here");
                        HashMap<String, Set<String>> temp = DataStructures.word_id_clues.get(root + "_" + pos);
                        if (temp.size() == 1) {
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + temp.entrySet().iterator().next().getKey() + " ");
                        } else {
                            String winsense = "";
                            int maxoverlapcount = -1;

                            for (String key : temp.keySet()) {
                                Set<String> tempcontext = new HashSet<String>(context);

                                Set<String> morphed_clues = new HashSet<String>();
                                for (String clue : temp.get(key)) {
                                    mar = ns.stem(clue);


                                    size = mar.getSize();
                                    //String rootsCat[] = new String[size];
                                    if (size == 0) {
                                        morphed_clues.add(clue);
                                        continue;
                                    }

                                    Vector<MorphOutput2> x = mar.getMorphOutputs();
                                    for (int k = 0; k < size; k++) {
                                        morphed_clues.add(x.get(k).getRoot());
                                    }
                                }

                                tempcontext.retainAll(morphed_clues);
//                                if(tempcontext.size()>0){
//                                    System.out.println("Here");
//                                    System.out.println(tempcontext.size());
//                                }
                                if (tempcontext.size() > maxoverlapcount) {
                                    //System.out.print("Here ");
                                    winsense = key;
                                    maxoverlapcount = tempcontext.size();
                                }

                            }
                            //System.out.println("");
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + winsense + " ");
                        }
                    } else {
                        Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + "0 ");
                        zero_tagged_logger.write("Line number: " + j + ": " + component + "\n");
                        zero_tagged_logger.flush();
                    }

                }

            }
            Files.ps_wsd.print("\n");
        }
        Files.ps_wsd.close();
    }

    public void perform_wsd_with_heuristics(File fname, int which) throws IOException {
        zero_tagged_logger.write("File " + fname.getName() + "\n");
        BufferedReader br = new BufferedReader(new FileReader(Files.base + "/HIN-TOURISM-TEST/" + fname.getName().split("\\.")[0] + ".txt.withoutctx"));
        Files.ps_wsd = new PrintStream(new FileOutputStream(Files.base + "/HIN-TOURISM-RESULTS/" + fname.getName() + ".sensetagged"), true, "UTF8");
        String line = "";
        int j = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String linecomponents[] = line.split(" ");
            String contextfull[] = new String[linecomponents.length];
            int i = 0;
            for (String component : linecomponents) {
                String[] parts = component.split("#");
                String word = parts[0];
                contextfull[i] = word;
                i++;
            }
            j++;
            //System.out.println("Context size:"+contextfull.length);
            for (String component : linecomponents) {
                Set<String> context = new HashSet<String>(Arrays.asList(contextfull));
                //System.out.println(context);
                //context = new HashSet<String>();

                String[] parts = component.split("#");
                String word = parts[0];
                String root = parts[1];
                String pos = parts[2];
                String wordno = parts[3];
                String goldsensetag = parts[4];
                context.remove(word);
                //System.out.println(context);
                //System.exit(1);
                if (goldsensetag.equals("0")) {
                    Files.ps_wsd.print(component + " ");
                } else {
                    if (root.contains("{")) {
                        root = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",")[0];
                    }
                    if (DataStructures.word_id_clues_with_heuristics.containsKey(root + "_" + pos)) {
                        HashMap<String, HashMap<String, double[]>> temp = DataStructures.word_id_clues_with_heuristics.get(root + "_" + pos);
                        if (temp.size() == 1) {
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + temp.entrySet().iterator().next().getKey() + " ");
                        } else {
                            String winsense = "";
                            double max_pmi_tot = -1;

                            for (String key : temp.keySet()) {
                                if (max_pmi_tot == -1) {
                                    winsense = key;
                                }
                                //System.out.println("here");
                                Set<String> tempcontext = new HashSet<String>(context);
                                HashMap<String, double[]> clues_heur = temp.get(key);
                                tempcontext.retainAll(clues_heur.keySet());
//                                if(tempcontext.size()>0){
//                                    System.out.println("Clues overlapped: "+tempcontext.size());
//                                    //System.out.println(tempcontext.size());
//                                }

                                if (tempcontext.size() > 0) {
                                    double curr_pmi_tot = 0;
                                    for (String overlap_clue : tempcontext) {
                                        double pmi_norm = clues_heur.get(overlap_clue)[3];
                                        if (pmi_norm == -1) {
                                            pmi_norm = 0.5;
                                        }
                                        curr_pmi_tot += pmi_norm;
                                    }
                                    if (curr_pmi_tot > max_pmi_tot) {
                                        winsense = key;
                                        max_pmi_tot = curr_pmi_tot;
                                    }
                                }
//                                if (tempcontext.size() > maxoverlapcount) {
//                                    //System.out.print("Here ");
//                                    winsense = key;
//                                    maxoverlapcount = tempcontext.size();
//                                }

                            }
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + winsense + " ");
                            //System.out.println("");

                        }
                    } else {
                        Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + "0 ");
                        zero_tagged_logger.write("Line number: " + j + ": " + component + "\n");
                        zero_tagged_logger.flush();
                    }

                }

            }
            Files.ps_wsd.print("\n");
        }
        Files.ps_wsd.close();
    }

    public void perform_wsd_with_overlapped_clues(File fname, int which) throws IOException {
        zero_tagged_logger.write("File " + fname.getName() + "\n");
        BufferedReader br = new BufferedReader(new FileReader(Files.base + "/HIN-TOURISM-TEST/" + fname.getName().split("\\.")[0] + ".txt.withoutctx"));
        Files.ps_wsd = new PrintStream(new FileOutputStream(Files.base + "/HIN-TOURISM-RESULTS/" + fname.getName() + ".sensetagged"), true, "UTF8");
        String line = "";
        int j = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String linecomponents[] = line.split(" ");
            String contextfull[] = new String[linecomponents.length];
            int i = 0;
            for (String component : linecomponents) {
                String[] parts = component.split("#");
                String word = parts[0];
                contextfull[i] = word;
                i++;
            }
            j++;
            //System.out.println("Context size:"+contextfull.length);
            for (String component : linecomponents) {
                Set<String> context = new HashSet<String>(Arrays.asList(contextfull));
                //System.out.println(context);
                //context = new HashSet<String>();

                String[] parts = component.split("#");
                String word = parts[0];
                String root = parts[1];
                String pos = parts[2];
                String wordno = parts[3];
                String goldsensetag = parts[4];
                context.remove(word);
                //System.out.println(context);
                //System.exit(1);
                if (goldsensetag.equals("0")) {
                    Files.ps_wsd.print(component + " ");
                } else {
                    if (root.contains("{")) {
                        root = root.replaceAll("\\{", "").replaceAll("\\}", "").split(",")[0];
                    }
                    if (DataStructures.word_id_clues_with_overlapped_clues.containsKey(root + "_" + pos)) {
                        HashMap<String, HashMap<String, Integer>> temp = DataStructures.word_id_clues_with_overlapped_clues.get(root + "_" + pos);
                        if (temp.size() == 1) {
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + temp.entrySet().iterator().next().getKey() + " ");
                        } else {
                            String winsense = "";

                            int max_score = -1;
                            for (String key : temp.keySet()) {

                                //System.out.println("here");
                                Set<String> tempcontext = new HashSet<String>(context);
                                //System.out.println(key);
                                HashMap<String, Integer> clues_heur = temp.get(key);
                                //System.out.println(clues_heur);
                                tempcontext.retainAll(clues_heur.keySet());
//                                if(tempcontext.size()>0){
//                                    System.out.println("Clues overlapped: "+tempcontext.size());
//                                    //System.out.println(tempcontext.size());
//                                }


                                int curr_score = 0;
                                for (String overlap_clue : tempcontext) {
                                    int weight = clues_heur.get(overlap_clue);

                                    curr_score += weight;
                                }
                                if (max_score < curr_score) {
                                    winsense = key;
                                    max_score = curr_score;
                                }

//                                if (tempcontext.size() > maxoverlapcount) {
//                                    //System.out.print("Here ");
//                                    winsense = key;
//                                    maxoverlapcount = tempcontext.size();
//                                }

                            }
                            Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + winsense + " ");
                            //System.out.println("");

                        }
                    } else {
                        Files.ps_wsd.print(word + "#" + root + "#" + pos + "#" + wordno + "#" + "0 ");
                        zero_tagged_logger.write("Line number: " + j + ": " + component + "\n");
                        zero_tagged_logger.flush();
                    }

                }

            }
            Files.ps_wsd.print("\n");
        }
        Files.ps_wsd.close();
    }

    public void run_over_all() throws IOException {
        String base = Files.base + "/HIN-TOURISM-TEST/";
        File dir = new File(base);
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (f.getName().contains("ctx") || f.getName().contains(".hi") || f.getName().contains("postagged") || f.getName().contains("infile")) {
                    continue;
                }
                //System.out.println("Processing file " + f.getName());
                //initial_processing(base + f.getName());
                //System.out.println("Preprocessed file " + f.getName());
                System.out.println("Performing WSD on file " + f.getName());
                perform_wsd(f);
                //perform_wsd_with_overlapped_clues(f, 1);
                System.out.println("WSD done on file " + f.getName());
                get_accuracies(f);
            }
        }

        error_logger.flush();
        error_logger.close();
        match_logger.write("Total Matching : " + correctsensetag + "\n");
        match_logger.write("Total tokens : " + totalwords + "\n");
        match_logger.write("Accuracy : " + ((1.0 * correctsensetag) / totalwords) + "\n");
        match_logger.write("Noun Accuracy : " + ((1.0 * correctnoun) / totalnoun) + " --- " + correctnoun + " out of " + totalnoun + " cases are tagged correctly\n");
        match_logger.write("Adjective Accuracy : " + ((1.0 * correctadjective) / totaladjective) + " --- " + correctadjective + " out of " + totaladjective + " cases are tagged correctly\n");
        match_logger.write("Adverb Accuracy : " + ((1.0 * correctadverb) / totaladverb) + " --- " + correctadverb + " out of " + totaladverb + " cases are tagged correctly\n");
        match_logger.write("Verb Accuracy : " + ((1.0 * correctverb) / totalverb) + " --- " + correctverb + " out of " + totalverb + " cases are tagged correctly\n\n");
        match_logger.flush();
        match_logger.close();
        mismatch_logger.flush();
        mismatch_logger.close();
        accuracies_logger.write("Total Matching : " + correctsensetag + "\n");
        accuracies_logger.write("Total tokens : " + totalwords + "\n");
        accuracies_logger.write("Accuracy : " + ((1.0 * correctsensetag) / totalwords) + "\n");
        accuracies_logger.write("Noun Accuracy : " + ((1.0 * correctnoun) / totalnoun) + " --- " + correctnoun + " out of " + totalnoun + " cases are tagged correctly\n");
        accuracies_logger.write("Adjective Accuracy : " + ((1.0 * correctadjective) / totaladjective) + " --- " + correctadjective + " out of " + totaladjective + " cases are tagged correctly\n");
        accuracies_logger.write("Adverb Accuracy : " + ((1.0 * correctadverb) / totaladverb) + " --- " + correctadverb + " out of " + totaladverb + " cases are tagged correctly\n");
        accuracies_logger.write("Verb Accuracy : " + ((1.0 * correctverb) / totalverb) + " --- " + correctverb + " out of " + totalverb + " cases are tagged correctly\n\n");
        accuracies_logger.flush();
        accuracies_logger.close();
        zero_tagged_logger.close();
    }

    public void get_accuracies(File file) throws FileNotFoundException, IOException {
        int correctsensetagcurrent = 0;
        int totalwordscurrent = 0;
        int correctverbcurrent = 0;
        int totalverbcurrent = 0;
        int correctnouncurrent = 0;
        int totalnouncurrent = 0;
        int correctadverbcurrent = 0;
        int totaladverbcurrent = 0;
        int correctadjectivecurrent = 0;
        int totaladjectivecurrent = 0;
        error_logger.write("Making accuracies for file " + file.getName() + "\n");
        error_logger.flush();
        match_logger.write("\nMaking accuracies for file " + file.getName() + "\n");
        match_logger.flush();
        mismatch_logger.write("\nMaking accuracies for file " + file.getName() + "\n");
        mismatch_logger.flush();
        accuracies_logger.write("Accuracies for file " + file.getName() + "\n");
        accuracies_logger.flush();
        String resultsbase = Files.base + "/HIN-TOURISM-RESULTS/";
        //System.out.println(file.getName());
        String reffname = file.getName().split("\\.")[0] + ".txt.withoutctx";
        //System.out.println(reffname);
        BufferedReader reference = new BufferedReader(new FileReader(Files.base + "HIN-TOURISM-TEST" + "/" + reffname));
        BufferedReader tagged = new BufferedReader(new FileReader(resultsbase + file.getName().split("\\.")[0] + ".txt.wordsonly.sensetagged"));
        String refline = "";
        String taggedline = "";
        int i = 0;
        while ((refline = reference.readLine()) != null) {
            i++;
            taggedline = tagged.readLine();
            refline = refline.trim();
            taggedline = taggedline.trim();

            String[] reflinecomponents = refline.split(" ");
            String[] taggedlinecomponents = taggedline.split(" ");

            if (reflinecomponents.length == taggedlinecomponents.length) {
                for (int j = 0; j < reflinecomponents.length; j++) {
                    String taggedsensetag = taggedlinecomponents[j].split("#")[taggedlinecomponents[j].split("#").length - 1];
                    String refsensetag = reflinecomponents[j].split("#")[reflinecomponents[j].split("#").length - 1];
                    if (!refsensetag.equals("0")) {

                        if (taggedsensetag.equals(refsensetag) || taggedlinecomponents[j].split("#")[0].equals("में") || taggedlinecomponents[j].split("#")[0].equals("यह")) { //modified
                            correctsensetag++;
                            correctsensetagcurrent++;
                            if (DataStructures.word_id_clues.containsKey(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2])) {
                                match_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + "and the clue set used is:" + DataStructures.word_id_clues.get(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2]).get(taggedlinecomponents[j].split("#")[4]) + " \n");
                            } else {
                                match_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + "and no clues available for this \n");
                            }

                            char pos = reflinecomponents[j].split("#")[2].toCharArray()[0];
                            switch (pos) {
                                case 'a':
                                    correctadjective++;
                                    totaladjective++;
                                    correctadjectivecurrent++;
                                    totaladjectivecurrent++;
                                    break;
                                case 'n':
                                    correctnoun++;
                                    totalnoun++;
                                    correctnouncurrent++;
                                    totalnouncurrent++;
                                    break;
                                case 'v':
                                    correctverb++;
                                    totalverb++;
                                    correctverbcurrent++;
                                    totalverbcurrent++;
                                    break;
                                case 'r':
                                    correctadverb++;
                                    totaladverb++;
                                    correctadverbcurrent++;
                                    totaladverbcurrent++;
                                    break;
                            }
                        } else {
                            if (DataStructures.word_id_clues.containsKey(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2])) {
                                mismatch_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + " and actual tag is " + refsensetag + " and the clue set used is:" + DataStructures.word_id_clues.get(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2]).get(taggedlinecomponents[j].split("#")[4]) + " \n");
                            } else {
                                mismatch_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + " and actual tag is " + refsensetag + " and no clues available for this \n");
                            }
                            char pos = reflinecomponents[j].split("#")[2].toCharArray()[0];
                            switch (pos) {
                                case 'a':

                                    totaladjective++;
                                    totaladjectivecurrent++;
                                    break;
                                case 'n':

                                    totalnoun++;
                                    totalnouncurrent++;
                                    break;
                                case 'v':

                                    totalverb++;
                                    totalverbcurrent++;
                                    break;
                                case 'r':

                                    totaladverb++;
                                    totaladverbcurrent++;
                                    break;
                            }
                        }
                        totalwords++;
                        totalwordscurrent++;
                        match_logger.flush();
                        mismatch_logger.flush();
                    }



                }
            } else {
                error_logger.write("Error! Number of tokens mismatch at line number: " + i);
                error_logger.flush();
            }

        }
        accuracies_logger.write("Total Matching : " + correctsensetagcurrent + "\n");
        accuracies_logger.write("Total tokens : " + totalwordscurrent + "\n");
        accuracies_logger.write("Accuracy : " + ((1.0 * correctsensetagcurrent) / totalwordscurrent) + "\n");
        accuracies_logger.write("Noun Accuracy : " + ((1.0 * correctnouncurrent) / totalnouncurrent) + " --- " + correctnouncurrent + " out of " + totalnouncurrent + " cases are tagged correctly\n");
        accuracies_logger.write("Adjective Accuracy : " + ((1.0 * correctadjectivecurrent) / totaladjectivecurrent) + " --- " + correctadjectivecurrent + " out of " + totaladjectivecurrent + " cases are tagged correctly\n");
        accuracies_logger.write("Adverb Accuracy : " + ((1.0 * correctadverbcurrent) / totaladverbcurrent) + " --- " + correctadverbcurrent + " out of " + totaladverbcurrent + " cases are tagged correctly\n");
        accuracies_logger.write("Verb Accuracy : " + ((1.0 * correctverbcurrent) / totalverbcurrent) + " --- " + correctverbcurrent + " out of " + totalverbcurrent + " cases are tagged correctly\n\n");
        accuracies_logger.flush();

    }

    public void get_accuracies_heuristics(File file) throws FileNotFoundException, IOException {
        int correctsensetagcurrent = 0;
        int totalwordscurrent = 0;
        int correctverbcurrent = 0;
        int totalverbcurrent = 0;
        int correctnouncurrent = 0;
        int totalnouncurrent = 0;
        int correctadverbcurrent = 0;
        int totaladverbcurrent = 0;
        int correctadjectivecurrent = 0;
        int totaladjectivecurrent = 0;
        error_logger.write("Making accuracies for file " + file.getName() + "\n");
        error_logger.flush();
        match_logger.write("\nMaking accuracies for file " + file.getName() + "\n");
        match_logger.flush();
        mismatch_logger.write("\nMaking accuracies for file " + file.getName() + "\n");
        mismatch_logger.flush();
        accuracies_logger.write("Accuracies for file " + file.getName() + "\n");
        accuracies_logger.flush();
        String resultsbase = Files.base + "/HIN-TOURISM-RESULTS/";
        //System.out.println(file.getName());
        String reffname = file.getName().split("\\.")[0] + ".txt.withoutctx";
        //System.out.println(reffname);
        BufferedReader reference = new BufferedReader(new FileReader(Files.base + "HIN-TOURISM-TEST" + "/" + reffname));
        BufferedReader tagged = new BufferedReader(new FileReader(resultsbase + file.getName().split("\\.")[0] + ".txt.wordsonly.sensetagged"));
        String refline = "";
        String taggedline = "";
        int i = 0;
        while ((refline = reference.readLine()) != null) {
            i++;
            taggedline = tagged.readLine();
            refline = refline.trim();
            taggedline = taggedline.trim();

            String[] reflinecomponents = refline.split(" ");
            String[] taggedlinecomponents = taggedline.split(" ");

            if (reflinecomponents.length == taggedlinecomponents.length) {
                for (int j = 0; j < reflinecomponents.length; j++) {
                    String taggedsensetag = taggedlinecomponents[j].split("#")[taggedlinecomponents[j].split("#").length - 1];
                    String refsensetag = reflinecomponents[j].split("#")[reflinecomponents[j].split("#").length - 1];
                    if (!refsensetag.equals("0")) {

                        if (taggedsensetag.equals(refsensetag) || taggedlinecomponents[j].split("#")[0].equals("में") || taggedlinecomponents[j].split("#")[0].equals("यह")) { //modified
                            correctsensetag++;
                            correctsensetagcurrent++;
                            if (DataStructures.word_id_clues_with_heuristics.containsKey(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2])) {
                                match_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + "and the clue set used is:" + DataStructures.word_id_clues_with_heuristics.get(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2]).get(taggedlinecomponents[j].split("#")[4]).keySet() + " \n");
                            } else {
                                match_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + "and no clues available for this \n");
                            }

                            char pos = reflinecomponents[j].split("#")[2].toCharArray()[0];
                            switch (pos) {
                                case 'a':
                                    correctadjective++;
                                    totaladjective++;
                                    correctadjectivecurrent++;
                                    totaladjectivecurrent++;
                                    break;
                                case 'n':
                                    correctnoun++;
                                    totalnoun++;
                                    correctnouncurrent++;
                                    totalnouncurrent++;
                                    break;
                                case 'v':
                                    correctverb++;
                                    totalverb++;
                                    correctverbcurrent++;
                                    totalverbcurrent++;
                                    break;
                                case 'r':
                                    correctadverb++;
                                    totaladverb++;
                                    correctadverbcurrent++;
                                    totaladverbcurrent++;
                                    break;
                            }
                        } else {
                            if (DataStructures.word_id_clues_with_heuristics.containsKey(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2])) {
                                mismatch_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + " and actual tag is " + refsensetag + " and the clue set used is:" + DataStructures.word_id_clues_with_heuristics.get(taggedlinecomponents[j].split("#")[1] + "_" + taggedlinecomponents[j].split("#")[2]).get(taggedlinecomponents[j].split("#")[4]).keySet() + " \n");
                            } else {
                                mismatch_logger.write("Line number: " + i + ": " + taggedlinecomponents[j] + " and actual tag is " + refsensetag + " and no clues available for this \n");
                            }
                            char pos = reflinecomponents[j].split("#")[2].toCharArray()[0];
                            switch (pos) {
                                case 'a':

                                    totaladjective++;
                                    totaladjectivecurrent++;
                                    break;
                                case 'n':

                                    totalnoun++;
                                    totalnouncurrent++;
                                    break;
                                case 'v':

                                    totalverb++;
                                    totalverbcurrent++;
                                    break;
                                case 'r':

                                    totaladverb++;
                                    totaladverbcurrent++;
                                    break;
                            }
                        }
                        totalwords++;
                        totalwordscurrent++;
                        match_logger.flush();
                        mismatch_logger.flush();
                    }



                }
            } else {
                error_logger.write("Error! Number of tokens mismatch at line number: " + i);
                error_logger.flush();
            }

        }
        accuracies_logger.write("Total Matching : " + correctsensetagcurrent + "\n");
        accuracies_logger.write("Total tokens : " + totalwordscurrent + "\n");
        accuracies_logger.write("Accuracy : " + ((1.0 * correctsensetagcurrent) / totalwordscurrent) + "\n");
        accuracies_logger.write("Noun Accuracy : " + ((1.0 * correctnouncurrent) / totalnouncurrent) + " --- " + correctnouncurrent + " out of " + totalnouncurrent + " cases are tagged correctly\n");
        accuracies_logger.write("Adjective Accuracy : " + ((1.0 * correctadjectivecurrent) / totaladjectivecurrent) + " --- " + correctadjectivecurrent + " out of " + totaladjectivecurrent + " cases are tagged correctly\n");
        accuracies_logger.write("Adverb Accuracy : " + ((1.0 * correctadverbcurrent) / totaladverbcurrent) + " --- " + correctadverbcurrent + " out of " + totaladverbcurrent + " cases are tagged correctly\n");
        accuracies_logger.write("Verb Accuracy : " + ((1.0 * correctverbcurrent) / totalverbcurrent) + " --- " + correctverbcurrent + " out of " + totalverbcurrent + " cases are tagged correctly\n\n");
        accuracies_logger.flush();

    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Files.init();
        //DataStructures.load_clues();
        System.out.println("Files Loaded!");
        DataStructures.load_clues();
        System.out.println("Data Structures Loaded");
        //System.out.println(DataStructures.word_id_clues.get("शामिल_a"));
        LightWeightWSD lwsd = new LightWeightWSD();
        lwsd.initialisation();
        lwsd.run_over_all();

    }
}
