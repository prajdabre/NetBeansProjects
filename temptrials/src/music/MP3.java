package music;


/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class MP3 {
    private String filename;
    private Player player;

    // constructor that takes the name of an MP3 file
    public MP3() {
       
    }

    public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play(String filename) throws JavaLayerException {
        try {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        
                    
                    player.play();





    }


    // test client
    public static void main(String[] args) throws JavaLayerException {
        String filename = "C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_20.mp3";
        MP3 mp3 = new MP3();
        mp3.play(filename);

        // do whatever computation you like, while music plays
        int N = 4000;
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += Math.sin(i + j);
            }
        }
        System.out.println(sum);

        // when the computation is done, stop playing it
        mp3.close();

        // play from the beginning

    }

}

/*
 *
 * private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        if (jToggleButton2.isSelected()) {
            threadlist[21]= new musicthread("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_22.mp3");
            threadlist[21].start();
           // mp3.play("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_22.mp3");
        } else{
            threadlist[21].mediaPlayer.stop();

        }
    }

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (jToggleButton3.isSelected()) {
            threadlist[9]= new musicthread("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_10.mp3");
            threadlist[9].start();
           // mp3.play("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_22.mp3");
        } else{
            threadlist[9].mediaPlayer.stop();

        }
    }

  
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MusicMixer().setVisible(true);
            }
        });
    }
    String playfile = "C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_22.mp3";
    MP3 mp3 = new MP3();
    String s[] = new String[23];
    int j = 0;
    public musicthread threadlist[] = new musicthread[22];

    public class musicthread extends Thread{
        public String playfilethread="";
        public MP3 threadmp3 = new MP3();
        Media hit;
MediaPlayer mediaPlayer;

        public musicthread(String fname){
            this.playfilethread=fname;
            hit = new Media(this.playfilethread);
            mediaPlayer = new MediaPlayer(hit);
        }


        @Override
        public void run() {
            try {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

 */