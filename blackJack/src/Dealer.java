import java.util.ArrayList;
import java.util.List;

public class Dealer {
	String name;
	List<Card> hand;
	double casinoFunds;
	double casinoBet;
	Table table;
	boolean hasBlackJack;

	public Dealer(String name, double funds) {
		this.name = name;
		this.casinoFunds = funds;
		casinoBet = 0;

		this.hand = new ArrayList<Card>();
	}

	public String getDealerName() {
		return name;
	}

	public List<Card> getDealerHand() {
		return hand;
	}

	public double getCasinoFunds() {
		return casinoFunds;
	}

	public double getBet() {
		return casinoBet;
	}

	public void setBet(double bet) {
		casinoBet = bet;
	}

	public void wonBet() {
		casinoFunds += casinoBet;
		casinoBet = 0;
	}

	public void lostBet() {
		casinoFunds -= casinoBet;
		casinoBet = 0;
	}

	public Table getTable() {
		return table;
	}

	public boolean doesTheDealerHaveBlackJack() {
		return hasBlackJack;
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

	public boolean peek() {
		int handValue = calculateHandTotal();
		return handValue == 1 || handValue >= 10;
	}

	public void clearHand() {
		hand.clear();
	}
	
	
	// Prints all cards in the players hand.
	public String toStringDealersHand() {
		String playersHand = null;
		
		// For every card in the hand add it to the string.
		//
		// Card of Suit, Card of Suit, ... , Card of Suit
		//
		Card lastCard;
		
		if(hand.size() < 1) {
			return "";
		}
		
		// if the size of the hand is 2 or more
		// fixes out of bounds.
		if(hand.size() > 1) {
			lastCard = hand.get(hand.size() -1);
		}
		
		// if only one card
		else {
			lastCard = hand.get(0);
		}

		
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
