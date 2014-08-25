/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hinditweetcrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.net.ssl.SSLEngineResult.Status;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author RAJ
 */
public class DownloadTweets {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException, FileNotFoundException, IOException {
        // TODO code application logic here
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey("jRwqa1wlNEFYKx3RWkvQ").setOAuthConsumerSecret("I89G4nmPhI52kTcGVLsFbsvZDEl0BsPubiqlv3lQw24").setOAuthAccessToken("198210801-HRVpHFAA9VPd1xmut6URPw1EWJKh1lfYJiwW4zMs").setOAuthAccessTokenSecret("jzV1AMPCTe7LdNfsaMzivNmppPgLFqY8tQX6vqwOI");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        BufferedWriter bw = new BufferedWriter(new FileWriter("tweets.txt"));
//        try {
//            Query query = new Query("BBCHindi");
//            QueryResult result;
//            do {
//                result = twitter.search(query);
//                List<twitter4j.Status> tweets = result.getTweets();
//                for (twitter4j.Status tweet : tweets) {
//                    bw.write(tweet.getText()+"\n");
//                }
//            } while ((query = result.nextQuery()) != null);
//            System.exit(0);
//        } catch (TwitterException te) {
//            te.printStackTrace();
//            System.out.println("Failed to search tweets: " + te.getMessage());
//            System.exit(-1);
//        }
        int count=0;
        try {
        Paging paging = new Paging(1, 200);
        for (int i = 1; i < 100; i++) {
            paging.setPage(i);
            ResponseList<twitter4j.Status> statuses = twitter.getUserTimeline("TimesHindi", paging);
            count++;
            for (twitter4j.Status stat : statuses) {
                bw.write(stat.getText() +" "+ stat.getCreatedAt()+ "\n");
                bw.flush();
            }
        }
        } catch(Exception e){
            System.out.println(e);
            System.out.println(count);
        }
        bw.flush();
        bw.close();
        
    }
}
