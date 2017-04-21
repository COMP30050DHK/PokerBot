package poker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


 
public class NamexTweet {
	
    private final static String CONSUMER_KEY = "rMY9LFF8ipfGSV6rDPQX8cL4o";
    private final static String CONSUMER_KEY_SECRET = "6MKe9VFzQp0UGP0AFZgUqMJr4ZEEATAmW7DYgNkXfqL8EUqHSH";
 
    public void start() throws TwitterException, IOException {
    	ConfigurationBuilder builder = new ConfigurationBuilder();
    	builder.setOAuthConsumerKey(CONSUMER_KEY);
    	builder.setOAuthConsumerSecret(CONSUMER_KEY_SECRET);
    	Configuration configuration = builder.build();
    	TwitterFactory factory = new TwitterFactory(configuration);
    	Twitter twitter = factory.getInstance();
    	//Twitter twitter = new TwitterFactory().getInstance();
    	//System.out.println("key:" + twitter.getConfiguration().getOAuthConsumerKey());
    	//System.out.println("secret: " + twitter.getConfiguration().getOAuthConsumerSecret());
    	 //twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
    	 
    	 String accessToken = "852105995459448833-Fh2rnK3hjvZSpP9BUJUNsNVCCA9h9fO";
    	 String accessTokenSecret = "5EEg95y3yGuLy2G2muuVlhdCSPBeumtfgq1BHTjy70y5n";
    	 AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
    	 
    	 twitter.setOAuthAccessToken(oathAccessToken);
    	 

    	 StatusUpdate replyStatus = new StatusUpdate("Thanks for using our hashtag!");
    	 Query query = new Query("#dealmeindhk");

    	 query.count(1); //  Query will return only 2 tweet per request.

    	 QueryResult queryResultObject = twitter.search(query);

    	 List<Status> qrTweets = queryResultObject.getTweets();
    	 long test = qrTweets.get(0).getId();
    	 replyStatus.setInReplyToStatusId(test);
    	 twitter.updateStatus(replyStatus);

    	 
    }
 
    public static void main(String[] args) throws Exception {
 new NamexTweet().start();// run the Twitter client
 
    }
}
