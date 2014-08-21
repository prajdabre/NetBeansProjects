/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package piper;

import java.io.IOException;
import java.util.*;
import livefile.Tail;

/**
 *
 * @author RAJ
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Tail tail = new Tail("C:\\Users\\RAJ\\Documents\\NetBeansProjects\\Piper\\build\\classes\\livefile\\input.txt");
        Sender sender = new Sender(tail);
        Receiver receiver = new Receiver(sender);
        sender.start();
        receiver.start();
        new Timeout(4000, "Terminated");

    }

}
