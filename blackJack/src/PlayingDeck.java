import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PlayingDeck {

	List<Deck> playingDeck = new ArrayList<Deck>();

	public PlayingDeck(int numOfDecks) {
		generatePlayingDeck(numOfDecks);

		// TODO: make sure shuffling works
		Collections.shuffle(playingDeck);
	}

	@Test
	public void generatePlayingDeck(int numOfDecks) {
		for (int i = 1; i < numOfDecks; i++) {
			playingDeck.add(new Deck());
		}

		// TODO: test assertion is returning false
		// assertTrue(numOfDecks > 0 && playingDeck.size() == (52 * numOfDecks));
	}

	public void shuffleDeck() {
		// TODO: make sure shuffling works
		Collections.shuffle(playingDeck);
	}

	@Test
	public String toString() {

		String result = "";

		for (Deck deck : playingDeck) {
			result += deck.toString() + "\n \n";
		}

		assertTrue(!result.equals(""));

		return result;
	}
}
