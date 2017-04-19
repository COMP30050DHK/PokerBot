package poker;

import java.util.Scanner;

public class GameOfPoker {
	
	public static PokerPlayer[] players = new PokerPlayer[5];
	public static String name = "";
	public static int botNum = 0;
	public static int playerNum = 0;
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
		players[playerNum] = human;
		playerNum++;
		
		PokerPlayer Tom = new AutomatedPokerPlayer(d, "Tom", Math.random(), Math.random());
		players[playerNum] = Tom;
		playerNum++;
		
		if(botNum>1){
			PokerPlayer Dick = new AutomatedPokerPlayer(d, "Dick", Math.random(), Math.random());
			players[playerNum] = Dick;
			playerNum++;
		}
		if(botNum>2){
			PokerPlayer Harry = new AutomatedPokerPlayer(d, "Harry", Math.random(), Math.random());
			players[playerNum] = Harry;
			playerNum++;
		}
		if(botNum>3){
			PokerPlayer William = new AutomatedPokerPlayer(d, "William", Math.random(), Math.random());
			players[playerNum] = William;
			playerNum++;
		}
		
		
		//Here, HandOfPokers will be created
		
		while(/*AMOUNT OF PLAYERS LEFT > 1*/){
			
			//NEW HAND OF POKER
			
		}
		
		
		
		
		
		
		
	    	
	}
	    
	    
	public static void main(String[] args){
		GameOfPoker game = new GameOfPoker();
		for(int i=0; i<=botNum; i++){
			System.out.println("Player "+i+" is: "+players[i].name);
		}
	}
	
	
}
