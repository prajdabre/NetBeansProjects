/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.wap.test;

import java.util.Arrays;
import java.util.List;
import jp.co.wap.exam.Problem2;
import jp.co.wap.exam.lib.Interval;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;

/**
 *
 * @author RAJ
 */
public class testProblem2Usage {

    /**
     * @param args the command line arguments
     */

//    @Test
    public void testProblem2Usage() {
        //System.out.println("Here");
        Problem2 p = new Problem2();
        //Problem2 p2 = new Problem2();
// example: Figure 1

        Interval interval1 = new Interval("08:00", "12:00");
        Interval interval2 = new Interval("06:00", "09:00");
        Interval interval3 = new Interval("11:00", "13:30");
        List<Interval> figure1 = Arrays.asList(interval1, interval2, interval3);
        
        assertThat(p.getMaxWorkingTime(figure1), is(330));

// example: Figure 2
        List<Interval> figure2 = Arrays.asList(new Interval("06:00", "08:30"),
                new Interval("08:00", "09:00"), new Interval("09:00", "11:00"),
                new Interval("09:00", "11:30"), new Interval("10:30", "14:00"), new Interval("12:30", "14:00"));
        
        
        assertThat(p.getMaxWorkingTime(figure2), is(390));
    }

    /* Uncomment to test
    public static void main(String args[]){
        testProblem2Usage tp = new testProblem2Usage();
        tp.testProblem2Usage();
    }
     */
}
