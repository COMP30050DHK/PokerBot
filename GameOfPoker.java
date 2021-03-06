package poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;

public class GameOfPoker {
	
	public static ArrayList<PokerPlayer> players = new ArrayList<PokerPlayer>();
	public static String nameOfUser;
	public static String name = "";
	public static int botNum = 0;
	public static DeckOfCards d;
	public static Status id;
	private static Twitter twit;
	private static Configuration config;
	private static String tweet;
	private static Status currentStatus;
	private static String nameArray[] ={"Joetta","Ayako","Maye","Nery","Gwenda","Shon","Krysta","Diana","Enda","Rueben","Jenniffer","Marcos","Ryan","Angelica","Azzie","Sharon","Edythe","Hunter","Evonne","Dian","Ula","Lawerence","Danilo","Mittie","Lawana","Perla","Maxine","Jeromy","Melida","Carmelita","Conception","Rheba","Kimberlie","Brendon","Serena","Julissa","India","Yasmin","Randolph","Helen","Shalonda","Mason","Loralee","Renae","Waneta","Kamala","Zenaida","Stevie","Taneka","Zoe"};
			
	
	//Constructor sets up the game
	public GameOfPoker(String name, int botNum, String userName, Status statusID, Twitter twitter, Configuration configuration){
		nameOfUser = userName;
		id = statusID;
		twit = twitter;
		config = configuration;
		DeckOfCards d = new DeckOfCards();	
  
    	List<String> names = new ArrayList<String>();
    	for(int i=0; i<nameArray.length; i++){
    	     names.add(nameArray[i]);
    	}
    	
    	Random rand = new Random();
	    name = "You";
	    //Creating players here	   
		PokerPlayer human = new HumanPokerPlayer(d, name);
		players.add(human);
		
		for (int i=0;i<botNum;i++){
			PokerPlayer bot = new AutomatedPokerPlayer(d, names.remove(rand.nextInt(names.size())), Math.random(), Math.random());
			players.add(bot);
		}
	}
	
	//plays through a game of poker
	public void playGame() throws TwitterException, InterruptedException{
		boolean playOn = true;
		int result = 0;
		
		while(playOn){

			HandOfPoker pokerHand = new HandOfPoker(d, players, twit, config, nameOfUser, id);
			//executes 1 hand of poker
			id = pokerHand.executeHandOfPoker();
			
			//important to check in reverse so no player is skipped if one is removed
			for(int i=players.size()-1; i>=0; i--){
				removePlayerCheck(i);
			}
		
			rotateOpeningPlayer();
			result = playAnotherRound();
			if (result!=1){
				playOn = false;
			}
		}
		switch(result){
			case 0:
				tweet+="\nCongrats, you have won! "+"\uD83C\uDF89";
			case -1:
				tweet+="\nYou have run out of chips and are removed from the game!"+"\uD83D\uDE2D";
			default:
				tweet="\nThanks for Playing! " + "\uD83D\uDC4B";
		}
		
		while(tweet.length()+nameOfUser.length() > 130){
			
			int i = tweet.indexOf("\n");

			tweet = tweet.substring(i+1);
		
		}
		
		tweet = "...\n" + tweet;	
		
		String newTweet = ("@" + nameOfUser + tweet);
		tweet = "";
		StatusUpdate replyStatus = new StatusUpdate(newTweet);
		replyStatus.setInReplyToStatusId(id.getId());
		 
	    try {
			Status currentStatus = twit.updateStatus(replyStatus);	
			return;
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/* returns 1 if the human player wishes to play another round and can,
	 *  0 if the human player has no opponents remaining 
	 *  -1 0 if the human player has no chips remaining or
	 *  -2 if the human player does not want to play another round
	 */
	private static int playAnotherRound(){
		
		boolean humanLeft=false;
		
		if (players.size()==1 && players.get(0).isHuman()){
			return 0;
		}
		for (int i=0;i<players.size();i++){
			if (players.get(i).isHuman()){
				humanLeft=true;
			}
		}
		
		if(!humanLeft){
			return -1;
		}
		
		boolean validInput = false;
		while(!validInput){
			
			String input = id.getText().toLowerCase();
			
			if (input.contains("n")){
				validInput=true;
				return -2;
			}
			else if (input.contains("y")){
				validInput=true;
				return 1;
			}
			else{
				tweet = ">>INVALID INPUT ('y' or 'n'):";
				String newTweet = ("@" + nameOfUser + tweet);
				tweet = "";
				StatusUpdate replyStatus = new StatusUpdate(newTweet);
				replyStatus.setInReplyToStatusId(id.getId());
				 
			    try {
					currentStatus = twit.updateStatus(replyStatus);	
					
					
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    id = findReply(currentStatus.getId());
			    
			}
			
			}
		
		return -1;
	}
	    
	//checks if any players have 0 chips left and eliminates them
	private static void removePlayerCheck(int i){
		if(players.get(i).isBust()){
			tweet+="\n" + players.get(i).name+" eliminated";
			players.remove(i);
			botNum--;
		}
	}
	
	//rotates which player starts the betting
	private static void rotateOpeningPlayer(){
		PokerPlayer rotate = players.remove(0);
		players.add(rotate);
	}
	
public static Status findReply(long currentId) {
		
		long searchingFor = currentId;
        
        
        ResponseList<Status> tweets = null;
		
        while (true) {
        	
    		try {
    			tweets = twit.getMentionsTimeline();
    		} catch (TwitterException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
            //get the most recent tweet
            for(int i = 0; i < tweets.size(); i++) {

                if(tweets.get(i).getInReplyToStatusId()==searchingFor) {
	                    return tweets.get(i);
                    }
                }

            //wait 10 seconds
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ie) {
                  //Handle exception

           }
        }
    }
	
	
	
	    
//	public static void main(String[] args){
		
		//GameOfPoker game = new GameOfPoker();
	//	game.playGame();
	
//	}
	
}