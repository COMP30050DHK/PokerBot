package poker;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;

public class GameOfPoker {
	
	public static ArrayList<PokerPlayer> players = new ArrayList<PokerPlayer>();
	public static String nameOfUser;
	public static String name = "";
	public static int botNum = 0;
	public static DeckOfCards d;
	public static long id = 0;
	private static Scanner scanner;
	private String[] botNames = {"Tom", "Dick", "Harry", "William"};
	private static Twitter twit;
	private static Configuration config;
	
	//Constructor sets up the game
	public GameOfPoker(String name, int botNum, String userName, long statusID, Twitter twitter, Configuration configuration){
		nameOfUser = userName;
		id = statusID;
		twit = twitter;
		config = configuration;
		DeckOfCards d = new DeckOfCards();
		//scanner = new Scanner(System.in);
		//System.out.println("Welcome to the Automated Poker Machine!\nWhat is your name?");
	    //name = scanner.next();
    	//System.out.println("Welcome, "+name+". How many bots would you like to play against (1-4)?");
	    
    	while(botNum<1||botNum>4){
	    	botNum = scanner.nextInt();
	    //	if(botNum<1||botNum>4){
	    		System.out.println("Invalid number of bots. How many bots would you like to play against (Integer 1-4)?");
	    //	}
	    }
    	
    	BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/poker/names.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String line = "";
		try {
			line = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	List<String> names = new ArrayList<String>();
    	while (line != null) {
    	     names.add(line);
    	     try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			pokerHand.executeHandOfPoker();
			
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
				System.out.println("Congratulations, you have eliminated all the automated players!");
			case -1:
				System.out.println("Bad Luck, you have run out of chips and are removed from the game");
			default:
				System.out.println("Thanks for playing!");
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
		
		System.out.print(">> Would you like to play another round? ('n' or 'y'): ");
		String input = scanner.next();
		boolean validInput = false;
		while(!validInput){
			
			if (input.equalsIgnoreCase("n")){
				validInput=true;
				return -2;
			}
			else if (input.equalsIgnoreCase("y")){
				validInput=true;
				return 1;
			}
			else{
				System.out.print(">> INVALID INPUT ('y' or 'n'): ");
				input = scanner.next();
			}
		}
		return -1;
	}
	    
	//checks if any players have 0 chips left and eliminates them
	private static void removePlayerCheck(int i){
		if(players.get(i).isBust()){
			System.out.println("Player "+players.get(i).name+" has no chips remaining and has been eliminated from the game.");
			players.remove(i);
			botNum--;
		}
	}
	
	//rotates which player starts the betting
	private static void rotateOpeningPlayer(){
		PokerPlayer rotate = players.remove(0);
		players.add(rotate);
	}
	
	    
//	public static void main(String[] args){
		
		//GameOfPoker game = new GameOfPoker();
	//	game.playGame();
	
//	}
	
}