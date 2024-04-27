import java.util.ArrayList;
import java.util.List;

public class Player {
	String name;
	List<Card> hand;
	double funds;
	double currentBet;

	public Player(String name, double funds) {
		this.name = name;
		this.funds = funds;
		currentBet = 0;

		this.hand = new ArrayList<Card>();
	}

	public String getPlayerName() {
		return name;
	}

	public List<Card> getPlayerHand() {
		return hand;
	}

	public double getPlayerFunds() {
		return funds;
	}

	public double getBet() {
		return currentBet;
	}

	public void setBet(double bet) {
		currentBet = bet;
	}

	public void wonBet() {
		funds += currentBet;
		currentBet = 0;
	}

	public void lostBet() {
		funds -= currentBet;
		currentBet = 0;
	}

	public void hasBlackjack() {
		funds += currentBet * 1.5;
		currentBet = 0;
	}

	public void pushed() {
		currentBet = 0;
	}

	public void clearHand() {
		hand.clear();
	}

	public int calculateHandTotal() {
		int handTotal = 0;
		boolean cardIsAce = false;

		for (int i = 0; i < hand.size(); i++) {
			int value = hand.get(i).getCardValue();
			if (value > 10) {
				value = 10;
			} else if (value == 1) {
				cardIsAce = true;
			}
			handTotal += value;
		}
		if (cardIsAce && handTotal + 10 <= 21) {
			handTotal += 10;
		}
		return handTotal;
	}
	
	
	// Prints all cards in the players hand.
	public String toStringPlayersHand() {
		String playersHand = null;
		
		// For every card in the hand add it to the string.
		//
		// Card of Suit, Card of Suit, ... , Card of Suit
		//
		
		Card lastCard = hand.get(hand.size() -1);
		
		for(Card c : hand) {
			
			// If on the last card don't print with a comma.
			if(c.equals(lastCard) ) {
				playersHand += c.toString();
			}
			
			playersHand += c.toString() + ",";
			
		}
		
		return playersHand;
	}
}
