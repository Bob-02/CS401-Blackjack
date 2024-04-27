import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayingDeck {

	List<Deck> playingDeck = new ArrayList<Deck>();
	private int nextCardIndex;

	public PlayingDeck(int numOfDecks) {
		generatePlayingDeck(numOfDecks);

		for (Deck deck : playingDeck) {
			Collections.shuffle(deck.cards);
		}
	}

	public void generatePlayingDeck(int numOfDecks) {
		for (int i = 0; i < numOfDecks; i++) {
			playingDeck.add(new Deck());
		}
	}

	public void shuffleDeck() {

		for (Deck deck : playingDeck) {
			Collections.shuffle(deck.cards);
		}
	}

	public String toString() {

		String result = "";

		for (Deck deck : playingDeck) {
			result += deck.toString() + "\n \n";
		}
		return result;
	}

	public Card dealACard() {

		if (nextCardIndex < 0 || nextCardIndex > 51) {
			System.out.println("Future exception goes here");
		}
		return playingDeck.get(0).cards.get(++nextCardIndex);
	}
}
