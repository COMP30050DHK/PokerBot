package Poker;

import java.util.Scanner;

public class GameOfPoker {
	
	public static PokerPlayer[] players = new PokerPlayer[4];
	int playerNum = 0;
	public GameOfPoker(String name, int botNum, DeckOfCards d){
		PokerPlayer human = new PokerPlayer(d, name);
		players[playerNum] = human;
		playerNum++;
		PokerPlayer Tom = new AutomatedPokerPlayer(d, "Tom", 0, 0);
		if(botNum>1){
			PokerPlayer Dick = new AutomatedPokerPlayer(d, "Dick", 0, 0);
			players[playerNum] = Dick;
			playerNum++;
		}
		if(botNum>2){
			PokerPlayer Harry = new AutomatedPokerPlayer(d, "Harry", 0, 0);
			players[playerNum] = Harry;
			playerNum++;
		}
		if(botNum>3){
			PokerPlayer William = new AutomatedPokerPlayer(d, "William", 0, 0);
			players[playerNum] = William;
			playerNum++;
		}
	    	
	}
	    
	    
	public static void main(String[] args){
		String name = "";
		int botNum = 0;
		DeckOfCards d = new DeckOfCards();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the Automated Poker Machine!\nWhat is your name?");
	    name = scanner.next();
    	System.out.println("Welcome, "+name+". How many bots would you like to play against (1-4)?");
	    while(botNum<1||botNum>4){
	    	botNum = scanner.nextInt();
	    	if(botNum<1||botNum>4){
	    		System.out.println("Invalid number of bots. How many bots would you like to play against (1-4)?");
	    	}
	    }
		GameOfPoker game = new GameOfPoker(name, botNum, d);
	}
	
	
}
