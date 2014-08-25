package jp.co.wap.exam;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jp.co.wap.exam.lib.Interval;

public class Problem2 {

    /*
     * The following method returns the maximum working time for an employee using dynamic programming.
     * 1. First the list of intervals are sorted in increasing order of end time of intervals : O(n * log n)
     * 2. Create an array to record maximum time allocatable till and end interval time
     * 3. For each sorted interval find the closest interval (denoted by index) whose end time is lesser than the start time of the current interval
     *      i. Update the maximum time for the end time of the given interval according to the below formula:
     *      ii. max[i] = maxumum (max[index]+interval[i], max[i-1]); if index is valid
     *                 = maxumum (interval[i], max[i-1]); if index is invalid
     * 4. For each interval, a binary search is used. Thus complexity is O( n * log n)
     *
     * The overall complexity is O( n * log n)
     */
    public int getMaxWorkingTime(List<Interval> intervals) {
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

        if (intervals.size()>10000) { //Intervals must not be more than 10000;
            try {
                throw new Exception("The the number of intervals are too many. Upto 10000 intervals only.");
            } catch (Exception ex) {
                Logger.getLogger(Problem1.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

        Interval intervalList[] = new Interval[intervals.size()]; //Copy intervals to arraylist for sorting
        for (int i = 0; i < intervals.size(); i++) {
            intervalList[i] = intervals.get(i);
        }

        Arrays.sort(intervalList, (new IntervalComparator())); //Sort in ascending order of end time of intervals

        int maximumTimeAllotable[] = new int[intervals.size() + 1]; //Array to record maximum time till end time of interval i

        maximumTimeAllotable[0] = 0; //When no interval is selected the maximum time is 0

        for (int i = 0; i < intervals.size(); i++) {
            int index = utility.binSearch(intervalList, 0, i - 1, intervalList[i]); // Get nearest suitable interval

            maximumTimeAllotable[i + 1] = Math.max(intervalList[i].getIntervalMinute() + maximumTimeAllotable[index + 1], maximumTimeAllotable[i]);
            // Update maximuk time possible
            // maxallotable[current] = max(maxallotable[current-1],maxallotable[index]+intervalduration[i]); if index is valid
            // maxallotable[current] = max(maxallotable[current-1],intervalduration[i]); if index is invalid
        }

        return maximumTimeAllotable[maximumTimeAllotable.length - 1]; //Maximum allocatable time is at the end
    }
}
