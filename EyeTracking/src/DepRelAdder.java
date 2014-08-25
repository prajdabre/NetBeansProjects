
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FilenameFilter;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class DepRelAdder {

    public static Document dom;
public static LexicalizedParser lp;
    /**
     * @param args the command line arguments
     */


        private File[] finder(String dirName,final String ending) {
            File dir = new File(dirName);

            return dir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String filename) {
                    return filename.endsWith(ending);
                }
            });

        }


    public static void parseXmlFile(String fpath) throws IOException {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(fpath);


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void adddepinfotoxml(String fpath) throws TransformerException, IOException {
        //lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        
        parseXmlFile(fpath);
        NodeList elementsByTagName = dom.getDocumentElement().getElementsByTagName("W");
        //System.out.println(elementsByTagName.getLength());
        int strcount = 0;
        String strs[] = new String[100];
        strs[0] = "";
        Vector<String> finout = new Vector<String>();
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            // System.out.println(elementsByTagName.item(i).getTextContent());
            //Str
            //System.out.println(elementsByTagName.item(i).getAttributes().getNamedItem("boundary"));

            if (elementsByTagName.item(i).getAttributes().getNamedItem("boundary") != null) {
                HashMap<String, Vector<String>> drels = new HashMap<String, Vector<String>>();

                strs[strcount] = strs[strcount] + " .";

                strs[strcount] = strs[strcount].trim();
                for (int i1 = 1; i1 <= strs[strcount].split(" ").length; i1++) {
                    //System.out.println(strs[strcount].split(" ")[i1-1]+"-"+i1);
                    Vector<String> temp = new Vector<String>();
                    temp.add("");
                    temp.add("");
                    drels.put(strs[strcount].split(" ")[i1 - 1] + "-" + String.valueOf(i1), temp);
                }
                //System.out.println(drels.keySet());
                //System.out.println(strs[strcount]);
                Tree parse = (Tree) lp.apply(strs[strcount]);
                GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
                System.out.println(tdl);
                //System.out.println(gs.allTypedDependencies());
//                for(String s2: drels.keySet()){
//                    System.out.println(s2+" "+drels.get(s2).size());
//                }
                //System.out.println(drels.get("£-17"));
                for (TypedDependency tdeps : tdl) {
                    String gov = tdeps.gov().toString();
                    String dep = tdeps.dep().toString();
                    String reln = tdeps.reln().toString();
                    if (reln.equals("root")) {
                        continue;
                    }
                    if(dep.contains("minimize")){
                        System.out.println("yes1");
                        dep=dep.replace("minimize", "minimise");
                    }
                    if(gov.contains("minimize")){
                        System.out.println("yes2");
                        gov=gov.replace("minimize", "minimise");
                    }
                    if(reln.matches("number")){
                        if(gov.contains("#")){
                            gov=gov.replace("#", "£");
                        }
                        if(dep.contains("#")){
                            dep=dep.replace("#", "£");
                        }
                    }
                    //System.out.println(drels.get("minimize-24"));
                    //System.out.println(gov+" "+dep+" "+reln);
                    Vector<String> depinfo = drels.get(dep);
                    Vector<String> govinfo = drels.get(gov);
                    if(depinfo == null){
                        System.out.println(dep+" dep null here");
                        System.out.println(gov+" "+dep+" "+reln);
                    }
                    if(govinfo == null){
                        System.out.println(gov+" gov null here");
                        System.out.println(gov+" "+dep+" "+reln);
                    }
                    //System.out.println(depinfo.size()+" "+govinfo.size());
                    String depin = depinfo.firstElement();
                    String depout = depinfo.lastElement();
                    String govin = govinfo.firstElement();
                    String govout = govinfo.lastElement();
                    depinfo.clear();
                    govinfo.clear();
                    if (reln.equals((String) "root")) {
                        //System.out.println("Root is: "+dep);
                    } else {
                        int govpos = Integer.parseInt(gov.split("-")[gov.split("-").length-1]);
                        int deppos = Integer.parseInt(dep.split("-")[dep.split("-").length-1]);
                        //System.out.println("");
                        depin = depin + String.valueOf(govpos - deppos) + ":" + reln + "|";
                        govout = govout + String.valueOf(deppos - govpos) + ":" + reln + "|";

                    }
                    depinfo.add(depin);
                    depinfo.add(depout);
                    govinfo.add(govin);
                    govinfo.add(govout);
                    drels.put(dep, depinfo);
                    drels.put(gov, govinfo);
                    //System.out.println(tdeps.gov() + " " + tdeps.dep() + " " + tdeps.reln().toString());
                }
                for (int i1 = 1; i1 <= strs[strcount].split(" ").length; i1++) {
                    //System.out.println(strs[strcount].split(" ")[i1-1]+"-"+i1);
                    //System.out.println(strs[strcount].split(" ")[i1-1]+ " " +drels.get(strs[strcount].split(" ")[i1-1]+"-"+i1));
                    String in = drels.get(strs[strcount].split(" ")[i1 - 1] + "-" + i1).firstElement();
                    String out = drels.get(strs[strcount].split(" ")[i1 - 1] + "-" + i1).lastElement();
                    if (in.length() != 0) {
                        in = in.substring(0, in.length() - 1);
                    }
                    if (out.length() != 0) {
                        out = out.substring(0, out.length() - 1);
                    }
                    finout.add(in);
                    finout.add(out);
                }
                strcount++;
                strs[strcount] = "";
            } else {
                strs[strcount] = strs[strcount] + " " + elementsByTagName.item(i).getTextContent();
            }
        }
        System.out.println(finout.size());
//        for (String s : finout) {
//            System.out.println(s);
//        }
        for (int i = 0; i < elementsByTagName.getLength(); i++) {

            Element e = (Element) elementsByTagName.item(i);
            e.setAttribute("in", finout.elementAt(2 * i));
            e.setAttribute("out", finout.elementAt((2 * i) + 1));

            //System.out.println();
        }
        for (int i = 0; i < elementsByTagName.getLength(); i++) {

            Element e = (Element) elementsByTagName.item(i);
           // System.out.println("in:" + e.getAttribute("in"));

            //System.out.println();
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(dom);
        StreamResult result = new StreamResult(new File(fpath+".fin"));

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        System.out.println("File saved!");
    }

    public static void main(String[] args) throws IOException, TransformerConfigurationException, TransformerException {
        // TODO code application logic here
        lp = LexicalizedParser.loadModel("frenchFactored.ser.gz");
        //lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
        DepRelAdder dreladder = new DepRelAdder();
        File flist[] = dreladder.finder("C:/Users/RAJ/Documents/NetBeansProjects/EyeTracking/TPR-DB/BML12/Alignment", "tgt");
        System.out.println(flist.length);
        for(File f : flist){
            System.out.println(f.getName());
            dreladder.adddepinfotoxml(f.getAbsolutePath());
        }
    }
}
