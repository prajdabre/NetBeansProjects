/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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

public class ImageDownloader {

    public static int threadno = 0;
    public static int errorcount = 0;

    public ImageDownloader(boolean useproxy, String seed, String name) {
        if (useproxy) {
            setproxy();
        }

        lethtegamesbegin(seed, name);

    }

    public static void main(String[] args) {


        int i = 0;

        String base = args[0];
        String folder = "/"+args[1];
        int error = 0;
        new File("/home/raj/public_html/imagedownloader" + folder).mkdir();
        while (true) {

            if (ImageDownloader.threadno <= 4) {
                ImageDownloader.threadno++;
                objclass temp = new objclass(base, folder, i);
                Runnable r = new MyThread(temp);
                new Thread(r).start();
                i++;
            }
            if (ImageDownloader.errorcount > 6) {
                break;
            }
        }
    }

    private void setproxy() {
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("https.proxyHost", "netmon.iitb.ac.in");
        System.setProperty("https.proxyPort", "80");

        final String authUser = "11305R001";
        final String authPassword = "01041989";
        Authenticator.setDefault(
                new Authenticator() {

                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                });

        System.setProperty("http.proxyUser", authUser);
        System.setProperty("http.proxyPassword", authPassword);
    }

    private void lethtegamesbegin(String baseurl, String foldername) {
        BufferedImage image = null;
        System.out.println(baseurl);
        System.out.println(foldername);
        String line = "";
        int i = 0;

        String base = baseurl;
        String folder = foldername;
        int error = 0;
        new File("C:/Users/RAJ/Desktop/hent/" + folder).mkdir();
        while (true) {

            if (ImageDownloader.threadno <= 2) {
                ImageDownloader.threadno++;
                objclass temp = new objclass(base, folder, i);
                Runnable r = new MyThread(temp);
                new Thread(r).start();
                i++;

            }
            if (ImageDownloader.errorcount > 3) {

                //TODO: Figure out how to stop spawning threads by one process after a number of errors but not affect
                //Thread generation of another process
                //Learn about callable

                ImageDownloader.threadno = 0;
                ImageDownloader.errorcount = 0;
                break;
            }
        }
        ImageDownloader.threadno = 0;
        ImageDownloader.errorcount = 0;

    }
    // read the url
}

class objclass {

    public String base = "";
    public String folder = "";
    public int index = 0;

    public objclass() {
    }

    public objclass(String base, String folder, int index) {
        this.base = base;
        this.folder = folder;
        this.index = index;
    }
}

class MyThread implements Runnable {

    objclass obj = null;

    public MyThread(objclass obj) {
        this.obj = obj;
    }

    public void run() {
        String base = this.obj.base;
        String folder = this.obj.folder;
        int i = this.obj.index;
        //System.out.println(base+folder+i);
        try {
            BufferedImage image = null;

            URL url = new URL(base + String.format("%03d", i) + ".jpg");
            System.out.println(url.toString());

//
            image = ImageIO.read(url);


            ImageIO.write(image, "jpg", new File("/home/raj/public_html/imagedownloader/" + folder + "/" + String.format("%03d", i) + ".jpg"));

        } catch (IOException e) {
            ImageDownloader.errorcount++;
            System.out.println("Quitting thread number " + i);



        }
        ImageDownloader.threadno--;
    }
}
