package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class HandOfPoker {

	protected int lastBet = 0;
	int state = 0;
	protected int pot = 0;
	protected boolean open = false;
	private ArrayList<PokerPlayer> pokerPlayers = new ArrayList<PokerPlayer>();


	public HandOfPoker(DeckOfCards d, ArrayList<PokerPlayer> players) {
		pokerPlayers = players;
		System.out.println(">> New Deal:");
		
		for (int i = 0; i < pokerPlayers.size(); i++) {
			pokerPlayers.get(i).newHand();
			if(pokerPlayers.get(i).isHuman()){
				System.out.println(pokerPlayers.get(i).hand.toString());
			}
		}
	
		for (int i = 0; i < pokerPlayers.size(); i++) {
			pokerPlayers.get(i).discard();
			if(pokerPlayers.get(i).isHuman()){
				System.out.println(pokerPlayers.get(i).hand.toString());
			}
		}
	
		bettingRound();
			
	}

	public void printPlayerChips() {
		for (int i = 0; i < pokerPlayers.size(); i++) {
			System.out.println("> " + pokerPlayers.get(i).name + " has " + pokerPlayers.get(i).numberOfChips + " chip(s) in the bank");
		}
	}

	public void bettingRound() {
		
		for (int i = 0; i < pokerPlayers.size(); i++) {
			if(pokerPlayers.get(i).canOpenBetting() || open){
				
				open = true;
				
				state = pokerPlayers.get(i).getBet(lastBet);
				
				if(state==1){
					lastBet = 1;
					System.out.println(pokerPlayers.get(i).getName() + " has raised");
				}
				else if(state==0){
					lastBet = 1;
					System.out.println(pokerPlayers.get(i).getName() + " has called");
				}
				else if(state==-1){
					System.out.println(pokerPlayers.get(i).getName() + " has folded");
					pokerPlayers.remove(i);
				}
			}
		}

	}

	public int discard() {

		// Human section

		HumanPokerPlayer human = (HumanPokerPlayer)pokerPlayers.get(0);

		System.out.print(">> Which card(s) would you like to discard (e.g., 1,3): ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		char nextChar;
		int cardsDiscarded=0;
		for (int i=0;i<input.length();i++){
			nextChar = input.charAt(i);
			if (nextChar>='0' && nextChar<='4'){
				int cardPosition = nextChar - '0';
				PlayingCard card = human.deck.deal();
				human.hand.discardAndReplace(cardPosition, card);
				cardsDiscarded++;
			}
		}
		return cardsDiscarded;
	}

	public void humanFold() {

		System.out.print(">> Would you like to fold? (y/n)");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();

		do {
			if (input.equals("Y") || input.equals("y")) {
				System.out.println("You have folded!\n");
				pokerPlayers.remove(0);
			}
			else if (input.equals("N") || input.equals("n")) {
				bettingRound();
			}
			else {
				System.out.println("Error in input, try again");
			}
		} while (true);
	}

	public void botFold(int lastBet) {

		for (int i = 1; i < pokerPlayers.size(); i++) {
			AutomatedPokerPlayer bot = (AutomatedPokerPlayer)pokerPlayers.get(i);
			// If bot.fold() returns true remove from hand
			if (bot.numberOfChips <= lastBet) {
				System.out.println("> " + bot.name + " has folded!");
				pokerPlayers.remove(i);
			}
		}
	}

	public void showCards() {

	}

	public void decideWinner() {

	}
}
