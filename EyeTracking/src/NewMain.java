import java.util.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class NewMain {
  public static void main(String[] args) {

    LexicalizedParser lp = LexicalizedParser.loadModel("englishPCFG.ser.gz"); //<--TODO path to grammar goes here
    lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

    String sent = "I will kill all my enemies";
    Tree parse = (Tree) lp.apply(sent);
    //parse.pennPrint();
    System.out.println();

    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
      //System.out.println("Raj:"+gs.typedDependencies());
    System.out.println(tdl);
    System.out.println();
    for(TypedDependency td: tdl){
        System.out.println(td.reln());
    }
    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
    //tp.printTree(parse);
  }

}