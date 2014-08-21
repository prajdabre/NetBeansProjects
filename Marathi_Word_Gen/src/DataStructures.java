
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
public class DataStructures {

    public static HashMap<String, String> morphemes_to_surface = new HashMap<String, String>();
    public static HashSet<String> root_word = new HashSet<String>();
    public static HashMap<String, Set<String>> morphemes_to_surface_multiple = new HashMap<String, Set<String>>();
    public static void load_map() {
        try {
            Files.br = new BufferedReader(new FileReader(new File(Files.base + "/" + Files.mapfile)));
            String line = "";
            while ((line = Files.br.readLine()) != null) {
                line = line.trim();
                String components[] = line.split(" ");
                if (components.length>=2) {
                    String key = "";
                    String value = components[0];
                    for(int i=1;i<components.length;i++){
                        key = key + components[i];
                        key=key.trim();
                    }
                    root_word.add(components[1]);
                    morphemes_to_surface.put(key, value);
                    if(morphemes_to_surface_multiple.containsKey(key)){
                        Set<String> forms = morphemes_to_surface_multiple.get(key);
                        forms.add(value);
                        morphemes_to_surface_multiple.put(key, forms);
                    } else {
                        Set<String> forms = new HashSet<String>();
                        forms.add(value);
                        morphemes_to_surface_multiple.put(key, forms);
                    }
                    
                }
            }
            if(morphemes_to_surface.containsKey("भारततील")){
                        System.out.println("Here");
                    }
        } catch (Exception ex) {
            Logger.getLogger(DataStructures.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Data loaded in HashMap.\nSize is: "+morphemes_to_surface.size());
    }
}
