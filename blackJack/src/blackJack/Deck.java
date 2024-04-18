package blackJack;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Deck {
	List<Card> cards = new ArrayList<Card>();

	public Deck() {
		generateDeck();
	}

	@Test
	public List<Card> getCards() {
		assertTrue(cards != null);
		return cards;
	}

	@Test
	public void generateDeck() {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				cards.add(new Card(Suit.values()[i], Rank.values()[j]));
			}
		}

		assertTrue(cards.size() == 52);
	}

	@Test
	public String toString() {
		String result = "";
		for (Card card : cards) {
			result += card.toString() + "\n";
		}

		assertTrue(!result.equals(""));

		return result;
	}

}
