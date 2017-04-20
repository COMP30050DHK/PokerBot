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
	private boolean cleanRound = false;
	private boolean roundOver = false;
	private int clean = 0;
	private int needToCall = 0;
	private int cantOpen = 0;
	PokerPlayer winner;
	DeckOfCards deck;


	public HandOfPoker(DeckOfCards d, ArrayList<PokerPlayer> players) {
		deck = d;
		pokerPlayers.addAll(players);
		playersIn.addAll(players);
		
		printPlayerChips();
		
	}
	
	public void executeHandOfPoker(){

		newHandCycle();
		discardCycle();
	
		//ready to start the betting cycle
		//betting stops whenever clean round is true
		//becomes true when there has been a full rotation of calling/seeing
		cleanRound = false;
		
		while(cleanRound!=true){
			bettingRound();
			if(roundOver){
				return;
			}
		}
		
		showCards();
		returnCards();
		if(!playersIn.isEmpty()){
			winner = playersIn.get(decideWinner());
			winner.setNumberOfChips(pot);
		
			System.out.println("\n" + winner.name + " won " + pot + " chips");
		
			playersIn.clear();
		
			playersIn.addAll(pokerPlayers);
		}
		
		
	
		return;
		
		
	}
	
	private void returnCards(){
		for (int i=0;i<pokerPlayers.size();i++){
			pokerPlayers.get(i).returnCards();
		}
	}

	public void printPlayerChips() {
		
		System.out.println("\n>> CHIP LISTINGS\n");
		
		for (int i = 0; i<playersIn.size(); i++) {
			System.out.println("> " + playersIn.get(i).name + " has " + playersIn.get(i).numberOfChips + " chip(s) in the bank");
		}
	}
	
	//dealing all players a new hand
	public void newHandCycle(){
		
		System.out.println("\n>> DEALING NEW CARDS\n");
		
		for (int i = 0; i < playersIn.size(); i++) {
			//won't owe anything at start of new round
			playersIn.get(i).amountToCall=0;
			playersIn.get(i).newHand();
			if(playersIn.get(i).isHuman()){
				System.out.println(playersIn.get(i).hand.toString());
			}
		}	
	}
	
	//asking all players if they want to discard
	public void discardCycle(){
		for (int i = 0; i<playersIn.size(); i++) {
			playersIn.get(i).discard();
			if(playersIn.get(i).isHuman()){
				System.out.println(playersIn.get(i).hand.toString());
			}
		}
	}

	public void bettingRound() {
		
		clean = 0;
		
		for (int i = 0; i<playersIn.size(); i++) {
			
			System.out.println(playersIn.get(i).getName() + " has " + playersIn.get(i).getNumberOfChips() + " chips");
			
			//if the betting is already open or if player can open betting
			if(playersIn.get(i).canOpenBetting() || open){
				
				//all their remaining chips are invested in this round
				if(playersIn.get(i).getNumberOfChips()==0){
					clean++;
				}
				
				//still have chips left so have a choice
				else if(playersIn.get(i).getNumberOfChips()!=0){
					
					//stores value player would need to call with
					needToCall = playersIn.get(i).amountToCall;
					
					if(needToCall>playersIn.get(i).numberOfChips){
						needToCall = playersIn.get(i).numberOfChips;
						System.out.println(playersIn.get(i).getName()+" see/call to go all in with = " + needToCall + "chips");

					}
					else{
						if(open){
							System.out.println(playersIn.get(i).getName()+" call/see amount = " + needToCall + " chip(s)");
						}
					}
						
					state = playersIn.get(i).getBet(playersIn.get(i).amountToCall, open);
				
					//raised by 1
					if(state==1){
						//betting becomes open
						open = true;
						//pot is increased by call + 1 (they raised)
						pot+=needToCall+1;
					
						//increasing all players call value except their own
						for (int j = 0; j < playersIn.size(); j++) {
							if(j!=i){
								playersIn.get(j).amountToCall++;
							}
						}
					
						//now don't need to call anything
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has raised");
					}
					
					//called
					else if(state==0){
						pot+=needToCall;
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has called");
						clean++;
					}
					
					//folded
					else if(state==-1){
						playersIn.get(i).amountToCall = 0;
						System.out.println(playersIn.get(i).getName() + " has folded");
						playersIn.remove(i);
					}
				}
			}
			
			else if(!playersIn.get(i).canOpenBetting() && !open){
				
				System.out.println(playersIn.get(i).getName()+" cant open betting!");
				cantOpen++;
				
				if(cantOpen>=playersIn.size()){
					System.out.println("Nobody wants to open, round over!");
					roundOver = true;
				}
				
			}
			
		}
		
		if(clean == playersIn.size()){
			cleanRound = true;
		}
	}

	public void showCards() {
		
		//System.out.println("\nEND OF ROUND\n");
		
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
