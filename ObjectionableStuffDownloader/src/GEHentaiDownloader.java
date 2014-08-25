/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Response;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author RAJ
 */
public class GEHentaiDownloader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
    }

    public void gehentai_download(String seed, String downloadpath) {
        try {
            //String base = "http://g.e-hentai.org/s/";
            //String[] type = new String[]{"thumbs/", "view/"};
            //int seed_len = seed.split("/").length;
            String prev_path = "-1";
            String curr_path = seed;
            Document doc = Jsoup.connect(curr_path).userAgent("Mozilla").timeout(5000000).get();
            Elements names = (Elements) doc.select("h1");
            //System.out.println(names);
            String folder_name = "";
            for (Element name : names) {
                folder_name = name.text().replaceAll("[\\-\\+\\.\\^:,|]", "");
                System.out.println("Extracting folder name: "+folder_name);
                new File(downloadpath+"/"+folder_name).mkdir();
                break;
            }
            Elements links = (Elements) doc.select("img[id]");
            while (true) {
                
                for (Element link : links) {
                    String img_url = link.attr("src");
                    final URL url = new URL(img_url);
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla");
                    final BufferedImage image = ImageIO.read(connection.getInputStream());
                    File image_file = new File(downloadpath + "/" + folder_name + "/" + img_url.split("/")[img_url.split("/").length - 1]);
                    if (!image_file.exists()) {
                        ImageIO.write(image, "jpg", image_file);
                    } else {
                        System.out.println("Skipped " + url);
                    }
                    prev_path = curr_path;
                    curr_path = link.parent().attr("href");
                    

                }
                if(curr_path.equals(prev_path)){
                        break;
                    }
                doc = Jsoup.connect(curr_path).userAgent("Mozilla").timeout(5000000).get();
                links = (Elements) doc.select("img[id]");
            }

        } catch (IOException ex) {
            Logger.getLogger(PururinDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
