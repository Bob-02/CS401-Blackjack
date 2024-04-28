public class Card {
	private Suit suit;
	private Rank rank;
	private int cardValue;

	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;

		switch (this.rank) {
		case Two: {
			cardValue = 2;
			break;
		}
		case Three: {
			cardValue = 3;
			break;
		}
		case Four: {
			cardValue = 4;
			break;
		}
		case Five: {
			cardValue = 5;
			break;
		}
		case Six: {
			cardValue = 6;
			break;
		}
		case Seven: {
			cardValue = 7;
			break;
		}
		case Eight: {
			cardValue = 8;
			break;
		}
		case Nine: {
			cardValue = 9;
			break;
		}
		case Ten: {
			cardValue = 10;
			break;
		}
		case Ace: {
			cardValue = 1;
			break;
		}
		// King, Queen, and Jack should get the default case
		default:
			cardValue = 10;
			break;
		}
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	public String toString() {
		return rank + " of " + suit;
	}

	public int getCardValue() {
		return cardValue;
	}
}
