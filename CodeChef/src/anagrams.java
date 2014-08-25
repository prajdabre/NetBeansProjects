
import java.util.HashMap;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author RAJ
 */
public class anagrams {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Vector<String> list = new Vector<String>();
        list.add("Resistance");
        list.add("Ancestries");
        list.add("Gainly");
        list.add("Laying");
        list.add("test");
        list.add("troop");
        printAnagrams(list);
    }

    static void printAnagrams(Vector<String> V) {
        HashMap<HashMap<String, Integer>, Integer> anacounter = new HashMap<HashMap<String, Integer>, Integer>();
        HashMap<HashMap<String, Integer>, Vector<String>> analist = new HashMap<HashMap<String, Integer>, Vector<String>>();
        for (String s : V) {
            char split[] = s.toLowerCase().toCharArray();
            HashMap<String,Integer> key = new HashMap<String, Integer>();
            for(char c: split){
                String ch = String.valueOf(c);
                
                if(key.containsKey(ch)){
                    Integer val = key.get(ch);
                    val++;
                    key.put(ch, val);
                } else{
                    key.put(ch, 1);
                }
            }
            if(anacounter.containsKey(key)){
                Integer val = anacounter.get(key);
                val++;
                anacounter.put(key, val);
            } else {
                anacounter.put(key, 1);
            }
            if(analist.containsKey(key)){
                Vector<String> anas = analist.get(key);
                anas.add(s);
                analist.put(key, anas);
            } else {
                Vector<String> anas = new Vector<String>();
                anas.add(s);
                analist.put(key, anas);
            }
        }
        for(HashMap<String,Integer> hm : analist.keySet()){
            Vector<String> list = analist.get(hm);
            if(list.size()>=2){
                System.out.println(list.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", ""));
            }
        }
    }
}
