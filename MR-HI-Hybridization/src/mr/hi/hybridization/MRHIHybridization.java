/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mr.hi.hybridization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import sanchay.corpus.ssf.SSFProperties;
import sanchay.corpus.ssf.SSFSentence;
import sanchay.corpus.ssf.SSFStory;
import sanchay.corpus.ssf.features.FeatureAttribute;
import sanchay.corpus.ssf.features.FeatureStructure;
import sanchay.corpus.ssf.features.impl.FSProperties;
import sanchay.corpus.ssf.features.impl.FeatureStructuresImpl;
import sanchay.corpus.ssf.impl.SSFStoryImpl;
import sanchay.corpus.ssf.tree.SSFNode;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author raj
 */
public class MRHIHybridization {

    /**
     * @param args the command line arguments
     */
    public HashMap<String, ArrayList<String>> phrase_maps_smt_surface = new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> phrase_maps_smt_suff_split = new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> phrase_maps_smt_suff_split_factored = new HashMap<String, ArrayList<String>>();
    public HashMap<String, String> surface_to_suff_split_maps = new HashMap<String, String>();
    public HashMap<String, String> surface_to_suff_split_factored_maps = new HashMap<String, String>();
    public HashMap<String, String> phrase_maps_rbmt = new HashMap<String, String>();
    public HashMap<String, ArrayList<String>> phrase_maps_hybrid = new HashMap<String, ArrayList<String>>();
    public ArrayList<String> in_phrases_seq = new ArrayList<String>();
    public ArrayList<String> out_phrases_seq = new ArrayList<String>();
    FSProperties fsp = new FSProperties();
    SSFProperties ssfp = new SSFProperties();
    SSFProperties cmlp = new SSFProperties();
    SSFStory story_mar = new SSFStoryImpl();
    SSFStory story_hin = new SSFStoryImpl();
    XmlRpcClient client_surface = new XmlRpcClient();
    XmlRpcClient client_suff_split = new XmlRpcClient();
    XmlRpcClient client_factored = new XmlRpcClient();

    public void init() throws FileNotFoundException, IOException, Exception {

        String SANCHAY_HOME = "Sanchay";

        //logger.debug("SANCHAY_HOME = " + SANCHAY_HOME);
        fsp.read(SANCHAY_HOME + File.separator + "props" + File.separator + "fs-mandatory-attribs.txt",
                SANCHAY_HOME + File.separator + "props" + File.separator + "fs-props.txt", "UTF-8");
        ssfp.read(SANCHAY_HOME + File.separator + "props" + File.separator + "ssf-props.txt", "UTF-8");
        cmlp.read(SANCHAY_HOME + File.separator + "props" + File.separator + "cml-props.txt", "UTF-8");
        FeatureStructuresImpl.setFSProperties(fsp);
        SSFNode.setSSFProperties(ssfp);
        SSFNode.setCMLProperties(cmlp);
        Files.open_files();
        init_xml_rpc();

    }

    public void init_xml_rpc() throws MalformedURLException {
        XmlRpcClientConfigImpl config_surface = new XmlRpcClientConfigImpl();
        config_surface.setServerURL(new URL("http://10.105.19.100:12311/RPC2"));
        client_surface = new XmlRpcClient();
        client_surface.setConfig(config_surface);
        XmlRpcClientConfigImpl config_suff_split = new XmlRpcClientConfigImpl();
        config_suff_split.setServerURL(new URL("http://10.105.19.100:12312/RPC2"));
        client_suff_split = new XmlRpcClient();
        client_suff_split.setConfig(config_suff_split);
        XmlRpcClientConfigImpl config_factored = new XmlRpcClientConfigImpl();
        config_factored.setServerURL(new URL("http://10.105.19.100:12313/RPC2"));
        client_factored = new XmlRpcClient();
        client_factored.setConfig(config_factored);
    }

