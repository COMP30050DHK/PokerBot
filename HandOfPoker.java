package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class HandOfPoker {

	protected int lastBet = 0;
	int state = 0;
	protected int pot = 0;
	protected boolean open = false;
	private ArrayList<PokerPlayer> pokerPlayers = new ArrayList<PokerPlayer>();
	private ArrayList<PokerPlayer> playersIn = new ArrayList<PokerPlayer>();
	private ArrayList<PokerPlayer> foldedPlayers = new ArrayList<PokerPlayer>();
	private boolean cleanRound = false;
	private int clean = 0;
	private int needToCall = 0;
	PokerPlayer winner;


	public HandOfPoker(DeckOfCards d, ArrayList<PokerPlayer> players) {
		pokerPlayers = players;
		playersIn = pokerPlayers;
		
		printPlayerChips();
		
		System.out.println("\n>> New Deal:\n");
		
		//dealing all players a new hand
		for (int i = 0; i < playersIn.size(); i++) {
			//won't owe anything at start of new round
			playersIn.get(i).amountToCall=0;
			playersIn.get(i).newHand();
			if(playersIn.get(i).isHuman()){
				System.out.println(playersIn.get(i).hand.toString());
			}
		}
	
		//asking all players if they want to discard
		for (int i = 0; i<playersIn.size(); i++) {
			playersIn.get(i).discard();
			if(playersIn.get(i).isHuman()){
				System.out.println(playersIn.get(i).hand.toString());
			}
		}
	
		//ready to start the betting cycle
		cleanRound = false;
		
		while(cleanRound!=true){
			bettingRound();
		}
		
		showCards();
		

		
		winner = playersIn.get(decideWinner());
		
		winner.setNumberOfChips(pot);
		
		System.out.println("\n" + winner.name + " won " + pot + " chips");
		
		return;
		
		
			
	}

	public void printPlayerChips() {
		for (int i = 0; i<playersIn.size(); i++) {
			System.out.println("> " + playersIn.get(i).name + " has " + playersIn.get(i).numberOfChips + " chip(s) in the bank");
		}
	}

	public void bettingRound() {
		
		clean = 0;
		
		for (int i = 0; i<playersIn.size(); i++) {
			
			System.out.println(playersIn.get(i).getName() + " has " + playersIn.get(i).getNumberOfChips() + " chips");
			
			if(playersIn.get(i).canOpenBetting() || open){
				
				if(playersIn.get(i).getNumberOfChips()==0){
					clean++;
				}
				
				else if(playersIn.get(i).getNumberOfChips()!=0){
					
					needToCall = playersIn.get(i).amountToCall; //stores value player would need to call with
					
					if(needToCall>playersIn.get(i).numberOfChips){
						needToCall = playersIn.get(i).numberOfChips;
						System.out.println(playersIn.get(i).getName()+" see to go all in with = " + needToCall + "chips");

					}
					else{
						System.out.println(playersIn.get(i).getName()+" call amount = " + needToCall);
					}
					
					open = true;	
					state = playersIn.get(i).getBet(playersIn.get(i).amountToCall, open);
				
					if(state==1){
					
						pot+=needToCall+1;
					
						for (int j = 0; j < playersIn.size(); j++) {
							if(j!=i){
								playersIn.get(j).amountToCall++;
							}
						}
					
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has raised");
					}
					else if(state==0){
						pot+=needToCall;
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has called");
						clean++;
					}
					else if(state==-1){
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has folded");
						foldedPlayers.add(playersIn.get(i));
						playersIn.remove(i);
					}
				}
			}
		}
		
		if(clean == playersIn.size()){
			cleanRound = true;
		}
	}

	public void showCards() {
		
		System.out.println("\nEND OF ROUND\n");
		
		for (int i = 0; i<playersIn.size(); i++) {
			System.out.println(playersIn.get(i).name + ": " + playersIn.get(i).hand.toString());
		}
	}

	public int decideWinner() {
		
		int max = 0;
		int winner=0;
		
		for (int i = 0; i < playersIn.size(); i++) {
			if(playersIn.get(i).hand.getGameValue()>max){
				max = playersIn.get(i).hand.getGameValue();
				winner = i;
			}
		}
		
		return winner;
			
	}
}
