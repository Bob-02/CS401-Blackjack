import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class PlayingDeckTest {

	@Test
	void testPlayingDeck() {
		int numOfDecks = 3;

		List<Deck> playingDeck = new ArrayList<Deck>();

		for (int i = 0; i < numOfDecks; i++) {
			playingDeck.add(new Deck());
		}

		assertEquals((numOfDecks * 52),
				(playingDeck.get(0).cards.size() + playingDeck.get(1).cards.size() + playingDeck.get(2).cards.size()));
		assertEquals(numOfDecks, playingDeck.size());

		Card testCard = playingDeck.get(0).cards.get(0);
		assertEquals(testCard, playingDeck.get(0).cards.get(0));

		for (Deck deck : playingDeck) {
			Collections.shuffle(deck.cards);
		}

		assertNotEquals(testCard, playingDeck.get(0).cards.get(0));
	}

	@Test
	void testGeneratePlayingDeck() {
		int numOfDecks = 3;

		List<Deck> playingDeck = new ArrayList<Deck>();

		for (int i = 0; i < numOfDecks; i++) {
			playingDeck.add(new Deck());
		}

		assertEquals(numOfDecks, playingDeck.size());
		assertEquals((numOfDecks * 52),
				(playingDeck.get(0).cards.size() + playingDeck.get(1).cards.size() + playingDeck.get(2).cards.size()));

	}

	@Test
	void testShuffleDeck() {
		List<Deck> playingDeck = new ArrayList<Deck>();
		Card testCard = playingDeck.get(0).cards.get(0);
		assertEquals(testCard, playingDeck.get(0).cards.get(0));

		for (Deck deck : playingDeck) {
			Collections.shuffle(deck.cards);
		}

		assertNotEquals(testCard, playingDeck.get(0).cards.get(0));
	}
}
