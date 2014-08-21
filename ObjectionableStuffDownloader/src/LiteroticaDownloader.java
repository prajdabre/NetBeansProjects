/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

/**
 *
 * @author RAJ
 */
public class LiteroticaDownloader {

    /**
     * @param args the command line arguments
     */
    public void download_story(String url_in, String author, String story_name, String path) {
        BufferedWriter bw = null;
        try {
            int i = 1;
            String url = url_in;
            int limit = -1;
            bw = new BufferedWriter(new FileWriter(new File(path+"/"+author + "/" + url.split("/")[url.split("/").length - 1].replaceAll("-", " ") + ".txt")));
            bw.write(story_name + "\n" + author + "\n\n");
            while (true) {
                Document doc = Jsoup.connect(url + "?page=" + String.valueOf(i)).timeout(50000).get();
                System.out.println(doc.baseUri());
                Elements div = doc.body().select("div[class]");
                Elements options = doc.body().select("option[value]");
                if (i == 1) {
                    for (Element option : options) {
                        //System.out.println(option.attr("value"));
                        limit = (Integer.parseInt(option.attr("value")) > limit) ? Integer.parseInt(option.attr("value")) : limit;
                    }
                }
                i++;
                //System.out.println(div.size());
                for (Element e : div) {
                    if (e.attr("class").equals("b-story-body-x x-r15")) {
                        Element text = e.child(0).child(0);
                        for (TextNode tn : text.textNodes()) {
                            if (tn.text().trim().equals("")) {
                                continue;
                            }
                            bw.write(tn.text().trim() + "\n");
                            bw.flush();
                        }
                    }
                }
                if (i > limit) {
                    break;
                }
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(LiteroticaDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(LiteroticaDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void download_by_author(String url_in, String path) {
        try {
            Document doc = Jsoup.connect(url_in).timeout(50000).get();
            String author = doc.title().split("-")[2].trim();
            System.out.println("Downloading story by author: " + author);
            File folder = new File(path + "/" + author);
            if (!folder.exists()) {
                folder.mkdir();
            }
            Elements a = doc.body().select("a[class]");
            for (Element url : a) {
                //System.out.println(option.attr("value"));
                if (url.attr("class").equals("bb") || url.attr("class").equals("t-t84 bb nobck")) {
                    String storyname = "http:" + url.attr("href");
                    System.out.println("Downloading story: " + url.text());
                    download_story(storyname, author, url.text(), path);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LiteroticaDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
        // TODO code application logic here
        LiteroticaDownloader lit_dwn = new LiteroticaDownloader();
        //lit_dwn.download_story("http://www.literotica.com/s/wives-on-vacation-anns-massage");
        //lit_dwn.download_by_author("http://www.literotica.com/stories/memberpage.php?uid=35663&page=submissions");

    }
}
