/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hindi_word_gen;

import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.data.POS1;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.lte.LexicalTransferEngine;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author raj
 */
public class hinwordgen {

    /**
     * @param args the command line arguments
     */
    public LexicalTransferEngine lte = new LexicalTransferEngine();
    public static HashMap<String, HashMap<String, ArrayList<String>>> wordforms = new HashMap<String, HashMap<String, ArrayList<String>>>();

    private void readStems() {
        try {
            //InputStream fstream = this.getClass().getResourceAsStream( "resources/repo" );
            // Get the object of DataInputStream
            //DataInputStream inStream = new DataInputStream(fstream);
            //System.out.println();
            //BufferedReader in = new BufferedReader(new FileReader(this.getClass().getResource("/resources/repo").getFile()));
            BufferedReader in = new BufferedReader(new FileReader("repo"));
            String line;
            //System.out.println(MA.USER_HOME);
            while ((line = in.readLine()) != null) {
                //System.out.println(str);
                Pattern pEnd = Pattern.compile(">");
                Matcher mEnd = pEnd.matcher(line);
                line = mEnd.replaceAll("");

                Pattern pStart = Pattern.compile("<");
                Matcher mStart = pStart.matcher(line);
                line = mStart.replaceAll("");
                String bits[] = line.split(";");
                String group = bits[0];
                String form = bits[1];
                String analysis = bits[2];
                if (wordforms.containsKey(group)) {
                    HashMap<String, ArrayList<String>> wordform = wordforms.get(group);
                    if (wordform.containsKey(form)) {
                        ArrayList<String> analyses = wordform.get(form);
                        if (!analyses.contains(analysis)) {
                            analyses.add(analysis);
                            wordform.put(form, analyses);
                        }
                    } else {
                        ArrayList<String> analyses = new ArrayList<String>();
                        analyses.add(analysis);
                        wordform.put(form, analyses);
                    }
                    wordforms.put(group, wordform);
                } else {
                    ArrayList<String> analyses = new ArrayList<String>();
                    analyses.add(analysis);
                    HashMap<String, ArrayList<String>> wordform = new HashMap<String, ArrayList<String>>();
                    wordform.put(form, analyses);
                    wordforms.put(group, wordform);
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Failed to open repo file!");
        }
    }

    public void intitalize() {
        try {
            String USER_HOME = "/home/raj/sampark/";
            lte.initialize(USER_HOME, true, true, true, true, true);
            readStems();

            //String[] translation = lte.getTranslation("करणे", POS1.VERB, Language.MARATHI, Language.HINDI, null);

        } catch (MultilingualDictException ex) {
            Logger.getLogger(hinwordgen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generate_forms() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("all_surfaces_noun.txt")));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src-tgt-surfaces.txt")));
        String line = "";
        while ((line = br.readLine()) != null) {
            String components[] = line.trim().split("<");
            if (components.length > 3) {
                continue;
            } else {
                String stem = components[0].trim();
                String root_cat = components[1].trim().split(">")[0].trim().split("-")[0].trim();
                ArrayList<String> anals = returnAnalyses(stem, root_cat);
                String suff = "";
                if (components[1].trim().split(">").length != 1) {
                    suff = components[1].trim().split(">")[1].trim();
                }
                String suffix_translations[] = null;
                if (!suff.equals("")) {
                    suffix_translations = lte.getTranslation(suff, POS1.FW, Language.MARATHI, Language.HINDI, null);
                }
                String root_wd_arr[] = new String[1];
                String surface_word = stem + suff;
                for (String s : anals) {
                    String root = s.replaceAll(">", "").replaceAll("<", "").split(",")[0];
                    //root = root + "णे";
                    root_wd_arr[0] = root;
                    String root_translations[] = lte.getTranslation(root, POS1.VERB, Language.MARATHI, Language.HINDI, null);

                    if (root_translations == null || root_translations.length == 0) {
                        continue;
                    }

                    for (String s1 : root_translations) {

                        if (suffix_translations == null || suffix_translations.length == 0 ) {
                            bw.write(surface_word + "\t" + s1 + "\n");
                            continue;
                        } else {
                            for (String s2 : suffix_translations) {
                                bw.write(surface_word + "\t" + s1 + "\t" + s2 + "\n");
                            }
                        }


                    }
                    bw.flush();
                }
            }
        }
        bw.close();
    }

    private ArrayList<String> returnAnalyses(String form, String category) {
        ArrayList<String> analyses = new ArrayList<String>();
        if (wordforms.get(category) != null) {
            HashMap<String, ArrayList<String>> forms = wordforms.get(category);
            if (forms.get(form) != null) {
                ArrayList<String> analysesVect = forms.get(form);
                for (int i = 0; i < analysesVect.size(); i++) {
                    String analysesCom[] = analysesVect.get(i).split("#");
                    for (String str : analysesCom) {
                        String analysis = str;
                        if (analysis.contains("::")) {
                            analysis = analysis.split("::")[0];

                        }
                        analyses.add("<" + analysis + ">");
                    }
                }
                //analyses=forms.get(form);
                return analyses;
            }
        }
        if (category.equals("NUM")) {
            analyses.add("<" + form + ",num,,,,,,,>");
        }

        if (category.equals("PUN")) {
            analyses.add("<" + form + ",pun,,,,,,,>");
        }

        return analyses;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        hinwordgen wgen = new hinwordgen();
        wgen.intitalize();
        wgen.generate_forms();
    }
}
