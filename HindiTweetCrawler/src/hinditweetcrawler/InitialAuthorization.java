/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hinditweetcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 *
 * @author RAJ
 */
public class InitialAuthorization {

    /**
     * @param args the command line arguments
     */
    private final static String CONSUMER_KEY = "jRwqa1wlNEFYKx3RWkvQ";
    private final static String CONSUMER_KEY_SECRET = "I89G4nmPhI52kTcGVLsFbsvZDEl0BsPubiqlv3lQw24";
    private final static String Access_Token = "198210801-HRVpHFAA9VPd1xmut6URPw1EWJKh1lfYJiwW4zMs";
    private final static String Access_Token_Secret = "jzV1AMPCTe7LdNfsaMzivNmppPgLFqY8tQX6vqwOI";

    public void start() throws TwitterException, IOException {

        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        RequestToken requestToken = twitter.getOAuthRequestToken();
        System.out.println("Authorization URL: \n"
                + requestToken.getAuthorizationURL());

        AccessToken accessToken = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            try {
                System.out.print("Input PIN here: ");
                String pin = br.readLine();

                accessToken = twitter.getOAuthAccessToken(requestToken, pin);

            } catch (TwitterException te) {

                System.out.println("Failed to get access token, caused by: "
                        + te.getMessage());

                System.out.println("Retry input PIN");

            }
        }

        System.out.println("Access Token: " + accessToken.getToken());
        System.out.println("Access Token Secret: "
                + accessToken.getTokenSecret());

        twitter.updateStatus("hi.. im updating this using Namex Tweet for Demo");

    }

    public static void main(String[] args) throws TwitterException, IOException {
        // TODO code application logic here
        new InitialAuthorization().start();
    }
}
