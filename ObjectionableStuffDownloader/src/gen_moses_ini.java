
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
public class gen_moses_ini {

    /**
     * @param args the command line arguments
     */
    public static void write_basic_ini(String src, String tgt) {
        try {
            String pair = src + "-" + tgt;
            BufferedWriter br = new BufferedWriter(new FileWriter(new File(Files.phrase_tables_base + "/" + pair + "/moses.ini")));
            br.write("[input-factors]\n0\n\n[mapping]\n0 T 0\n\n[distortion-limit]\n6\n\n[feature]\nUnknownWordPenalty\nWordPenalty\n");
            br.write("PhraseDictionaryBinary name=TranslationModel0 table-limit=20 num-features=5 path=");
            br.write(Files.phrase_tables_base + "/" + pair + "/phrase-table input-factor=0 output-factor=0\n");
            br.write("LexicalReordering name=LexicalReordering0 num-features=6 type=wbe-msd-bidirectional-fe-allff input-factor=0 output-factor=0 ");
            br.write("path=" + Files.phrase_tables_base + "/" + pair + "/reordering-table");
            br.write("\nDistortion\nKENLM lazyken=0 name=LM0 factor=0 path=" + Files.LM_base + "/" + tgt + ".blm order=5\n");
            br.write("\n[weight]\nUnknownWordPenalty0= 1\nWordPenalty0= -1\nTranslationModel0= 0.2 0.2 0.2 0.2 0.2\nLexicalReordering0= 0.3 0.3 0.3 0.3 0.3 0.3\nDistortion0= 0.3\nLM0= 0.5\n");
            br.flush();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(gen_moses_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void write_piv_ini(String src, String piv, String tgt) {
        try {
            String pair = src + "-" + tgt;
            String triplet = src + "-" + piv + "-" + tgt;
            BufferedWriter br = new BufferedWriter(new FileWriter(new File(Files.phrase_tables_base + "/" + pair + "/moses-" + triplet + ".ini")));
            br.write("[input-factors]\n0\n\n[mapping]\n0 T 0\n\n[distortion-limit]\n6\n\n[feature]\nUnknownWordPenalty\nWordPenalty\n");
            br.write("PhraseDictionaryBinary name=TranslationModel0 table-limit=20 num-features=5 path=");
            br.write(Files.phrase_tables_base + "/" + pair + "/phrase-table-" + triplet + " input-factor=0 output-factor=0\n");
            br.write("LexicalReordering name=LexicalReordering0 num-features=6 type=wbe-msd-bidirectional-fe-allff input-factor=0 output-factor=0 ");
            br.write("path=" + Files.phrase_tables_base + "/" + pair + "/reordering-table");
            br.write("\nDistortion\nKENLM lazyken=0 name=LM0 factor=0 path=" + Files.LM_base + "/" + tgt + ".blm order=5\n");
            br.write("\n[weight]\nUnknownWordPenalty0= 1\nWordPenalty0= -1\nTranslationModel0= 0.2 0.2 0.2 0.2 0.2\nLexicalReordering0= 0.3 0.3 0.3 0.3 0.3 0.3\nDistortion0= 0.3\nLM0= 0.5\n");
            br.flush();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(gen_moses_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void write_combo_pivs(String src, String[] pivs, String tgt,String type) {
        try {
            String pair = src + "-" + tgt;

            BufferedWriter br = new BufferedWriter(new FileWriter(new File(Files.phrase_tables_base + "/" + pair + "/moses-" + "combo" + type +".ini")));
            br.write("[input-factors]\n0\n\n[mapping]\n");
            for (int i = 0; i < pivs.length; i++) {
                br.write("0 T " + String.valueOf(i) + "\n");
            }
            br.write("\n[distortion-limit]\n6\n\n[feature]\nUnknownWordPenalty\nWordPenalty\n");

            for (int i = 0; i < pivs.length; i++) {
                String triplet = src + "-" + pivs[i] + "-" + tgt;
                br.write("PhraseDictionaryBinary name=TranslationModel"+ String.valueOf(i) + " table-limit=20 num-features=5 path=");
                br.write(Files.phrase_tables_base + "/" + pair + "/phrase-table-" + triplet + " input-factor=0 output-factor=0\n");
            }

            br.write("LexicalReordering name=LexicalReordering0 num-features=6 type=wbe-msd-bidirectional-fe-allff input-factor=0 output-factor=0 ");
            br.write("path=" + Files.phrase_tables_base + "/" + pair + "/reordering-table");
            br.write("\nDistortion\nKENLM lazyken=0 name=LM0 factor=0 path=" + Files.LM_base + "/" + tgt + ".blm order=5\n");
            br.write("\n[weight]\nUnknownWordPenalty0= 1\nWordPenalty0= -1\n");
            for(int i=0;i<pivs.length;i++){
                br.write("TranslationModel"+String.valueOf(i)+"= 0.2 0.2 0.2 0.2 0.2\n");
            }

            br.write("LexicalReordering0= 0.3 0.3 0.3 0.3 0.3 0.3\nDistortion0= 0.3\nLM0= 0.5\n");
            br.flush();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(gen_moses_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        String pivots[] = new String[]{"bn", "gu", "kK", "ml", "mr", "pa", "ta", "te", "ur", "hi", "en"};
        String srcs[] = new String[]{"en"};
        String tgts[] = new String[]{"en"};

        for (String src : srcs) {
            for (String tgt : pivots) {
                if (src.equals(tgt)) {
                    continue;
                }
                String pivs[] = new String[pivots.length-2];
                int index=0;
                for(String s:pivots){
                    if(!s.equals(tgt) && !s.equals(src)){
                        pivs[index] = s;
                        index++;
                    }
                }
                System.out.println("Language Pair: " + src + "-" + tgt);
                write_combo_pivs(src, pivs, tgt, "all");
            }
        }

        for (String src : pivots) {
            for (String tgt : tgts) {
                if (src.equals(tgt)) {
                    continue;
                }
                String pivs[] = new String[pivots.length-2];
                int index=0;
                for(String s:pivots){
                    if(!s.equals(tgt) && !s.equals(src)){
                        pivs[index] = s;
                        index++;
                    }
                }
                System.out.println("Language Pair: " + src + "-" + tgt);
                write_combo_pivs(src, pivs, tgt, "all");
            }
        }

//        for (String src : pivots) {
//            for (String tgt : pivots) {
//                if (src.equals(tgt)) {
//                    continue;
//                }
//                System.out.println("Language Pair: " + src + "-" + tgt);
//                write_basic_ini(src, tgt);
//                for (String piv : pivots) {
//                    if (src.equals(piv) || piv.equals(tgt)) {
//                        continue;
//                    }
//
//                    write_piv_ini(src, piv, tgt);
//
//                    System.out.println("Made ini for " + src + "-" + piv + "-" + tgt);
//
//                }
//            }
//        }






    }
}
