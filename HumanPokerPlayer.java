package poker;

import java.util.Scanner;

public class HumanPokerPlayer extends PokerPlayer{
	
	
	/* The scanners are left open because closing them causes System.in to close
	 *  and this cannot be reopened without causing an exception
	 */
	
	public HumanPokerPlayer(DeckOfCards d, String playerName) {
		super(d, playerName);
	}
	
	public boolean isHuman(){
		return true;
	}

	/* Asks the user which cards they would like to discard. If the user enters
	 *  more than 3 cards only the first 3 will be discarded.
	 * 
	 */
	public int discard(){
		System.out.print(">> Which card(s) would you like to discard (e.g., 1,3): ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		char nextChar;
		int cardsDiscarded=0;
		for (int i=0;i<input.length();i++){
			nextChar = input.charAt(i);
			if (nextChar>='0' && nextChar<='4'){
				int cardPosition = nextChar - '0';
				PlayingCard card = deck.deal();
				hand.discardAndReplace(cardPosition, card);
				cardsDiscarded++;
			}
			if (cardsDiscarded==3){
				break;
			}
		}
		return cardsDiscarded;
	}
	
	
	/* Returns 1 if player raises, 0 if player sees, -1 if player folds.
	 *  If currentHighBet is 0, the betting has not been opened yet, and
	 *  players can only raise or fold.
	 */
	public int getBet(int currentHighBet, boolean open){
		int bet = 0;
		boolean validInput = false;
		
		System.out.println(this.hand.toString());
		
		while (!validInput){
			
			System.out.println("You have: " + numberOfChips + " chips");
			
			if (!open){
				System.out.print(">> Would you like to raise or fold: ");
			} else {
				System.out.print(">> Would you like to raise, see or fold: ");
			}
			
			Scanner scanner = new Scanner(System.in);
			String input = scanner.next();

			
			if (input.equalsIgnoreCase("raise")){
				bet = 1;
				setNumberOfChips(-currentHighBet-1);
				amountToCall = 0;
				validInput = true;
			}
			else if (input.equalsIgnoreCase("see") && open){
				bet = 0;
				setNumberOfChips(-currentHighBet);
				amountToCall = 0;
				validInput = true;
			}
			else if (input.equalsIgnoreCase("fold")){
				bet = -1;
				validInput = true;
			}
			if (!validInput){
				System.out.println(">> Invalid input!");
			}
		}

		return bet;
	}
	
	public static void main(String[] args){
		
		DeckOfCards deck = new DeckOfCards();
		HumanPokerPlayer player = new HumanPokerPlayer(deck, "Tom");
		System.out.println(player.toString());
		player.discard();
		System.out.println(player.toString());
		System.out.println("getBet result: " + player.getBet(3,true));
	}
}
