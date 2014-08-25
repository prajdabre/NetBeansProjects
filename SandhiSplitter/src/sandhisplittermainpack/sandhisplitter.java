/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandhisplittermainpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sandhisplittermainpack.io.resources;
import sl.morph.mar.transducer.Transducer;

/**
 *
 * @author RAJ
 */
public class sandhisplitter {

    public Set<String> s;
    public HashMap<String, Vector<String>> replaceby;
    public static HashMap<String, Vector<String>> inflforms = new HashMap<String, Vector<String>>();
    Transducer t;
    public sandhisplitter() throws FileNotFoundException, IOException {
        t=new Transducer(resources.morphotactpath);
        s = new HashSet<String>();
        replaceby = new HashMap<String, Vector<String>>();
        BufferedReader rin = new BufferedReader(new FileReader(new File(resources.repopath)));
        String line = "";
        while ((line = rin.readLine()) != null) {
            if(line.contains("<DF>")){
            s.add(line.split(";")[1].replaceAll("<", "").replace(">", "").trim());
            }
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
                if (inflforms.containsKey(form)) {
                    Vector<String> analyses = inflforms.get(form);
                    if (!analyses.contains(analysis)) {
                        analyses.add(analysis);
                        inflforms.put(form, analyses);
                    }
                } else {
                    Vector<String> analyses = new Vector<String>();
                    analyses.add(analysis);

                    inflforms.put(form, analyses);

                }
        }
        s.add("मति");
        s.add("प्रति");
        s.add("अति");
        s.add("ओघ");
        s.add("अन");
        rin = new BufferedReader(new FileReader(new File(resources.rulespath)));
        while ((line = rin.readLine()) != null) {
            if (!line.contains("#")) {
                if(replaceby.containsKey(line.split("\t")[1])){
                    Vector<String> temp=replaceby.get((String)line.split("\t")[1]);
                    temp.add(line.split("\t")[2]);
                    replaceby.put(line.split("\t")[1], temp);
                }else{
                    Vector<String> temp = new Vector<String>();
                    temp.add(line.split("\t")[2]);
                    replaceby.put(line.split("\t")[1], temp);
                }
                
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public Vector<String> splitsandhi(String str) {
        Vector<String> splitcont = new Vector<String>();
        Set entset = replaceby.entrySet();
        Iterator i = entset.iterator();
        int done=0;
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            String instring = (String) me.getKey();
            Vector<String> outstringvect = (Vector<String>) me.getValue();
            String[] parts = str.split(instring);
            for(String outstring:outstringvect){
            if (parts.length > 1) {
                for (int j = 0; j<parts.length;j++){
                    String fin=mergearray(parts,0,j,instring)+outstring+str.replace(mergearray(parts,0,j,instring)+instring, "");
                   // System.out.println("Parts Detected: "+fin );
                    if(s.contains(fin.split("-")[0]) && !t.getParsedOutput(fin.split("-")[1]).firstElement().contains("analysis") ){
                        
                        if(fin.split("-")[0].equals((String)"अन")){
                            System.out.println("Sandhi for: "+str+" detected and split as: "+fin.split("-")[0]+" - "+fin.split("-")[1]+" with analysis "+inflforms.get(fin.split("-")[1]) + " which is a negative compound word");
                            splitcont.add(fin.split("-")[0]);
                            splitcont.add(fin.split("-")[1]+" with analysis "+inflforms.get(fin.split("-")[1]) + " which is a negative compound word");
                        } else{
                            System.out.println("Sandhi for: "+str+" detected and split as: "+fin.split("-")[0]+" with analysis "+inflforms.get(fin.split("-")[0])+" - "+fin.split("-")[1] +" with analysis "+inflforms.get(fin.split("-")[1]));
                            splitcont.add(fin.split("-")[0]+" with analysis "+inflforms.get(fin.split("-")[0]));
                            splitcont.add(fin.split("-")[1]+" with analysis "+inflforms.get(fin.split("-")[1]));
                        }
                    done=1;
                    break;
                    }
                }
                if(done==1)
                    break;

        }
            }
        
    }
        if(done==0){
            splitcont.add("Not Analyzed");
        }
        return splitcont;
    }

    public String mergearray(String[] parts,int start, int end,String replacer){
        String merged="";
        merged=parts[start];
        for(int i = start+1 ; i <= end ; i++){
            merged+=replacer+parts[i];
        }
        return merged;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        sandhisplitter splitter = new sandhisplitter();
      //  System.out.println(splitter.s.contains((String)"इच्छा"));
    //    System.out.println(splitter.replaceby.get("ो"));
//        String instr = "या";
//        String outstr = "बी";
//        String mainstr= "माझ्यातुझ्याबरोबर";
//        String[] parts=mainstr.split(instr);
//        for (int j = 0; j<parts.length;j++){
//                    String fin=parts[j]+outstr+mainstr.replace(parts[j]+instr, "");
//                    System.out.println(fin);
//        }
        String test[] = {"जितेंद्रिय","मृतात्मा","जोडाक्षर","अनोळखी","अनिष्ट","न्यायान्याय","देवालय","विद्यारंभ","देवीच्छा","पार्वतीश","भूद्धार","भूर्जित","ईश्वरेच्छा","गणेश","सूर्योदय","समुद्रोर्मी","रमेच्छा","रमेश","गंगोत्साह","गंगोर्मी","अत्यल्प","अत्यानंद","अत्युत्तम","अत्यूर्जा","प्रत्येक","मत्यौघ","नद्यर्पण","गौर्यानंद"};
        for(String stest: test)
        splitter.splitsandhi(stest);
    }
}
