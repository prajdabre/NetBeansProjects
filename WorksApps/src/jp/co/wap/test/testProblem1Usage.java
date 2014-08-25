/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.wap.test;

import java.util.Arrays;
import java.util.List;
import jp.co.wap.exam.Problem1;
import jp.co.wap.exam.lib.Interval;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;

/**
 *
 * @author RAJ
 */
public class testProblem1Usage {

    /**
     * @param args the command line arguments
     */

//    @Test
    public void testProblem1Usage() {
        //System.out.println("Here");
        Problem1 p = new Problem1();
        //Problem2 p2 = new Problem2();
// example: Figure 1

        Interval interval1 = new Interval("08:00", "12:00");
        Interval interval2 = new Interval("06:00", "09:00");
        Interval interval3 = new Interval("11:00", "13:30");
        List<Interval> figure1 = Arrays.asList(interval1, interval2, interval3);
        
        assertThat(p.getMaxIntervalOverlapCount(figure1), is(2));

// example: Figure 2
        List<Interval> figure2 = Arrays.asList(new Interval("06:00", "09:30"),
                new Interval("09:00", "12:30"), new Interval("10:00", "10:30"),
                new Interval("12:00", "14:30"), new Interval("11:00", "13:30"));
        
        
        assertThat(p.getMaxIntervalOverlapCount(figure2), is(3));
    }

    /* Uncomment to test
    public static void main(String args[]){
        testProblem1Usage tp = new testProblem1Usage();
        tp.testProblem1Usage();
    }
     */
}
