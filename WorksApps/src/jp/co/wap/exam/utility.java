/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.wap.exam;

import java.util.Comparator;
import jp.co.wap.exam.lib.Interval;

/**
 *
 * @author RAJ
 */

/*
 * The class utility below contains some utility functions
 */
public class utility {

    /*
     * This method performs a binary search to search for the nearest interval with end time less than the start of the given
     * interval.
     * Run time is O(log n)
     */
    public static int binSearch(Interval intervalList[], int begin, int end, Interval keyInterval) {
        int start = begin;
        int finish = end;
        int mid = (start + finish) / 2;
        int furthestIndex = -1;
        while ((start <= finish)) {
            if (intervalList[mid].getEndMinuteUnit() <= keyInterval.getBeginMinuteUnit()) {
                start = mid + 1;
                furthestIndex = mid;
            } else {
                finish = mid - 1;
            }
            mid = (start + finish) / 2;
        }
        return furthestIndex;
    }
}

/*
 * The class markedInterval below is used for the creation of objects that can contain
 * the interval start or end information with markers +1 for start and -1 for end
 */
class markedInterval {

    private int time;
    private int begOrEnd;

    public markedInterval() {
        this.time = 0;
        this.begOrEnd = 1;
    }

    public int gettime() {
        return this.time;
    }

    public int getbegOrEnd() {
        return this.begOrEnd;
    }

    public markedInterval(int time, int begOrEnd) {
        this.time = time;
        this.begOrEnd = begOrEnd;
    }
}

/*
 * The class below implements a comparator for comparing the contents of object so that the object can be sorted
 * This class is not used at the current moment but is kept for future possible usage
 */
class TimeComparator implements Comparator<markedInterval> {

    public int compare(markedInterval o1, markedInterval o2) {
        if (o1.gettime() > o2.gettime()) {
            return 1;
        } else if ((o1.gettime() == o2.gettime()) && (o1.getbegOrEnd() < o2.getbegOrEnd())) {
            return 1;
        } else {
            return 0;
        }
    }
}

/*
 * The class below implements a comparator for comparing the end time of intervals to sort a
 * list of intervls in ascending order.
 */
class IntervalComparator implements Comparator<Interval> {

    public int compare(Interval o1, Interval o2) {
        return (o1.getEndMinuteUnit() > o2.getEndMinuteUnit()) ? 1 : 0;
    }
}
