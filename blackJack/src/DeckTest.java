import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class DeckTest {

	@Test
	void testDeck() {
		List<Card> cards = new ArrayList<Card>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				cards.add(new Card(Suit.values()[i], Rank.values()[j]));
			}
		}

		assertEquals(52, cards.size());
	}
}
