/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transducerinverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raj
 */
public class ReadGrammar {

    public DataStructures ds = new DataStructures();

    public void read_terminals() {
        try {
            String line = "";
            for (BufferedReader b : Files.terminal_files) {
                while ((line = b.readLine()) != null) {
                    line = line.trim();
                    line = line.replaceAll("[ ][ ]+", " ");
                    if (line.contains("%")) {
                        continue;
                    } else {
                        String components[] = line.split("=");
                        if (components.length == 2) {
                            String key = components[0];
                            String morpheme_file_name = components[1].split(" ")[0].replaceAll("\"", "");
                            String morpheme_tag_name = components[1].split(" ")[1];
                            if (ds.terminals_list.containsKey(key)) {
                                ArrayList<String> temp = ds.terminals_list.get(key);
                                temp.add(morpheme_file_name);
                                ds.terminals_list.put(key, temp);
                            } else {
                                ArrayList<String> temp = new ArrayList<String>();
                                temp.add(morpheme_file_name);
                                ds.terminals_list.put(key, temp);
                            }
                        }
                    }
                }
            }
            //display_rules();
        } catch (IOException ex) {
            Logger.getLogger(ReadGrammar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void read_nominal_grammar() {
        try {
            String line = "";
            int intermediate_counter = 1;
            while ((line = Files.nominal_file.readLine()) != null) {

                if (line.matches("^%.*") || line.matches("^#.*") || line.matches("^$") || line.length() == 0) {
                    continue;
                } else {
                    Stack<String> st = new Stack<String>();
                    String components[] = line.trim().split("=");
                    //System.out.println(line);
                    String rule_key = components[0].trim();
                    String rule_value = components[1].trim();

                    StringTokenizer stok = new StringTokenizer(rule_value, " ");
                    //System.out.print(rule_key+" = ");
                    while (stok.hasMoreTokens()) {
                        //System.out.print(stok.nextToken());
                        String token = stok.nextToken().trim();
                        if (token.equals(")")) {
                            String popped = "";
                            String inner_key = rule_key + "_inter_" + Integer.toString(intermediate_counter++);
                            String inner_value = "";
                            while (!(popped = st.pop()).equals("(")) {
                                inner_value = popped + " " + inner_value;
                                inner_value = inner_value.trim();
                            }
                            System.out.println("Key Value Pair created:" + inner_key + "=" + inner_value);
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(inner_value);
                            ds.intermediate_grammar_rules.put(inner_key, temp);
                            st.push(inner_key);
                        } else {
                            st.push(token);
                        }
                    }
                    String popped = "";
                    rule_value = "";
                    while (!st.empty()) {
                        popped = st.pop();
                        rule_value = popped + " " + rule_value;
                        rule_value = rule_value.trim();
                    }
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(rule_value);
                    ds.final_grammar_rules.put(rule_key, temp);
                    //System.out.println("");
                }
            }
            //display_rules();
        } catch (IOException ex) {
            Logger.getLogger(ReadGrammar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void read_verbal_grammar() {
        try {
            String line = "";
            int intermediate_counter = 1;
            while ((line = Files.verbal_file.readLine()) != null) {

                if (line.matches("^%.*") || line.matches("^#.*") || line.matches("^$") || line.length() == 0) {
                    continue;
                } else {
                    Stack<String> st = new Stack<String>();
                    String components[] = line.trim().split("=");
                    //System.out.println(line);
                    String rule_key = components[0].trim();
                    String rule_value = components[1].trim();

                    StringTokenizer stok = new StringTokenizer(rule_value, " ");
                    //System.out.print(rule_key+" = ");
                    while (stok.hasMoreTokens()) {
                        //System.out.print(stok.nextToken());
                        String token = stok.nextToken().trim();
                        if (token.equals(")")) {
                            String popped = "";
                            String inner_key = rule_key + "_inter_" + Integer.toString(intermediate_counter++);
                            String inner_value = "";
                            while (!(popped = st.pop()).equals("(")) {
                                inner_value = popped + " " + inner_value;
                                inner_value = inner_value.trim();
                            }
                            //System.out.println("Key Value Pair created:" + inner_key + "=" + inner_value);
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(inner_value);
                            ds.intermediate_grammar_rules.put(inner_key, temp);
                            st.push(inner_key);
                        } else {
                            st.push(token);
                        }
                    }
                    String popped = "";
                    rule_value = "";
                    while (!st.empty()) {
                        popped = st.pop();
                        rule_value = popped + " " + rule_value;
                        rule_value = rule_value.trim();
                    }
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(rule_value);
                    ds.final_grammar_rules.put(rule_key, temp);
                    //System.out.println("");
                }
            }
            //display_rules();
        } catch (IOException ex) {
            Logger.getLogger(ReadGrammar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void split_by_pipe(int which) {
        Iterator it;
        if (which == 1) {
            it = ds.intermediate_grammar_rules.entrySet().iterator();
        } else {
            it = ds.final_grammar_rules.entrySet().iterator();
        }
        HashMap<String, ArrayList<String>> final_grammar_rules_temp = new HashMap<String, ArrayList<String>>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = (String) pair.getKey();
            ArrayList<String> value = (ArrayList<String>) pair.getValue();
            ArrayList<String> temp = new ArrayList<String>();
            for (String val : value) {
                //System.out.println(val);
                String components[] = val.split("\\|");
                for (String comp : components) {
                    if (!temp.contains(comp)) {
                        temp.add(comp);
                    }
                }
            }
            final_grammar_rules_temp.put(key, temp);
        }
        if (which == 1) {
            ds.intermediate_grammar_rules = final_grammar_rules_temp;
        } else {
            ds.final_grammar_rules = final_grammar_rules_temp;
        }
    }

    public void expand_optionals_aka_question_marks(int which) {
        Iterator it;
        if (which == 1) {
            it = ds.intermediate_grammar_rules.entrySet().iterator();
        } else {
            it = ds.final_grammar_rules.entrySet().iterator();
        }

        HashMap<String, ArrayList<String>> final_grammar_rules_temp = new HashMap<String, ArrayList<String>>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = (String) pair.getKey();
            ArrayList<String> value = (ArrayList<String>) pair.getValue();
            //System.out.println(key+" "+value);
            ArrayList<String> temp_final = new ArrayList<String>();
            for (String val : value) {
                if (!val.contains("?")) {
                    temp_final.add(val.trim());
                }
                ArrayList<String> temp = new ArrayList<String>();
                //System.out.println(val);
                String components[] = val.split(" ");
                String new_val = "";
                Set<String> optionals = new HashSet<String>();
                for (int i = 0; i < components.length; i++) {
                    if (components[i].equals("?")) {
                        optionals.add(components[i - 1].trim());
                        //System.out.println("??? detected");

                    } else {
                        new_val = new_val + " " + components[i];
                        new_val = new_val.trim();
                    }

                }

                Set<Set<String>> powset = powerSet(optionals);
                //System.out.println(powset);
                for (Set<String> subset : powset) {
                    String reduced = new_val;
                    if (subset.isEmpty()) {
                        if (!temp_final.contains(reduced)) {
                            temp_final.add(reduced);
                        }
                        continue;
                    }
                    //System.out.println(subset);
                    for (String s : subset) {
                        reduced = reduced.replaceAll(s, "").replaceAll("[ ][ ]+", " ").trim();
                        //System.out.println("Here "+ reduced);
                    }
                    if (!temp_final.contains(reduced)) {
                        temp_final.add(reduced);
                    }
                }

            }
            //System.out.println(key+" "+temp_final);
            final_grammar_rules_temp.put(key, temp_final);
        }
        if (which == 1) {
            ds.intermediate_grammar_rules = final_grammar_rules_temp;
        } else {
            ds.final_grammar_rules = final_grammar_rules_temp;
        }
    }

    public void reduce_intermediates_to_terminals_using_intermediates() {
        HashMap<String, ArrayList<String>> intermediate_grammar_rules_temp = new HashMap<String, ArrayList<String>>();
        Iterator it = null;
        int expansion_occured = 1;
        while (expansion_occured == 1) {
            expansion_occured = 0;

            it = ds.intermediate_grammar_rules.entrySet().iterator();


            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String key = (String) pair.getKey();
                ArrayList<String> value = (ArrayList<String>) pair.getValue();
                ArrayList<String> new_value = new ArrayList<String>();
                for (String s : value) {
                    int expansion_occured_this_rule = 0;
                    int i = 0;
                    String components[] = s.trim().split(" ");
                    String new_entry = "";
                    for (i = 0; i < components.length; i++) {
                        if (ds.intermediate_grammar_rules.containsKey(components[i].trim())) {
                            expansion_occured_this_rule = 1;
                            break;
                        } else {
                            new_entry = new_entry.trim() + " " + components[i].trim();
                            new_entry = new_entry.trim();
                        }
                    }
                    if (expansion_occured_this_rule == 1) {
                        expansion_occured = 1;

                        for (String s1 : ds.intermediate_grammar_rules.get(components[i].trim())) {
                            String new_entry_current = new_entry;
                            int new_i = i;
                            new_entry_current = new_entry_current.trim() + " " + s1.trim();
                            new_entry_current = new_entry_current.trim();
                            new_i++;
                            for (; new_i < components.length; new_i++) {
                                new_entry_current = new_entry_current.trim() + " " + components[new_i].trim();
                                new_entry_current = new_entry_current.trim();
                            }
                            if (!new_value.contains(new_entry_current)) {
                                new_value.add(new_entry_current);
                            }
                        }
                    } else {
                        if (!new_value.contains(new_entry)) {
                            new_value.add(new_entry);
                        }
                    }
                }
                intermediate_grammar_rules_temp.put(key, new_value);
            }
            ds.intermediate_grammar_rules = intermediate_grammar_rules_temp;
        }
    }

    public void reduce_finals_to_terminals_using_intermediates() {
        HashMap<String, ArrayList<String>> final_grammar_rules_temp = new HashMap<String, ArrayList<String>>();
        Iterator it = null;
        int expansion_occured = 1;
        while (expansion_occured == 1) {
            expansion_occured = 0;

            it = ds.final_grammar_rules.entrySet().iterator();


            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String key = (String) pair.getKey();
                ArrayList<String> value = (ArrayList<String>) pair.getValue();
                ArrayList<String> new_value = new ArrayList<String>();
                for (String s : value) {
                    int expansion_occured_this_rule = 0;
                    int i = 0;
                    String components[] = s.trim().split(" ");
                    String new_entry = "";
                    for (i = 0; i < components.length; i++) {
                        if (ds.intermediate_grammar_rules.containsKey(components[i].trim())) {
                            expansion_occured_this_rule = 1;
                            break;
                        } else {
                            new_entry = new_entry.trim() + " " + components[i].trim();
                            new_entry = new_entry.trim();
                        }
                    }
                    if (expansion_occured_this_rule == 1) {
                        expansion_occured = 1;

                        for (String s1 : ds.intermediate_grammar_rules.get(components[i].trim())) {
                            String new_entry_current = new_entry;
                            int new_i = i;
                            new_entry_current = new_entry_current.trim() + " " + s1.trim();
                            new_entry_current = new_entry_current.trim();
                            new_i++;
                            for (; new_i < components.length; new_i++) {
                                new_entry_current = new_entry_current.trim() + " " + components[new_i].trim();
                                new_entry_current = new_entry_current.trim();
                            }
                            if (!new_value.contains(new_entry_current)) {
                                new_value.add(new_entry_current);
                            }
                        }
                    } else {
                        if (!new_value.contains(new_entry)) {
                            new_value.add(new_entry);
                        }
                    }
                }
                final_grammar_rules_temp.put(key, new_value);
            }
            ds.final_grammar_rules = final_grammar_rules_temp;
        }
    }

    public void reduce_finals_to_terminals_using_finals() {
        HashMap<String, ArrayList<String>> final_grammar_rules_temp = new HashMap<String, ArrayList<String>>();
        Iterator it = null;
        int expansion_occured = 1;
        while (expansion_occured == 1) {
            expansion_occured = 0;

            it = ds.final_grammar_rules.entrySet().iterator();


            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String key = (String) pair.getKey();
                ArrayList<String> value = (ArrayList<String>) pair.getValue();
                ArrayList<String> new_value = new ArrayList<String>();
                for (String s : value) {
                    int expansion_occured_this_rule = 0;
                    int i = 0;
                    String components[] = s.trim().split(" ");
                    String new_entry = "";
                    for (i = 0; i < components.length; i++) {
                        if (ds.final_grammar_rules.containsKey(components[i].trim())) {
                            expansion_occured_this_rule = 1;
                            break;
                        } else {
                            new_entry = new_entry.trim() + " " + components[i].trim();
                            new_entry = new_entry.trim();
                        }
                    }
                    if (expansion_occured_this_rule == 1) {
                        expansion_occured = 1;

                        for (String s1 : ds.final_grammar_rules.get(components[i].trim())) {
                            String new_entry_current = new_entry;
                            int new_i = i;
                            new_entry_current = new_entry_current.trim() + " " + s1.trim();
                            new_entry_current = new_entry_current.trim();
                            new_i++;
                            for (; new_i < components.length; new_i++) {
                                new_entry_current = new_entry_current.trim() + " " + components[new_i].trim();
                                new_entry_current = new_entry_current.trim();
                            }
                            if (!new_value.contains(new_entry_current)) {
                                new_value.add(new_entry_current);
                            }
                        }
                    } else {
                        if (!new_value.contains(new_entry)) {
                            new_value.add(new_entry);
                        }
                    }
                }
                final_grammar_rules_temp.put(key, new_value);
            }
            ds.final_grammar_rules = final_grammar_rules_temp;
        }
    }

    public void write_expanded_grammar() {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter("expanded_rules.txt"));
            for (String s : ds.final_grammar_rules.keySet()) {
                for (String s1 : ds.final_grammar_rules.get(s)) {
                    if (s.equalsIgnoreCase("NOMINAL") || s.equalsIgnoreCase("CARDINAL")
                            || s.equalsIgnoreCase("PRONOUN") || s.equalsIgnoreCase("ADJ")
                            || s.equalsIgnoreCase("ADVS") || s.equalsIgnoreCase("INT")
                            || s.equalsIgnoreCase("CONS") || s.equalsIgnoreCase("COMS")
                            || s.equalsIgnoreCase("AKYS") || s.equalsIgnoreCase("KRS")
                            || s.equalsIgnoreCase("PARTS") || s.equalsIgnoreCase("EOFCLS")) {
                        br.write(s + " " + s1 + "\n");
                    }
                }
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(ReadGrammar.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void expand_grammar() {
        split_by_pipe(1);
        expand_optionals_aka_question_marks(1);
        split_by_pipe(2);
        expand_optionals_aka_question_marks(2);
        reduce_intermediates_to_terminals_using_intermediates();
        reduce_finals_to_terminals_using_intermediates();
        reduce_finals_to_terminals_using_finals();
        write_expanded_grammar();
        //display_rules();
    }

    String combine_string_array(String parts[], int from, int to) {
        return null;
    }

    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    void display_rules() {
        for (String s : ds.final_grammar_rules.keySet()) {
            for (String s1 : ds.final_grammar_rules.get(s)) {
                System.out.println(s + " " + s1);
            }
        }
//        System.out.println("\n\nIntermediate Grammar Rules\n\n");
//        for (String s : ds.intermediate_grammar_rules.keySet()) {
//            for (String s1 : ds.intermediate_grammar_rules.get(s)) {
//                System.out.println(s + " " + s1);
//            }
//        }
    }
}
