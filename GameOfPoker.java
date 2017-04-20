package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class GameOfPoker {
	
	public static ArrayList<PokerPlayer> players = new ArrayList<PokerPlayer>();
	public static String name = "";
	public static int botNum = 0;
	public static DeckOfCards d;
	public static int openingPlayer = 0;
	private static Scanner scanner;
	private String[] botNames = {"Tom", "Dick", "Harry", "William"};
	
	//Constructor sets up the game
	public GameOfPoker(){
		
		DeckOfCards d = new DeckOfCards();
		scanner = new Scanner(System.in);
		System.out.println("Welcome to the Automated Poker Machine!\nWhat is your name?");
	    name = scanner.next();
    	System.out.println("Welcome, "+name+". How many bots would you like to play against (1-4)?");
	    
    	while(botNum<1||botNum>4){
	    	botNum = scanner.nextInt();
	    	if(botNum<1||botNum>4){
	    		System.out.println("Invalid number of bots. How many bots would you like to play against (Integer 1-4)?");
	    	}
	    }
	    	    
	    //Creating players here	   
		PokerPlayer human = new HumanPokerPlayer(d, name);
		players.add(human);
		
		for (int i=0;i<botNum;i++){
			PokerPlayer bot = new AutomatedPokerPlayer(d, botNames[i], Math.random(), Math.random());
			players.add(bot);
		}

	}
	
	//plays through a game of poker
	public static void playGame(){
		boolean playOn = true;
		int result = 0;
		while(playOn){

			HandOfPoker pokerHand = new HandOfPoker(d, players);
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
		if (players.size()==1 && players.get(0).isHuman()){
			return 0;
		}
		for (int i=0;i<players.size();i++){
			if (players.get(i).isHuman() && players.get(i).isBust()){
				return -1;
			}
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
	public static void removePlayerCheck(int i){
		if(players.get(i).isBust()){
			System.out.println("Player "+players.get(i).name+" has no chips remaining and has been eliminated from the game.");
			players.remove(i);
			botNum--;
		}
	}
	
	//rotates which player starts the betting
	public static void rotateOpeningPlayer(){
		PokerPlayer rotate = players.remove(0);
		players.add(rotate);
	}
	
	    
	public static void main(String[] args){
		
		GameOfPoker game = new GameOfPoker();
		game.playGame();
	
	}
	
}