package poker;

import java.util.Random;
import java.util.Scanner;

public class AutomatedPokerPlayer extends PokerPlayer {
	
	protected double riskAversion; //1.0 means will only bet with good cards, 0.0 means is likely to bet even with bad cards
	protected double bluffLevel; //1.0 means will bluff and possibly raise, even with bad cards
	protected Random rand;

	public AutomatedPokerPlayer(DeckOfCards d, String playerName, double risk_Aversion, double bluff_Level) {
		super(d, playerName);
		riskAversion = risk_Aversion;
		bluffLevel = bluff_Level;
		Random rand = new Random();
	}
	
	public void changeRisk(double newRisk){
		riskAversion = newRisk;
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
			
			if(possibleBluff<bluffLevel-0.095){
				return 1;
			}
			
			return 0;
		}
		
		//decision to bet or raise is a function of the quality of the hand + the risk-aversion of the agent
		
		double confidence = hand.getGameValue()/1000000.0 * (1.5 - riskAversion);
		
		if(confidence>1.0){
			bet = 0;
		}
		if(confidence>1.5){
			bet = 1;
		}
		
		System.out.println("Confidence in hand: " + confidence);
		
		return bet;
	}
	
	public static void main(String[] args){
		DeckOfCards deck = new DeckOfCards();
		AutomatedPokerPlayer player = new AutomatedPokerPlayer(deck, "Robo", 0.4, 0.0);
		player.discard();
		System.out.println(player.toString());
		System.out.println("HIGH RISK TAKER");
		System.out.println("getBet result: " + player.getBet(1));
		player.changeRisk(0.6);
		System.out.println("MEDIUM RISK TAKER");
		System.out.println("getBet result: " + player.getBet(1));
		player.changeRisk(0.8);
		System.out.println("LOW RISK TAKER");
		System.out.println("getBet result: " + player.getBet(1));
	}

}
