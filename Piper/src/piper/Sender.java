/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package piper;

/**
 *
 * @author RAJ
 */
    //: c13:PipedIO.java
    // Using pipes for inter-thread I/O
    import java.io.*;
    import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import livefile.Tail;
    import piper.*;

    class Sender extends Thread {
      private Random rand = new Random();
      private char c[] = new char[10000]  ;
      private PipedWriter out = new PipedWriter();
      private PipedReader in = null;
      public Sender(Tail tail) throws IOException{
          in = new PipedReader(tail.getPipedWriter());
      }

      public PipedWriter getPipedWriter() { return out; }
      public void run() {
          while(true) {
            try {
                while (in.ready()) {
                    System.out.println("Data is ready");
                    c= new char[10000];
                    in.read(c,0,10000);
                    System.out.println("From File to Sender:" + String.valueOf(c));
                    out.write(c);
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            }
                    

        }
      }
    }


