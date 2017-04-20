package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class HandOfPoker {

	protected int lastBet = 0;
	int state = 0;
	protected int pot = 0;
	protected boolean open = false;
	private ArrayList<PokerPlayer> pokerPlayers = new ArrayList<PokerPlayer>();
	private ArrayList<PokerPlayer> foldedPlayers = new ArrayList<PokerPlayer>();
	private boolean cleanRound = false;
	private int clean = 0;
	private int needToCall = 0;
	PokerPlayer winner;


	public HandOfPoker(DeckOfCards d, ArrayList<PokerPlayer> players) {
		pokerPlayers = players;
		
		printPlayerChips();
		
		System.out.println("\n>> New Deal:\n");
		
		//dealing all players a new hand
		for (int i = 0; i < pokerPlayers.size(); i++) {
			//won't owe anything at start of new round
			pokerPlayers.get(i).amountToCall=0;
			pokerPlayers.get(i).newHand();
			if(pokerPlayers.get(i).isHuman()){
				System.out.println(pokerPlayers.get(i).hand.toString());
			}
		}
	
		//asking all players if they want to discard
		for (int i = 0; i < pokerPlayers.size(); i++) {
			pokerPlayers.get(i).discard();
			if(pokerPlayers.get(i).isHuman()){
				System.out.println(pokerPlayers.get(i).hand.toString());
			}
		}
	
		//ready to start the betting cycle
		cleanRound = false;
		
		while(cleanRound!=true){
			bettingRound();
		}
		
		showCards();
		
		for(int i=0; i<foldedPlayers.size(); i++){
			pokerPlayers.add(foldedPlayers.get(i));
		}
		
		winner = pokerPlayers.get(decideWinner());
		
		winner.setNumberOfChips(pot);
		
		System.out.println("\n" + winner.name + " won " + pot + " chips");
		
		return;
		
		
			
	}

	public void printPlayerChips() {
		for (int i = 0; i < pokerPlayers.size(); i++) {
			System.out.println("> " + pokerPlayers.get(i).name + " has " + pokerPlayers.get(i).numberOfChips + " chip(s) in the bank");
		}
	}

	public void bettingRound() {
		
		clean = 0;
		
		for (int i = 0; i < pokerPlayers.size(); i++) {
			
			System.out.println(pokerPlayers.get(i).getName() + " has " + pokerPlayers.get(i).getNumberOfChips() + " chips");
			
			if(pokerPlayers.get(i).canOpenBetting() || open){
				
				if(pokerPlayers.get(i).getNumberOfChips()==0){
					clean++;
				}
				
				else if(pokerPlayers.get(i).getNumberOfChips()!=0){
					
					needToCall = pokerPlayers.get(i).amountToCall; //stores value player would need to call with
					
					if(needToCall>pokerPlayers.get(i).numberOfChips){
						needToCall = pokerPlayers.get(i).numberOfChips;
						System.out.println(pokerPlayers.get(i).getName()+" see to go all in with = " + needToCall + "chips");

					}
					else{
						System.out.println(pokerPlayers.get(i).getName()+" call amount = " + needToCall);
					}
					
					open = true;	
					state = pokerPlayers.get(i).getBet(pokerPlayers.get(i).amountToCall, open);
				
					if(state==1){
					
						pot+=needToCall+1;
					
						for (int j = 0; j < pokerPlayers.size(); j++) {
							if(j!=i){
								pokerPlayers.get(j).amountToCall++;
							}
						}
					
						pokerPlayers.get(i).amountToCall = 0;
						System.out.println(pokerPlayers.get(i).getName() + " has raised");
					}
					else if(state==0){
						pot+=needToCall;
						pokerPlayers.get(i).amountToCall = 0;
						System.out.println(pokerPlayers.get(i).getName() + " has called");
						clean++;
					}
					else if(state==-1){
						pokerPlayers.get(i).amountToCall = 0;
						System.out.println(pokerPlayers.get(i).getName() + " has folded");
						foldedPlayers.add(pokerPlayers.get(i));
						pokerPlayers.remove(i);
					}
				}
			}
		}
		
		if(clean == pokerPlayers.size()){
			cleanRound = true;
		}
	}

	public void showCards() {
		
		System.out.println("\nEND OF ROUND\n");
		
		for (int i = 0; i < pokerPlayers.size(); i++) {
			System.out.println(pokerPlayers.get(i).name + ": " + pokerPlayers.get(i).hand.toString());
		}
	}

	public int decideWinner() {
		
		int max = 0;
		int winner=0;
		
		for (int i = 0; i < pokerPlayers.size(); i++) {
			if(pokerPlayers.get(i).hand.getGameValue()>max){
				max = pokerPlayers.get(i).hand.getGameValue();
				winner = i;
			}
		}
		
		return winner;
			
	}
}
