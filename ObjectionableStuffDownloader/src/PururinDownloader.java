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
public class PururinDownloader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        


    }

    public void pururin_download(String seed,String downloadpath){
        try {
            String base = "http://pururin.com/";
            String[] type = new String[]{"thumbs/", "view/"};
            int seed_len = seed.split("/").length;
            String ending = seed.split("/")[seed_len-2]+"/"+seed.split("/")[seed_len-1];
            String thumbs_url = base + type[0] + ending;
            Document doc = Jsoup.connect(thumbs_url).userAgent("Mozilla").timeout(5000000).get();
            String folder = ending.split("/")[1].split("\\.")[0];
            Elements links = (Elements) doc.select("a[href]");
            for (Element link : links) {
                String image_page = link.attr("href");
                if (image_page.contains("/view/")) {
                    //System.out.println(image_page);
                    Document image_doc = Jsoup.connect(base + image_page).userAgent("Mozilla").timeout(5000000).get();
                    Elements img_links = (Elements) image_doc.select("img[src]");
                    for (Element img_link : img_links) {
                        String img_url = img_link.attr("src");
                        if (img_url.contains("/f/")) {
                            System.out.println(base + img_url);
                            new File(downloadpath+"/" + folder).mkdir();
                            //BufferedImage image = ImageIO.read(new URL(base + img_url));
                            final URL url = new URL(base + img_url);
                            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestProperty("User-Agent", "Mozilla");
                            final BufferedImage image = ImageIO.read(connection.getInputStream());
                            File image_file = new File(downloadpath+ "/" + folder + "/" + img_url.split("/")[img_url.split("/").length - 1]);
                            if (!image_file.exists()) {
                                ImageIO.write(image, "jpg", image_file);
                            } else {
                                System.out.println("Skipped " + url);
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PururinDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
