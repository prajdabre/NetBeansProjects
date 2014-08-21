package transducerinverter;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author raj
 */
public class Files {
    public static String base = "/home/raj/NetBeansProjects/Marathi_Word_Gen/";
    public static String mainfstfile = "morphoTact.fst";
    public static String nominalfstfile = "nominal.fst";
    public static String verbalfstfile = "verbal.fst";
    public static String otherfile = "other.fst";
    public static String pnfile = "pn.fst";
    public static String suffixesfile = "suffixes.fst";
    public static String verbsfile = "verbs.fst";
    public static String repofile = "repo";
    public static String dictfile = "dictionary";
    public static String verbsuffixes = "verbSuffixes";
    public static String expanded_grammar = "expanded_rules.txt";
    public static String all_surfaces = "all_surfaces_noun.txt";
    public static BufferedReader br = null;
    public static BufferedReader nominal_file = null;
    public static BufferedReader verbal_file = null;
    public static BufferedReader main_file = null;
    public static BufferedReader other_file = null;
    public static BufferedReader pn_file = null;
    public static BufferedReader suffixes_file = null;
    public static BufferedReader verbs_file = null;
    public static BufferedReader terminal_files[] = null;
    public static BufferedReader verb_suffixes = null;
    public static BufferedReader repo_file = null;
    public static BufferedReader dict_file = null;
    public static BufferedReader expanded_gammar_file = null;
    public static void open_files(){
        try {
            nominal_file = new BufferedReader(new FileReader(nominalfstfile));
            verbal_file = new BufferedReader(new FileReader(verbalfstfile));
            main_file = new BufferedReader(new FileReader(mainfstfile));
            other_file = new BufferedReader(new FileReader(otherfile));
            pn_file = new BufferedReader(new FileReader(pnfile));
            suffixes_file = new BufferedReader(new FileReader(suffixesfile));
            verbs_file = new BufferedReader(new FileReader(verbsfile));
            terminal_files = new BufferedReader[]{other_file,pn_file,suffixes_file,verbs_file};
            verb_suffixes = new BufferedReader(new FileReader(verbsuffixes));
            repo_file = new BufferedReader(new FileReader(repofile));
            dict_file = new BufferedReader(new FileReader(dictfile));
            expanded_gammar_file = new BufferedReader(new FileReader(expanded_grammar));
            System.out.println("Files opened for read and write");
        } catch (Exception ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
