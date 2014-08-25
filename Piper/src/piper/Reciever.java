/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package piper;

import java.io.IOException;
import java.io.PipedReader;

/**
 *
 * @author RAJ
 */
class Receiver extends Thread {
      private PipedReader in;
      private char c [] = new char[10000] ;
      public Receiver(Sender sender) throws IOException {
        in = new PipedReader(sender.getPipedWriter());

      }
      public void run() {
        try {
          while(true) {
            // Blocks until characters are there:
              while(in.ready()){
                  c= new char[10000];
                in.read(c,0,10000);
              //  if(String.valueOf(c)!="")
            System.out.println("From Sender to Receiver: "+String.valueOf(c));
            
              }
          }
        } catch(IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
