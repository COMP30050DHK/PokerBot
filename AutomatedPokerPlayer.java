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
		int percentageOfChips = 0;
		
		if(currentHighBet<numberOfChips){
			if(currentHighBet>0){
				percentageOfChips = (int) (((float)currentHighBet/numberOfChips)*100.0);	
			}
		}
		else if(currentHighBet>=numberOfChips){
			percentageOfChips = 100;
		}
		
		
		if(hand.getGameValue()<1500000 && percentageOfChips<20){
			
			float possibleBluff = new Random().nextFloat();
			
			if(possibleBluff<bluffLevel-0.2){
				return 1;
			}
		}
		
		//decision to bet or raise is a function of the quality of the hand + the risk-aversion of the agent
		
		double confidence = hand.getGameValue()/1000000.0 / (riskAversion);

			if (isBetween(percentageOfChips, 0, 15)) {
			  confidence = confidence * 1.2;
			}
			else if (isBetween(percentageOfChips, 15, 40)) {
				confidence = confidence * 1.0;
			}
			else if (isBetween(percentageOfChips, 40, 95)) {
				confidence = confidence * 0.9;
			}
			//wouldn't be raising here
			else if (percentageOfChips > 95) {
				confidence = confidence * 0.85;
				if(confidence>1.0){
					return 0;
				}
				else{
					return -1;
				}
			}
			
			if(confidence>2.5){
				bet = 1;
			}
			else if(confidence>1.0){
				bet = 0;
			}
			else if(confidence<1.0){
				bet = -1;
			}
		
		return bet;
		
	}
	
	private static boolean isBetween(int x, int lower, int upper) {
		  return lower <= x && x <= upper;
	}
	
	public static void main(String[] args){
		DeckOfCards deck = new DeckOfCards();
		AutomatedPokerPlayer player = new AutomatedPokerPlayer(deck, "Robo", 0.3, 0.3);
		player.discard();
		System.out.println(player.toString() + "\n");
		System.out.print("HIGH RISK TAKER:\t");
		System.out.println("getBet result: " + player.getBet(0));
		player.changeRisk(0.6);
		System.out.print("MEDIUM RISK TAKER:\t");
		System.out.println("getBet result: " + player.getBet(0));
		player.changeRisk(0.85);
		System.out.print("LOW RISK TAKER:\t\t");
		System.out.println("getBet result: " + player.getBet(0));
	}

}
