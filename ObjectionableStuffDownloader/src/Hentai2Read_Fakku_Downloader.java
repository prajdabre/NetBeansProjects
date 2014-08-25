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

public class Hentai2Read_Fakku_Downloader {

    public static int threadno = 0;
    public static int errorcount = 0;

    public Hentai2Read_Fakku_Downloader(boolean useproxy, String downloadpath, String seed, String name) {
        if (useproxy) {
            setproxy();
        }

        lethtegamesbegin(downloadpath, seed, name);

    }

    public static void main(String[] args) {
//        System.setProperty("java.net.useSystemProxies", "true");
//        System.setProperty("https.proxyHost", "netmon.iitb.ac.in");
//        System.setProperty("https.proxyPort", "80");
//
//        final String authUser = "11305R001";
//        final String authPassword = "01041989";
//        Authenticator.setDefault(
//                new Authenticator() {
//
//                    public PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(
//                                authUser, authPassword.toCharArray());
//                    }
//                });
//
//        System.setProperty("http.proxyUser", authUser);
//        System.setProperty("http.proxyPassword", authPassword);
//        BufferedImage image = null;
//
//        String line = "";
//        int i = 0;
////            BufferedReader br = new BufferedReader(new FileReader("C:/hent/urls.txt"));
////            String folder = br.readLine();
//
////            while ((line = br.readLine()) != null) {
////                URL url = new URL(line);
//////                HttpURLConnection connection = null;
//////                connection = (HttpURLConnection) url.openConnection();
//////                //Set request to header to reduce load as Subirkumarsao said.
//////                connection.setRequestMethod("HEAD");
//////                int code = connection.getResponseCode();
//////                System.out.println("" + code);
//////                //System.out.println(url.openStream().available());
////                image = ImageIO.read(url);
////
////                i++;
////                ImageIO.write(image, "jpg", new File("C:/hent/" + folder + "/" + String.valueOf(i) + ".jpg"));
////            }
////        String base = "http://hentai2read.com/wp-content/hentai/6173/7/";
////
////        while (true) {
////            try {
////                URL url = new URL(base + String.format("%03d", i) + ".jpg");
////
////                image = ImageIO.read(url);
////
////
////                ImageIO.write(image, "jpg", new File("C:/hent/" + folder + "/" + String.format("%03d", i) + ".jpg"));
////                i++;
////            } catch (IOException e) {
////                if (error == 1) {
////                    System.out.println("Quitting");
////                    break;
////                } else {
////                    error = 1;
////                }
////                //System.out.println(i);
////                i++;
////
////                continue;
////            }
////        }
//        String base = "http://z.mfcdn.net/store/manga/5259/TBD-265.0/compressed/kas_kuroko_no_basket_265_";
//        String folder = "/knb265";
//        int error = 0;
//        new File("C:/Users/RAJ/Desktop/hent" + folder).mkdir();
//        while (true) {
//
//            if (Hentai2Read_Fakku_Downloader.threadno <= 4) {
//                Hentai2Read_Fakku_Downloader.threadno++;
//                objclass temp = new objclass(base, folder, i);
//                Runnable r = new MyThread(temp);
//                new Thread(r).start();
//                i++;
//            }
//            if (Hentai2Read_Fakku_Downloader.errorcount > 6) {
//                break;
//            }
//        }
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

    private void lethtegamesbegin(String downloadpath, String baseurl, String foldername) {
        BufferedImage image = null;
        System.out.println(baseurl);
        System.out.println(foldername);
        String line = "";
        int i = 0;

        String base = baseurl;
        String folder = foldername;
        int error = 0;
        new File(downloadpath + "/" + folder).mkdir();
        while (true) {

            if (Hentai2Read_Fakku_Downloader.threadno <= 2) {
                Hentai2Read_Fakku_Downloader.threadno++;
                objclass temp = new objclass(downloadpath, base, folder, i);
                Runnable r = new MyThread(temp);
                new Thread(r).start();
                i++;

            }
            if (Hentai2Read_Fakku_Downloader.errorcount > 3) {

                //TODO: Figure out how to stop spawning threads by one process after a number of errors but not affect
                //Thread generation of another process
                //Learn about callable

                Hentai2Read_Fakku_Downloader.threadno = 0;
                Hentai2Read_Fakku_Downloader.errorcount = 0;
                break;
            }
        }
        Hentai2Read_Fakku_Downloader.threadno = 0;
        Hentai2Read_Fakku_Downloader.errorcount = 0;

    }
    // read the url
}

class objclass {

    public String base = "";
    public String folder = "";
    public String downloadpath = "";
    public int index = 0;

    public objclass() {
    }

    public objclass(String downloadpath, String base, String folder, int index) {
        this.base = base;
        this.folder = folder;
        this.index = index;
        this.downloadpath = downloadpath;
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
        String downloadpath = this.obj.downloadpath;
        int i = this.obj.index;
        //System.out.println(base+folder+i);
        try {
            BufferedImage image = null;


            URL url = new URL(base + String.format("%03d", i) + ".jpg");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla");

            System.out.println(url.toString());

//
            image = ImageIO.read(connection.getInputStream());


            ImageIO.write(image, "jpg", new File(downloadpath + "/" + folder + "/" + String.format("%03d", i) + ".jpg"));

        } catch (IOException e) {
            Hentai2Read_Fakku_Downloader.errorcount++;
            System.out.println(e);
            System.out.println("Quitting thread number " + i);



        }
        Hentai2Read_Fakku_Downloader.threadno--;
    }
}
