import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import java.util.Iterator;
import java.util.TreeMap;
import java.text.*;
import edu.sussex.nlp.jws.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class TestJWSRandom
{

// 'TestJWSRandom' - how to use the JWSRandom class
// replacement for the 'TestRandom' class
// David Hope, 2008
 	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException
	{

// Set up Java WordNet::Similarity
		String	dir	=	"C:/Program Files (x86)/WordNet";
		JWS		ws	=	new JWS(dir, "2.1");


// Create a 'JWSRandom' instance: there are 3 constructors that one can use:

// C1.
		//JWSRandom random = new JWSRandom(dict); 							//  default, completely random

// C2.
		//JWSRandom random = new JWSRandom(dict, true); 					//	true = store the randomly generated numbers (default) for a sense pair
																										// false = exactly the same behaviour as C1.
// C3.
	//ICFinder ic=new ICFinder(dir);
        //    System.out.println(ic.getNounRoots());
                JWSRandom random = new JWSRandom(ws.getDictionary(), true, 1.0); 	// set the upper limit on the scores
//Path p=new Path(null, null)
        // set the upper limit on the scores
        // Example Of Use:
        // 1. all senses
      //  IDictionary dictionary = ws.getDictionary();
       // dictionary.getIndexWord("exactly", POS.)
//		TreeMap<String,Double> map = random.random("exact", "approximate", "a");
//		for(String pair : map.keySet())
//		{
//			System.out.println(pair + "\t" + (map.get(pair)));
//		}
// 2. max
     //           AdaptedLesk alsk=new AdaptedLesk(ws.getDictionary());
                double max=0.0;
                String categ="n";
                String cats=POS.TAG_ADJECTIVE+" "+POS.TAG_ADVERB+" "+POS.TAG_NOUN+" "+POS.TAG_VERB;
               for(String s:cats.split(" ")){

                   System.out.println("\nmax\t\t\t" + random.max("exact", "approximate", s));
                   if(random.max("exact", "approximate", s)>max)
                       max=random.max("exact", "approximate", s);
           }
               if(max>0.5)
                   System.out.println("You may be funny after all");
               else
                   System.out.println("Either you are not funny or your vocabulary is IlPathos!");
               File file=new File("C:\\Users\\RAJ\\Documents\\NetBeansProjects\\homonyms.txt");
               BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
               String line=reader.readLine();
               List<String> homos = new ArrayList<String>();
               while(line!=null){
         //          System.out.println(line.trim());
          
           homos.add(line.trim().replaceAll(", ", " "));
        
                line=reader.readLine();
               }
               //System.out.println(homos);
               System.out.println("\n");
               String sent="People with a long tail love to tell a tall tale";
        Iterator<String> iterator = homos.iterator();
        int maxmatch=0;
               while(iterator.hasNext()){
                   //System.out.println(iterator.next());
                   String currhomo=iterator.next();
                       
                   int thismatch=0;
                   for(String sentcont: sent.split(" ")){
                       if(currhomo.contains(sentcont) && sentcont.length()>2){
                       thismatch++;
                      // System.out.println(sentcont+" "+currhomo);
                       }
                   }
                   if(thismatch>1){
                   maxmatch+=thismatch;
                   }
               }
        if(maxmatch>=2){
            System.out.println("There is potential humor with score : "+maxmatch+". Congrats! you can become trolldad someday! ");
        }
 else{
            System.out.println("So you think you are funny?");
 }
          //  System.out.println("raj".contains("p"));

    }
} // eof
