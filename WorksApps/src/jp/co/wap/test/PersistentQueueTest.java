package jp.co.wap.test;

import jp.co.wap.exam.PersistentQueue;

public class PersistentQueueTest {

    public static void main(String args[]) {
        PersistentQueue<Integer> p = new PersistentQueue<Integer>();
        int max = 1000000;

        /* Uncomment this to start the timer
         * long current = System.currentTimeMillis();
         */

        /*
         * Add around a million random integers to the queue
         */

        for (int i = 0; i < max; i++) {
            int value = (int) (Math.random() * 100);
            p = p.enqueue(value); //Utilize the same object

        }
        /* Uncomment to find out total enqueue time for a million elements.
         * System.out.println("enqueue time:" + (System.currentTimeMillis() - current));
         * current = System.currentTimeMillis();
         */
        for (int i = max - 1; i >= 0; i--) {
            p = p.dequeue();
        }

        /* Uncomment to find out total dequeue time for a million elements. Also the final dequeue will throw an exception
         * p.dequeue();
         * System.out.println("dequeue time:" + (System.currentTimeMillis() - current));
         */
    }
}
