package poker;

import java.util.Random;
import java.util.Scanner;

public class AutomatedPokerPlayer extends PokerPlayer {
	
	protected float riskAversion; //1.0 means will only bet with good cards, 0.0 means is likely to bet even with bad cards
	protected float bluffLevel; //1.0 means will bluff and possibly raise, even with bad cards
	protected Random rand;

	public AutomatedPokerPlayer(DeckOfCards d, String playerName, float risk_Aversion, float bluff_Level) {
		super(d, playerName);
		riskAversion = risk_Aversion;
		bluffLevel = bluff_Level;
		Random rand = new Random();
	}
	
	public int discard(){
		int numOfCardsDiscarded=0;
		Random rand = new Random();
		int discardProbabilities[] = new int[HandOfCards.HAND_SIZE];
		//get discard probabilities
		for (int j=0;j<HandOfCards.HAND_SIZE;j++){
			discardProbabilities[j] = hand.getDiscardProbability(j);
		}
		//compare discard probabilities to random numbers and discard cards
		for (int i=0;i<HandOfCards.HAND_SIZE;i++){
			int randomNumber = rand.nextInt(100);
			if (randomNumber<discardProbabilities[i]){
				hand.discardAndReplace(i, deck.deal());
				numOfCardsDiscarded++;
			}
		}
		//returns the number of cards discarded
		return numOfCardsDiscarded;
	}
	
	
	public int getBet(int currentHighBet){
		//bet will store -1 for fold, 0 for call, 1 for raise
		int bet = 0;
		
		if(hand.getGameValue()<1000000){
			
			float possibleBluff = new Random().nextFloat();
			System.out.println(possibleBluff);
			
			
			if(possibleBluff<bluffLevel-0.095){
				return 1;
			}
		}
		
		
		
		
		return bet;
	}
	
	public static void main(String[] args){
		DeckOfCards deck = new DeckOfCards();
		AutomatedPokerPlayer player = new AutomatedPokerPlayer(deck, "Robo", 1, (float) 0.5);
		System.out.println(player.toString());
		player.discard();
		System.out.println(player.toString());
		System.out.println("getBet result: " + player.getBet(3));
	}

}
