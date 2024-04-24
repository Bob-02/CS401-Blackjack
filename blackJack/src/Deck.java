import java.util.ArrayList;
import java.util.List;

public class Deck {
	List<Card> cards = new ArrayList<Card>();

	public Deck() {
		generateDeck();
	}

	public List<Card> getCards() {
		return cards;
	}

	public void generateDeck() {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				cards.add(new Card(Suit.values()[i], Rank.values()[j]));
			}
		}
	}

	public String toString() {
		String result = "";
		for (Card card : cards) {
			result += card.toString() + "\n";
		}

		return result;
	}

}
