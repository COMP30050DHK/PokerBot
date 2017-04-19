/* Darragh O'Keeffe
 * 14702321
 * COMP 30050 Assignment 6
 * 10/03/2017
 */

package poker;

import java.util.Random;

/* Player class with ability to discard cards from a hand dealt from a deck
 *  which is passed as an argument to the constructor
 */
public class PokerPlayer {
	protected HandOfCards hand;
	protected DeckOfCards deck;
	protected String name;
	protected int numberOfChips;
	public int amountToCall=0;
	protected static final int STARTING_NUMBER_OF_CHIPS = 10;
	
	//public constructor takes a deck of cards and creates a new hand of cards
	public PokerPlayer(DeckOfCards d, String playerName){
		name = playerName;
		numberOfChips = STARTING_NUMBER_OF_CHIPS;
		hand = new HandOfCards(d);
		deck = d;
	}
	
	public void newHand(){
		deck.reset();
		hand = new HandOfCards(deck);
	}
	
	public boolean isHuman(){
		return false;
	}
	
	//gets the discard probabilities of the cards in the hand, generates 5 random
	// numbers from zero to 99 and discards cards whose discard probabilities are
	// lower than the corresponding discard probabilities
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
	
	public String toString(){
		//returns details of hand held by the player
		return (name + ": " + hand.toString() + " " + hand.getType() + ", Value: " + hand.getGameValue());
	}
	
	//returns the value of the hand held by the player 
	public int getHandValue(){
		return hand.getGameValue();
	}
	
	//returns the players name
	public String getName(){
		return name;
	}
	
	//returns number of chips held by the player
	public int getNumberOfChips(){
		return numberOfChips;
	}
	
	//use negative argument for player losing chips
	public void setNumberOfChips(int chips){
		numberOfChips+=chips;
	}
	
	//a player can open the betting if they have at least a pair
	public boolean canOpenBetting(){
		return hand.getGameValue()>HandOfCards.ONE_PAIR_DEFAULT_VALUE;
	}
	
	//A player is bust and will be removed from the game when they have no chips remaining
	public boolean isBust(){
		return (numberOfChips==0);
	}
	
	//tests sampleSize number of players and calls the discard function on all of them
	// and prints the results
	public static void main(String[] args){
				
		float sampleSize = 1000;
		float numHandsImproved = 0;
		int errors = 0;
		for (int i=0;i<sampleSize;i++){
			DeckOfCards deck = new DeckOfCards();
			PokerPlayer player = new PokerPlayer(deck, "INSERT_NAME");
			System.out.println("Starting hand: "+player.toString());
			int initialValue = player.getHandValue();
			int cardsDiscarded = player.discard();
			System.out.println("Discard "+cardsDiscarded+" card(s)");
			if (cardsDiscarded>3){
				System.out.println("ERROR: TOO MANY CARDS DISCARDED");
				errors++;
			}
			if (cardsDiscarded<0){
				System.out.println("ERROR: CANNOT DISCARD NEGATIVE NUMBER OF CARDS");
				errors++;
			}
			int finalValue = player.getHandValue();
			System.out.println("New hand: "+player+"\n");
			if (finalValue>initialValue){
				numHandsImproved++;
			}
		}
		System.out.println("Number of Hands tested: "+sampleSize);
		System.out.println("Number of Hands improved: "+numHandsImproved);
		System.out.println("Percentage of Hands improved: "+(100*numHandsImproved/sampleSize));
		System.out.println("Number of hands with errors: "+errors);
	}
	

	public int getBet(int currentHighBet, boolean open){
		//not needed here but needed in classes that inherit from this
		return 0;	
	}
}
