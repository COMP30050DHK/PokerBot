package poker;

import java.util.Scanner;

public class AutomatedPokerPlayer extends PokerPlayer {
	
	protected int riskAversion;
	protected int bluffLevel;

	public AutomatedPokerPlayer(DeckOfCards d, String playerName, int risk_Aversion, int bluff_Level) {
		super(d, playerName);
		riskAversion = risk_Aversion;
		bluffLevel = bluff_Level;
	}
	
	
	public int getBet(int currentHighBet){
		//bet will store -1 for fold, 0 for call, 1 for raise
		int bet = 0;
		return bet;
	}
	
	
	
	
	
	
	public static void main(String[] args){
		DeckOfCards deck = new DeckOfCards();
		AutomatedPokerPlayer player = new AutomatedPokerPlayer(deck, "Robo", 10, 0);
		System.out.println(player.toString());
		player.discard();
		System.out.println(player.toString());
		System.out.println("getBet result: " + player.getBet(3));
	}

}
