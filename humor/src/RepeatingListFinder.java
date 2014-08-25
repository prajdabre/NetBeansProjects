

import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


interface Processor<T> {
    public void process(T element);
}

public class RepeatingListFinder<T> implements Processor<T> {

    private List<T> unit_sequence = new ArrayList<T>();
    private int repeat_count = 0;
    private int partial_matches = 0;
    private Iterator<T> iterator = null;

    /* Class invariant:
     *
     * The sequence of elements passed through process()
     * can be expressed as the concatenation of
     *        the unit_sequence repeated "repeat_count" times,
     *   plus the first "element_matches" of the unit_sequence.
     *
     * The iterator points to the remaining elements of the unit_sequence,
     * or null if there have not been any elements processed yet.
     */

    public void process(T element) {
        if (unit_sequence.isEmpty() || !iterator.next().equals(element))
        {
            revise_unit_sequence(element);
            iterator = unit_sequence.iterator();
            repeat_count = 1;
            partial_matches = 0;
        }
        else if (!iterator.hasNext())
        {
            iterator = unit_sequence.iterator();
            ++repeat_count;
            partial_matches = 0;
        }
        else
        {
            ++partial_matches;
        }
    }

    /* Unit sequence has changed.
     * Restructure and add the new non-matching element.
     */
    private void revise_unit_sequence(T element) {
        if (repeat_count > 1 || partial_matches > 0)
        {
            List<T> new_sequence = new ArrayList<T>();
            for (int i = 0; i < repeat_count; ++i)
                new_sequence.addAll(unit_sequence);
            new_sequence.addAll(
                    unit_sequence.subList(0, partial_matches));

            unit_sequence = new_sequence;
        }
        unit_sequence.add(element);
    }

    public List<T> getUnitSequence() {
        return Collections.unmodifiableList(unit_sequence);
    }
    public int getRepeatCount() { return repeat_count; }
    public int getPartialMatchCount() { return partial_matches; }
    public String toString()
    {
        return "("+getRepeatCount()
        +(getPartialMatchCount() > 0
            ? (" "+getPartialMatchCount()
                +"/"+unit_sequence.size())
            : "")
        +") x "+unit_sequence;
    }

    /********** static methods below for testing **********/

    static public List<Character> stringToCharList(String s)
    {
        List<Character> result = new ArrayList<Character>();
        for (char c : s.toCharArray())
            result.add(c);
        return result;
    }

    static public <T> void test(List<T> list)
    {
        RepeatingListFinder<T> listFinder
            = new RepeatingListFinder<T>();
        for (T element : list)
            listFinder.process(element);
        System.out.println(listFinder);
    }

    static public void test(String testCase)
    {
        test(stringToCharList(testCase));
    }
public static String arrayToString(String[] a, String separator) {
    String result = "";
    if (a.length > 0) {
        result = a[0];    // start with the first element
        for (int i=1; i<a.length; i++) {
            result = result + separator + a[i];
        }
    }
    return result;
}
    static public void main(String[] args)
    {
        test("ABC ABC ABC ABC");
        test("ABCDFTBAT");
        test("ABABA");
        test("ABACABADABACABAEABACABADABACABAEABACABADABAC");
        test("ABCABCABCDEFABCABCABCDEFABCABCABCDEF");
        test("ABABCABABCABABDABABDABABC");
        String[] s="when i sit around the house i sit around the house".split(" ");
        for(int i=0;i<s.length;i++){
            String s1=arrayToString(Arrays.copyOfRange(s, i, s.length), " ");
            System.out.println(s1);
            test(s1);
//           String s="i sit around the house xanadu i sit around the house";
//           test(s);
//           String[] s1=s.split(" ");
//           for(int i=0;i<s1.length;i++){
//
//           if(s.split(s1[i]).length>1){
//
//           }

           }
        }
        
    }