    public void hybridize() throws Exception {
        story_mar.readFile(Files.marssffile);
        story_hin.readFile(Files.hinssffile);
        int scount_mar = story_mar.countSentences();
        int scount_hin = story_hin.countSentences();
        //System.out.println(scount_mar);
        //System.out.println(scount_hin);
        if (scount_hin != scount_mar) {
            System.err.println("Number of Hindi and Marathi sentences are not equal\n Please check files");
            System.exit(1);
        }

        for (int i = 0; i < scount_mar; i++) {
            System.out.println("Processing sentence "+i);


            SSFSentence sen_mar = story_mar.getSentence(i);
            SSFSentence sen_hin = story_hin.getSentence(i);
            int ccount_mar = sen_mar.getRoot().countChildren();
            int ccount_hin = sen_hin.getRoot().countChildren();
            //System.out.println("Sentence "+i+": Marathi chunks: "+ccount_mar+" Hindi Chunks:"+ ccount_hin);

            if (ccount_hin != ccount_mar) {
                System.err.println("Number of Hindi and Marathi chunks for sentecne " + i + "are not equal\n Please check the sentence");
                continue;
            }

            phrase_maps_smt_surface = new HashMap<String, ArrayList<String>>();
            phrase_maps_smt_suff_split = new HashMap<String, ArrayList<String>>();
            phrase_maps_smt_suff_split_factored = new HashMap<String, ArrayList<String>>();
            surface_to_suff_split_maps = new HashMap<String, String>();
            surface_to_suff_split_factored_maps = new HashMap<String, String>();
            phrase_maps_rbmt = new HashMap<String, String>();
            phrase_maps_hybrid = new HashMap<String, ArrayList<String>>();
            in_phrases_seq = new ArrayList<String>();
            out_phrases_seq = new ArrayList<String>();


            for (int j = 0; j < ccount_mar; j++) {
                SSFNode node_mar = sen_mar.getRoot().getChild(j);
                SSFNode node_hin = sen_hin.getRoot().getChild(j);
                String mar_phrase_surface = "";
                String mar_phrase_suff_split = "";
                String mar_phrase_suff_split_factored = "";
                String hin_phrase_surface = "";

                for (int k = 0; k < node_mar.countChildren(); k++) {
                    SSFNode nodeChild = (SSFNode) node_mar.getChildAt(k);
                    String word = nodeChild.getLexData();
                    String POS = nodeChild.getName();
                    if (!word.equals("") || word != null) {
                        mar_phrase_surface += word + " ";
                        if (nodeChild.getFeatureStructures() != null && nodeChild.getFeatureStructures().getChildCount() > 0) {
                            FeatureAttribute nodefatam = null;
                            FeatureAttribute nodefaword = null;
                            FeatureAttribute nodefacat = null;
                            FeatureAttribute nodefagen = null;
                            FeatureAttribute nodefanum = null;
                            FeatureAttribute nodefaper = null;
                            FeatureAttribute nodefacase = null;
                            String sourceWord = "null";
                            String sourcePos = "null";
                            String sourceTam = "null";
                            String sourceCat = "null";
                            String sourceGen = "null";
                            String sourcePer = "null";
                            String sourceCase = "null";
                            String sourceNum = "null";


                            FeatureStructure nodefs = nodeChild.getFeatureStructures().getAltFSValue(0);
                            nodefaword = nodefs.getAttribute(0);
                            if (nodefaword != null) {
                                sourceWord = nodefaword.getAltValue(0).toString();
                            }

                            nodefatam = nodefs.getAttribute(6);
                            if (nodefatam != null) {
                                sourceTam = nodefatam.getAltValue(0).toString();
                            }
                            nodefacat = nodefs.getAttribute(1);
                            if (nodefacat != null) {
                                sourceCat = nodefacat.getAltValue(0).toString();
                            }
                            nodefagen = nodefs.getAttribute(2);
                            if (nodefagen != null) {
                                sourceGen = nodefagen.getAltValue(0).toString();
                            }
                            nodefanum = nodefs.getAttribute(3);
                            if (nodefanum != null) {
                                sourceNum = nodefanum.getAltValue(0).toString();
                            }
                            nodefaper = nodefs.getAttribute(4);
                            if (nodefaper != null) {
                                sourcePer = nodefaper.getAltValue(0).toString();
                            }
                            nodefacase = nodefs.getAttribute(5);
                            if (nodefacase != null) {
                                sourceCase = nodefacase.getAltValue(0).toString();
                            }

                            word = sourceWord + "|" + POS + "|" + sourceCat + "|" + sourceGen + "|" + sourceNum + "|" + sourcePer + "|" + sourceCase;
                            mar_phrase_suff_split += sourceWord + " ";
                            mar_phrase_suff_split_factored += word + " ";
                            sourceTam = sourceTam.replaceAll("#", " ").replaceAll("_", " ").trim();
                            if (sourceTam.length() != 0 && sourceTam.equals(" ")) {
                                String components[] = sourceTam.split(" ");
                                for (int comp = 0; comp < components.length - 1; comp++) {
                                    mar_phrase_suff_split += components[comp] + " ";
                                    word = components[comp] + "|" + "PSP" + "|" + "suff" + "|" + sourceGen + "|" + sourceNum + "|" + sourcePer + "|" + "d";
                                    mar_phrase_suff_split_factored += word + " ";
                                }
                                mar_phrase_suff_split += components[components.length - 1] + " ";
                                word = components[components.length - 1] + "|" + "PSP" + "|" + "suff" + "|" + sourceGen + "|" + sourceNum + "|" + sourcePer + "|" + "o";
                                mar_phrase_suff_split += word + " ";
                            }

                        }


                    }



                }

                if (!mar_phrase_surface.trim().equals("") && mar_phrase_surface.length() != 0) {
                    in_phrases_seq.add(mar_phrase_surface.trim());
                    surface_to_suff_split_maps.put(mar_phrase_surface.trim(), mar_phrase_suff_split.trim());
                    surface_to_suff_split_factored_maps.put(mar_phrase_surface.trim(), mar_phrase_suff_split_factored.trim());
                }
                for (int k = 0; k < node_hin.countChildren(); k++) {
                    SSFNode nodeChild = (SSFNode) node_hin.getChildAt(k);
                    String word = nodeChild.getLexData();
                    String POS = nodeChild.getName();
                    if (!word.equals("") || word != null) {
                        hin_phrase_surface += word + " ";
                    }

                }
                if (!hin_phrase_surface.trim().equals("") && hin_phrase_surface.length() != 0) {
                    out_phrases_seq.add(hin_phrase_surface.trim());
                    phrase_maps_rbmt.put(mar_phrase_surface.trim(), hin_phrase_surface.trim());
                }

            }
            
            String mar_sent = "";
            String hin_sent = "";
            
            for (int index = 0; index < in_phrases_seq.size(); index++) {
                mar_sent += in_phrases_seq.get(index) + " ";
            }
            for (int index = 0; index < out_phrases_seq.size(); index++) {
                hin_sent += out_phrases_seq.get(index) + " ";
            }

            mar_sent = mar_sent.trim();
            hin_sent = hin_sent.trim();
            Files.originaltranslationWriter_hin.write(hin_sent+"\n");
            Files.originaltranslationWriter_hin.flush();
            translate_phrases_of_sentence();

            double current_score = score_sentence(hin_sent);
            
            
            for (int index = 0; index < in_phrases_seq.size(); index++) {
                ArrayList<String> curr_hin_sent_seq = new ArrayList<String>(out_phrases_seq);
                ArrayList<String> substitutes = phrase_maps_smt_surface.get(in_phrases_seq.get(index));
                substitutes.addAll(phrase_maps_smt_suff_split.get(in_phrases_seq.get(index)));
                for (String substitute : substitutes) {
                    curr_hin_sent_seq.set(index, substitute);
                    hin_sent="";
                    for (int index_inner = 0; index_inner < curr_hin_sent_seq.size(); index_inner++) {
                        hin_sent += curr_hin_sent_seq.get(index_inner) + " ";
                    }
                    hin_sent = hin_sent.trim();
                    double new_score = score_sentence(hin_sent);
                    if(new_score<current_score){
                        current_score = new_score;
                        out_phrases_seq = new ArrayList<String>(curr_hin_sent_seq);
                    }
                }
            }
            mar_sent = "";
            hin_sent = "";
            for (int index = 0; index < in_phrases_seq.size(); index++) {
                mar_sent += in_phrases_seq.get(index) + " ";
                //System.out.println(mar_sent);
            }
            for (int index = 0; index < out_phrases_seq.size(); index++) {
                hin_sent += out_phrases_seq.get(index) + " ";
            }

            mar_sent = mar_sent.trim();
            hin_sent = hin_sent.trim();
            System.out.println(hin_sent);
            System.out.println(phrase_maps_smt_surface);
            System.out.println(phrase_maps_smt_suff_split);
            //System.out.println(in_phrases_seq);
            //System.out.println(out_phrases_seq);
            Files.finaltranslationWriter_mar.write(mar_sent+"\n");
            Files.finaltranslationWriter_hin.write(hin_sent+"\n");
           
            Files.finaltranslationWriter_mar.flush();
            Files.finaltranslationWriter_hin.flush();
           

        }
    }

