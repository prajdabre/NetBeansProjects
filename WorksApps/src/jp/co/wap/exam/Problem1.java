package jp.co.wap.exam;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jp.co.wap.exam.lib.Interval;

public class Problem1 {

    /*
     * The following method returns the maximum number of overlapping intervals from the given list
     * An auxiliary arrat is created which contains the start time and end time of intervals separately
     * Start time is marked with a +1 and the End time are marked with -1
     * This array is sorted in ascending order irrespective of start and end time. This takes O(n * log n)
     * Then for each start and end time being equal put the start time after the end time. This is a part of the sort.
     * 1. Go from left to right in this sorted array and:
     *  i. Increase the counter of overlaps if its the start of an interval
     *      a. If this count is the greatest till now then record it
     *  ii. Decrease the counter of overlaps if its the end of an interval
     * This takes O(n)
     * In the end, the maximum number of overlaps are recorded.
     * The overall complexity is O(n * log n)
     */
    public int getMaxIntervalOverlapCount(List<Interval> intervals) {
// TODO: Implement this method.
        
        if (intervals == null || intervals.isEmpty()) { //Argument nust not be null
            return 0;
        }

        if (intervals.contains(null)) { //Intervals must not contain null;
            try {
                throw new Exception("The intervals in the list must not be null objects");
            } catch (Exception ex) {
                Logger.getLogger(Problem1.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }
        int maxOverlap = 0, overlapTillNow = 0;

        markedInterval intervallist[] = new markedInterval[intervals.size() * 2]; //An array containing start and end points
        for (int i = 0; i < intervals.size(); i++) {
            intervallist[2 * i] = new markedInterval(intervals.get(i).getBeginMinuteUnit(), 1); //Start points marked by +1
            intervallist[2 * i + 1] = new markedInterval(intervals.get(i).getEndMinuteUnit(), -1); //End points marked by -1
        }

        /* Uncomment to see the unsorted intervals
        for(markedInterval i: intervallist){
        System.out.print(i.time+" "+i.begOrEnd+";");
        }
        //System.out.println("");
         */

        Arrays.sort(intervallist, (new TimeComparator())); //Sort the list of intervals; if start and end points are same then
                                                           //put end point before start point in the sort: O(n * log n)


        /* Uncomment to see the sorted intervals
        for(markedInterval i: intervallist){
        System.out.print(i.time+" "+i.begOrEnd+";");
        }
         */


        /*
         * This loop goes over the sorted list and every time it sees a "interval start marker" the counter is increased by 1
         * For an "interval end marker" the counter is decreased by 1
         * If the counter is greater than beore then record it in another variable
         */
        for (int i = 0; i < intervallist.length; i++) {
            if (intervallist[i].getbegOrEnd() == 1) {
                overlapTillNow++;
                if (overlapTillNow > maxOverlap) {
                    maxOverlap = overlapTillNow; //Maximum number of overlapping intervals have increased
                }
            } else if (intervallist[i].getbegOrEnd() == -1) {
                overlapTillNow--;
            }

        }

        return maxOverlap; //Returns 1 when no intervals overlap
    }
}
