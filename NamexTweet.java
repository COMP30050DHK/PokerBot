package poker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

 
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
 
public class NamexTweet {
    private final static String CONSUMER_KEY = "rMY9LFF8ipfGSV6rDPQX8cL4o";
    private final static String CONSUMER_KEY_SECRET = "6MKe9VFzQp0UGP0AFZgUqMJr4ZEEATAmW7DYgNkXfqL8EUqHSH";
 
    public void start() throws TwitterException, IOException {

 
 Twitter twitter = new TwitterFactory().getInstance();
 twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
 
 String accessToken = "852105995459448833-Fh2rnK3hjvZSpP9BUJUNsNVCCA9h9fO";
 String accessTokenSecret = "5EEg95y3yGuLy2G2muuVlhdCSPBeumtfgq1BHTjy70y5n";
 AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
 
 twitter.setOAuthAccessToken(oathAccessToken);

 twitter.updateStatus("Second Tweet!");
 
    }
 
    public static void main(String[] args) throws Exception {
 new NamexTweet().start();// run the Twitter client
    }
}