    public double score_sentence(String sentence) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File("temp")));
            bw.write(sentence+"\n");
            bw.flush();
            bw.close();
            //final ProcessBuilder pb = new ProcessBuilder("bash", "-c", "/home/raj/tools/moses/bin/query /home/raj/hi-mar/work-9-MTP/lm/hindimonolingual.tokenized.blm.hi null <temp");
            final ProcessBuilder pb = new ProcessBuilder("bash", "-c", "/home/raj/tools/kenlm/bin/query -n /home/raj/hi-mar/lm_hindi/hindi_lm.bin <temp");
            Process p = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //System.out.println(input.readLine());
            String scores[] = input.readLine().split("\t");
            double ll = Double.parseDouble(scores[scores.length - 1].split(" ")[1]);
            return ll;

        } catch (IOException ex) {
            Logger.getLogger(MRHIHybridization.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(MRHIHybridization.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0.0;
    }

    public void translate_phrases_of_sentence() throws XmlRpcException, IOException, InterruptedException {
        for (String s : in_phrases_seq) {
            String translations[] = translate_phrase(s, 1);
            if (!(translations == null || translations.length == 0)) {
                ArrayList<String> trans = new ArrayList<String>(Arrays.asList(translations));
                phrase_maps_smt_surface.put(s, trans);
            }
            String ss = surface_to_suff_split_maps.get(s);
            translations = translate_phrase(ss, 2);
            if (!(translations == null || translations.length == 0)) {
                ArrayList<String> trans = new ArrayList<String>(Arrays.asList(translations));
                phrase_maps_smt_suff_split.put(s, trans);
            }
        }
    }

    public String[] translate_phrase(String phrase, int type) throws XmlRpcException, IOException, InterruptedException {
        //System.out.println(phrase);
        String translations[] = null;
        HashMap<String, String> mosesParams = new HashMap<String, String>();
        String textToTranslate = phrase;
        mosesParams.put("text", textToTranslate);
        mosesParams.put("align", "true");
        
        //mosesParams.put("nbest", "1");
        // The XmlRpcClient.execute method doesn't accept Hashmap (pParams). It's either Object[] or List. 
        Object[] params = new Object[]{null};
        params[0] = mosesParams;
        // Invoke the remote method "translate". The result is an Object, convert it to a HashMap.
        HashMap result = null;

        String textTranslation = "";
        switch (type) {
            case 1:
                result = (HashMap) client_surface.execute("translate", params);
                textTranslation = (String) result.get("text");
                break;
            case 2:
                result = (HashMap) client_suff_split.execute("translate", params);
                textTranslation = (String) result.get("text");
                break;
            case 3:
                Runtime r = Runtime.getRuntime();
                String[] cmd = {
                    "/bin/sh",
                    "-c",
                    "echo \"" + phrase + "\" | /bin/nc localhost 12348"
                };
                Process p = r.exec(cmd);
                p.waitFor();
                BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                String translation = "";
                while ((line = b.readLine()) != null) {
                    //System.out.println(tamkey+" "+line);
                    line = line.replaceAll("।", "");

                    for (int smt_ctr = 0; smt_ctr < line.split("\\|").length; smt_ctr = smt_ctr + 2) {
                        translation = translation + line.split("\\|")[smt_ctr].trim() + " ";
                    }
                    translation = translation.trim();
                }
                textTranslation = translation;
                break;
        }

        // Print the returned results

        //System.out.println(textTranslation);
        translations = new String[1];
        translations[0] = textTranslation;
        return translations;
    }

    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        MRHIHybridization mrhihybrid = new MRHIHybridization();
        mrhihybrid.init();
        mrhihybrid.hybridize();
        //mrhihybrid.translate_phrase("मला", 1);
    }
}
