/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package humor;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;

/**
 *
 * @author RAJ
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("wordnet.database.dir", "C:/Program Files (x86)/WordNet/2.1/dict/");
        NounSynset nounSynset;
NounSynset[] hyponyms;
        
WordNetDatabase database = WordNetDatabase.getFileInstance();
Synset[] synsets = database.getSynsets("rape", SynsetType.NOUN);

for (int i = 0; i < synsets.length; i++) {
    nounSynset = (NounSynset)(synsets[i]);
    hyponyms = nounSynset.getHyponyms();
    System.err.println(nounSynset.getWordForms()[0] +
            ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
}
String sent="inaccurate approximate";
for (String words:sent.split(" ")){
            Synset[] synsets1 = database.getSynsets(words);
            for (Synset synword:synsets1){
                String[] usageExamples = synword.getWordForms();
                for(String usageword:usageExamples){
                    System.out.println(words+" "+usageword+"\n");
                }
                WordSense[] antonyms = synword.getAntonyms(words);
                for (WordSense antoword: antonyms){
                    //System.out.println();
                    String[] usageExamplesanto = antoword.getSynset().getWordForms();
                    for(String usagewordanto:usageExamplesanto){
                        System.out.println(usagewordanto+"\n");
                    }
                }
            }
}
//for (String s : sent.split(" ")){
//    System.out.println(s);
//            Synset[] ss = database.getSynsets(s);
//
//    for (Synset syn : ss){
//                String[] usages=syn.getUsageExamples();
////                hyponyms = ((NounSynset)(syn)).getHyponyms();
////                for(NounSynset ns: hyponyms){
////                    for(String s1:ns.getUsageExamples()){
////                        System.out.println(s1);
////                    }
////
////                }
//                for (String wfs: usages){
//                    System.out.println(wfs);
//                }
//                WordSense[] antonyms = syn.getAntonyms(s);
//                for (WordSense ws: antonyms){
//                    for(String inusages:ws.getSynset().getUsageExamples()){
//                        System.out.println(inusages);
//                    }
//
//                }
//    }
//}
        // TODO code application logic here
//WordNetDatabase database = WordNetDatabase.getFileInstance();
			//Synset[] synsets = database.getSynsets(wordForm);
                        for(String s1:database.getBaseFormCandidates("exact", SynsetType.VERB)){
                            System.out.println(s1);
                        }
    }

}
