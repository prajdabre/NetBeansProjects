/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MusicMixer.java
 *
 * Created on 10 Oct, 2012, 10:52:21 PM
 */
package music;

import javazoom.jl.decoder.JavaLayerException;
import music.MP3;

/**
 *
 * @author RAJ
 */
public class MusicMixer extends javax.swing.JFrame {

    /** Creates new form MusicMixer */
    public MusicMixer() {
        initComponents();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();

        jButton1.setText("jButton1");

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToggleButton2.setText("jToggleButton2");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton3.setText("jToggleButton3");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToggleButton3)
                    .addComponent(jToggleButton2))
                .addContainerGap(246, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jToggleButton2)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton3)
                .addContainerGap(206, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:

        if (jToggleButton2.isSelected()) {
            threadlist[21]= new musicthread("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_22.mp3");
            threadlist[21].start();
           // mp3.play("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_22.mp3");
        } else{
            threadlist[21].threadmp3.close();
            
        }
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton3.isSelected()) {
            
            threadlist[9]= new musicthread("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_10.mp3");
            threadlist[9].start();
            
           // mp3.play("C:\\Users\\RAJ\\Downloads\\Music\\Incredibox_22.mp3");
        } else{
            threadlist[9].threadmp3.close();

        }
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
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
        public musicthread(String fname){
            this.playfilethread=fname;
        }
       

        @Override
        public void run() {
            try {
                while(true)
                threadmp3.play(this.playfilethread);
            } catch (JavaLayerException ex) {
                ex.printStackTrace();
            }

        }


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    // End of variables declaration//GEN-END:variables
}
