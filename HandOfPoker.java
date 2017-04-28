package poker;


import java.awt.List;
import java.util.ArrayList;
import java.util.Scanner;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class HandOfPoker {

	protected int lastBet = 0;
	int state = 0;
	protected int pot = 0;
	protected boolean open = false;
	private ArrayList<PokerPlayer> pokerPlayers = new ArrayList<PokerPlayer>();
	private ArrayList<PokerPlayer> playersIn = new ArrayList<PokerPlayer>();
	private boolean cleanRound = false;
	private boolean roundOver = false;
	private int clean = 0;
	private int needToCall = 0;
	private int cantOpen = 0;
	PokerPlayer winner;
	DeckOfCards deck;
	Scanner scanner;
	
	private static Twitter twit;
	private static Configuration config;
	private static TwitterStream twitterStream;
	private static String tweet = ("@");
	public static String userName = "";
	private static long tweetId;


	public HandOfPoker(DeckOfCards d, ArrayList<PokerPlayer> players, Twitter twitter, Configuration configuration, String name, long id) {
		
		userName = name;
		deck = d;
		pokerPlayers.addAll(players);
		playersIn.addAll(players);
		scanner = new Scanner(System.in);
		twit = twitter;
		config = configuration;
		tweetId = id;
		TwitterStream twitterStream = new TwitterStreamFactory(config).getInstance();
		printPlayerChips();
		
	}
	
	public void executeHandOfPoker(){

		newHandCycle();
		discardCycle();
		


		
	
		//ready to start the betting cycle
		//betting stops whenever clean round is true
		//becomes true when there has been a full rotation of calling/seeing
		cleanRound = false;
		
		while(cleanRound!=true){
			bettingRound();
			if(roundOver){
				return;
			}
		}
		
		showCards();
		returnCards();
		
		if(!playersIn.isEmpty()){
			
			winner = playersIn.get(decideWinner());
			winner.setNumberOfChips(pot);
			System.out.println("\n" + winner.name + " won " + pot + " chips");
			playersIn.clear();
			playersIn.addAll(pokerPlayers);
		}
		
		printPlayerChips();
		
		return;
		
	}
	
	private void returnCards(){
		for (int i=0;i<pokerPlayers.size();i++){
			pokerPlayers.get(i).returnCards();
		}
	}

	public void printPlayerChips() {
		
		System.out.println("\n\n\n\n\n user name" + userName + "\n\n\n\n\n\n");
		
		tweet+=userName;
		
		System.out.println("\n\n\n\n\n tweet " + tweet + "\n\n\n\n\n\n");
		
		tweet+=(" CHIPS\n");
		
		System.out.println("\n>> CHIP LISTINGS\n");
		
		for (int i = 0; i<playersIn.size(); i++) {
			tweet+=">" + playersIn.get(i).name + ": " + playersIn.get(i).numberOfChips + " chip(s)\n";
			System.out.println("> " + playersIn.get(i).name + " has " + playersIn.get(i).numberOfChips + " chip(s) in the bank");
		}
	}
	
	//dealing all players a new hand
	public void newHandCycle(){
		
		tweet+= "\nDEALING\n";
		
		System.out.println("\n>DEALING\n");
		
		for (int i = 0; i < playersIn.size(); i++) {
			//won't owe anything at start of new round
			playersIn.get(i).amountToCall=0;
			playersIn.get(i).newHand();
			if(playersIn.get(i).isHuman()){
				tweet+=playersIn.get(i).hand.toString();
				System.out.println(playersIn.get(i).hand.toString());
			}
		}	
	}
	
	//asking all players if they want to discard
	public void discardCycle(){
		for (int i = 0; i<playersIn.size(); i++) {

			if(playersIn.get(i).isHuman()){
				System.out.println(playersIn.get(i).hand.toString());
				tweet+="Discard? (enter n or 0,1,3): ";
				System.out.println("Discard cards? (enter n or 0,1,3): ");
				
				
				StatusUpdate replyStatus = new StatusUpdate(tweet);
				 replyStatus.setInReplyToStatusId(tweetId);
				 
				 
				 System.out.println("\n\n\n\n\n" + replyStatus.getPlaceId());
				 
				 
			    try {
					twit.updateStatus(replyStatus);	
					
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    
			    
			    ArrayList<Status> tweets = findReplies();
			    
			    System.out.println(tweets);
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				boolean validInput = false;
				while(!validInput){
					validInput = ((HumanPokerPlayer) playersIn.get(i)).discard(scanner.nextLine());
					if(!validInput){
						System.out.println("Invalid input, enter n or a sequence of numbers (Ex: 0,1,3): ");
					}
				}
			} else {
				((AutomatedPokerPlayer) playersIn.get(i)).discard();
			}
		}
	}

	public void bettingRound() {
		
		
		//going to cycle through all players for a round of betting
		for (int i = 0; i<playersIn.size(); i++) {
			
			if(playersIn.size()==1){
				cleanRound=true;
				return;
			}
			
			//means everyone has called in this round and betting will stop
			if(clean >= playersIn.size()){
				cleanRound = true;
				return;
			}
			
			System.out.println(playersIn.get(i).getName() + " has " + playersIn.get(i).getNumberOfChips() + " chips");
			
			//if the betting is already open or if player can open betting
			if(playersIn.get(i).canOpenBetting() || open){
				
				//all their remaining chips are invested in this round
				if(playersIn.get(i).getNumberOfChips()<=0){
					clean++;
				}
				
				//still have chips left so have a choice to bet/fold/see
				else if(playersIn.get(i).getNumberOfChips()!=0){
					
					//stores value that current player would need to call with
					needToCall = playersIn.get(i).amountToCall;
					
					//if their call value is more than their chips
					if(needToCall>=playersIn.get(i).numberOfChips){
						//call value will become all of their chips (all in)
						needToCall = playersIn.get(i).numberOfChips;
						System.out.println(playersIn.get(i).getName()+" see/call to go all in with = " + needToCall + "chips");
					}
					
					//if they have more chips than call value
					else{
						//if betting has been opened
						if(open){
							System.out.println(playersIn.get(i).getName()+" call/see amount = " + needToCall + " chip(s)");
						}
						if(!open){
							System.out.println("Betting hasn't been opened yet.");
						}
					}
						
					//state will become either -1,0,1
					
					if(playersIn.get(i).isHuman()){
						boolean validInput = false;
						System.out.println("You have: " + playersIn.get(i).numberOfChips + " chips");
						if (!open){
							System.out.print(">> Would you like to raise or fold: ");
						} else {
							System.out.print(">> Would you like to raise, see or fold: ");
						}
						while (!validInput){
							state = ((HumanPokerPlayer) playersIn.get(i)).getBet(scanner.nextLine(), playersIn.get(i).amountToCall, open);
							if (state==-2){
								System.out.println("Invalid Input! ");
								System.out.println("You have: " + playersIn.get(i).numberOfChips + " chips");
								if (!open){
									System.out.print(">> Would you like to raise or fold: ");
								} else {
									System.out.print(">> Would you like to raise, see or fold: ");
								}
							} else {
								validInput = true;
							}
						}
					} else {
						state = ((AutomatedPokerPlayer) playersIn.get(i)).getBet(playersIn.get(i).amountToCall, open);
					}
					//player wants to raise
					if(state==1){
						//betting becomes open
						open = true;
						//pot is increased by their call value + 1 (they raised)
						pot+=needToCall+1;
					
						//increasing all players call value except their own
						for (int j = 0; j < playersIn.size(); j++) {
							if(j!=i){
								playersIn.get(j).amountToCall++;
							}
						}
					
						//they now don't need to call anything
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has raised");
						clean = 0;
					}
					
					//player has called
					else if(state==0){
						pot+=needToCall;
						//now don't need to call anything
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has called");
						clean++;
					}
					
					//player has folded
					else if(state==-1){
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has folded");
						playersIn.remove(i);
						i--;
						clean++;
					}
				}
			}
			
			
			//if the player can't open betting and it isn't open yet
			else if(!playersIn.get(i).canOpenBetting() && !open){
				
				System.out.println(playersIn.get(i).getName()+" cant open betting!");
				//counts amount of players who can't open
				cantOpen++;
				
				//if nobody is able to open
				if(cantOpen>=playersIn.size()){
					System.out.println("Nobody wants to open, round over!");
					roundOver = true;
				}
				
			}
			
		}
	
	}

	public void showCards() {
		
		System.out.println("\nEND OF ROUND\n");
		
		for (int i = 0; i<playersIn.size(); i++) {
			System.out.println(playersIn.get(i).name + ": " + playersIn.get(i).hand.toString());
		}
	}

	public int decideWinner() {
		
		int max = 0;
		int winner=0;
		
		for (int i = 0; i < playersIn.size(); i++) {
			if(playersIn.get(i).hand.getGameValue()>max){
				max = playersIn.get(i).hand.getGameValue();
				winner = i;
			}
		}
		
		return winner;
			
	}
	
	
	public static ArrayList<Status> findReplies() {
        
		Query query = new Query("@DHK_pokerBot");
        
        ArrayList<Status> tweets = new ArrayList<Status>();
        
        try {
            QueryResult result;
            do {
                result = twit.search(query);
                
                for (Status tweet : result.getTweets()) {
                    // Replace this logic to check if it's a response to a known tweet
                    if (tweet.getInReplyToStatusId() > 0) {
                        tweets.add(tweet);
                    }
                }               
            } while ((query = result.nextQuery()) != null);

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        
        return tweets;
    }
	
	
	
}