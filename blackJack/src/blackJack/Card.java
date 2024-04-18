package blackJack;

enum Suit {
	Hearts, Diamonds, Clubs, Spades;
}

enum Rank {
	Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King;
}

public class Card {
	private Suit suit;
	private Rank rank;

	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
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

}
