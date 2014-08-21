/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweightwsd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author raj
 */
public class tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Set<String> hs = new HashSet<String>();
        hs.add("12345");
        hs.add("123");
        hs.add("231");
        hs.add("223423");
        System.out.println(hs);
        ArrayList<Integer> al  = new ArrayList<Integer>();
        for(String s: hs){
            al.add(Integer.parseInt(s));
        }
        
        System.out.println(al);
        Collections.sort(al);
        System.out.println(al);
        
        System.out.println("");
        
        HashMap<String,String> hm = new HashMap<String, String>();
        hm.put("raj", "sdgdrf");
        
        
        
    }
}
