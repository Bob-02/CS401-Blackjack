
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CardTest {

	@Test
	void testCard() {
		Card testCard = new Card(Suit.Hearts, Rank.Queen);

		assertNotNull(testCard.getSuit());
		assertNotNull(testCard.getRank());
	}

	@Test
	void testGetSuit() {
		Card testCard = new Card(Suit.Hearts, Rank.Queen);

		assertNotNull(testCard.getSuit());
	}

	@Test
	void testGetRank() {
		Card testCard = new Card(Suit.Hearts, Rank.Queen);

		assertNotNull(testCard.getRank());
	}

	@Test
	void testToString() {
		Card testCard = new Card(Suit.Hearts, Rank.Queen);

		assertEquals((testCard.getRank().toString() + " of " + testCard.getSuit().toString()), testCard.toString());
	}

}
