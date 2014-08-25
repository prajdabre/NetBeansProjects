/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandhisplittermainpack.io;

import java.util.Vector;

/**
 *
 * @author RAJ
 */
public class resources {

    public static String[] vowlist = {"अ", "आ", "इ", "ई", "उ", "ऊ", "ए", "ऐ", "ओ", "ओ", "औ", "अं", "अः"};
    public static String[] diaclist = {"ा", "ि", "ी", "ु", "ू", "े", "ै", "ो", "ौ", "ं", "ः"};
    public static String rulespath = "C:/Users/RAJ/Documents/NetBeansProjects/SandhiSplitter/src/sandhisplittermainpack/io/rules";
    public static String repopath = "C:/Users/RAJ/Documents/NetBeansProjects/SandhiSplitter/src/sandhisplittermainpack/io/repo";
    public static String morphotactpath = "C:/Users/RAJ/Documents/NetBeansProjects/SandhiSplitter/src/sandhisplittermainpack/io/morphotact.txt";
    public static String halant = "्";
    public static Vector<String> diacritics;
    public static Vector<String> vowels;

    public resources() {
        diacritics = arrtovect(diaclist);
        vowels = arrtovect(vowlist);
    }

    public <T> Vector<T> arrtovect(T[] a) {
        Vector<T> vect = new Vector<T>();
        for (T t1 : a) {
            vect.add(t1);
        }
        return vect;
    }
}
