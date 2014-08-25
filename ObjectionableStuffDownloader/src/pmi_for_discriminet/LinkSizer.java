/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pmi_for_discriminet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class LinkSizer {

    public static void main(String[] args) {

        LinkSizer ls =new LinkSizer();
        BufferedImage image = null;
        Double size =0.0;
        int counter=0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:/Users/RAJ/Documents/NetBeansProjects/PMI_FOR_DISCRIMINET/new2.txt"));
            String folder = br.readLine();
            String line="";
            while ((line = br.readLine()) != null) {
                    size = size + ls.getFileSize(new URL(line.trim()))/1024.0/1024.0/1024.0;
                    counter++;
                    System.out.println("Size till now = "+size+" GB");
                    System.out.println(counter);

            }
            }
            

            // read the url


         catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total Size= "+size+" GB");
    }
    public int getFileSize(URL url) {
    HttpURLConnection conn = null;
    try {
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.getInputStream();
        return conn.getContentLength();
    } catch (IOException e) {
        return -1;
    } finally {
        conn.disconnect();
    }
}
}
