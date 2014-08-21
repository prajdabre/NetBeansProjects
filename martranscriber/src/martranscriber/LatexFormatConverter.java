/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package martranscriber;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author RAJ
 */
public class LatexFormatConverter {

    /**
     * @param args the command line arguments
     */
    public String conversion = "";
    public static String[] vowlist = {"अ", "आ", "इ", "ई", "उ", "ऊ", "ए", "ऐ", "ओ", "ओ", "औ"};
    public static String[] diaclist = {"ा", "ि", "ी", "ु", "ू", "े", "ै", "ो", "ौ", "ं", "ः", "ँ"};
    public HashMap<String, String> trscpfwd = new HashMap<String, String>();
    public static Vector<String> diacritics;
    public static Vector<String> vowels;

    public <T> Vector<T> arrtovect(T[] a) {
        Vector<T> vect = new Vector<T>();
        for (T t1 : a) {
            vect.add(t1);
        }
        return vect;
    }

    public void populate() throws IOException {
        this.diacritics = arrtovect(diaclist);
        this.vowels = arrtovect(vowlist);

        BufferedReader rin = new BufferedReader(new FileReader(new File("./src/martranscriber/mappings")));
        String line = "";
        while ((line = rin.readLine()) != null) {
            //System.out.println(line);
            trscpfwd.put(line.split(" ")[0], line.split(" ")[1]);
        }
    }

    public void convert_to_latex(String input) {
        int start_word = 1;
        int start_consonant = 1;
        System.out.println(diacritics.contains("ू"));
        this.conversion = "";
        for (String s : input.split("")) {
            if (!s.equals("")) {
                System.out.println(s);
                if (s.equals(" ")) {
                    if (start_consonant == 1) {
                        this.conversion += "a";
                        start_consonant = 0;
                    }
                    this.conversion += " ";
                    start_word = 1;
                } else if (s.equals("्")) {
                    start_consonant = 0;
                    continue;
                } else if (vowels.contains((String) s)) {
                    this.conversion += this.trscpfwd.get(s);
                } else if (diacritics.contains((String) s)) {
                    if ((s.equals("ं") || s.equals("ः") || s.equals("ँ")) && start_consonant == 1) {
                        this.conversion += "a";
                    }
                    this.conversion += this.trscpfwd.get(s);
                    start_consonant = 0;
                } else {
                    if (start_word == 1) {
                        this.conversion += this.trscpfwd.get(s);
                        start_word = 0;
                        start_consonant = 1;
                    } else if (start_consonant == 1) {
                        this.conversion += "a" + this.trscpfwd.get(s);
                    } else {
                        this.conversion += this.trscpfwd.get(s);
                        start_consonant = 1;
                    }

                }
            }
        }
        System.out.println(this.conversion);
    }

    public void write_to_dot_df() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("devanag.dn"));
        bw.write("\n{\\dn " + this.conversion + "}" + "\n");
        bw.close();
    }

    public void run_devanag() throws IOException {
        Process process = new ProcessBuilder("./src/martranscriber/devnag.exe", "devanag.dn").start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;



        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public void get_devanag() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("devanag.tex"));
        int i = 0;
        String line = "";
        while ((line = br.readLine()) != null) {
            if (i == 0) {
                i = 1;
                continue;
            }
            System.out.println(line);
        }
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        LatexFormatConverter lfc = new LatexFormatConverter();
        lfc.populate();
        //System.out.println(lfc.trscpfwd);
        lfc.convert_to_latex("वह रोज एक फल खाने के लिए उसके घर जाता है ते रोज एक फळे खाण्यासाठी त्यांच्या घरी जात आहे ते रोज एक फळे खाण्यासाठी त्यांच्या घरी जातो ");
        lfc.write_to_dot_df();
        lfc.run_devanag();
        lfc.get_devanag();
    }
}
