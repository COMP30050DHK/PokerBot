package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class GameOfPoker {
	
	public static ArrayList<PokerPlayer> players = new ArrayList<PokerPlayer>();
	public static String name = "";
	public static int botNum = 0;
	public static int playerNum = 0;
	public static DeckOfCards d;
	public static int openingPlayer = 0;
	
	public GameOfPoker(){
		DeckOfCards d = new DeckOfCards();
		Scanner scanner = new Scanner(System.in);
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
		
		PokerPlayer Tom = new AutomatedPokerPlayer(d, "Tom", Math.random(), Math.random());
		players.add(Tom);
		
		if(botNum>1){
			PokerPlayer Dick = new AutomatedPokerPlayer(d, "Dick", Math.random(), Math.random());
			players.add(Dick);
		}
		if(botNum>2){
			PokerPlayer Harry = new AutomatedPokerPlayer(d, "Harry", Math.random(), Math.random());
			players.add(Harry);
		}
		if(botNum>3){
			PokerPlayer William = new AutomatedPokerPlayer(d, "William", Math.random(), Math.random());
			players.add(William);
		}
	    	
	}
	    
	public static void removePlayerCheck(int i){
		if(players.get(i).numberOfChips<=0){
			System.out.println("Player "+players.get(i).name+" has no chips remaining and has been eliminated from the game.");
			players.remove(i);
			botNum--;
		}
	}
	
	public static void rotateOpeningPlayer(){
		PokerPlayer rotate = players.remove(0);
		players.add(rotate);
	}
	
	    
	public static void main(String[] args){
		GameOfPoker game = new GameOfPoker();
		
		while(players.size()>1){
			
			
			
			
			for(int i=0; i<players.size(); i++){
				removePlayerCheck(i);
			}
			

			
			

			HandOfPoker pokerHand = new HandOfPoker(d, players);
			
			rotateOpeningPlayer();
			
		}
		
		
		System.out.println(players.size());
		
		
		System.out.println("The winner was " + players.get(0).getName());
	
	}
	
	
}