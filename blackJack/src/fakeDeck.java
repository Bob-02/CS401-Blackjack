import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class fakeDeck {
    private List<fakeCard> cards = new ArrayList<>();
    private int nextCardIndex = 0;

    public fakeDeck() {
        generateDeck();
    }

    // Generate a standard deck of 52 cards
    private void generateDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new fakeCard(suit, rank));
            }
        }
    }

    // Shuffle the deck of cards
    public void shuffle() {
        Collections.shuffle(cards);
        nextCardIndex = 0;  // Reset the index after shuffling
    }

    // Deal one card from the deck
    public fakeCard dealCard() {
        if (nextCardIndex < cards.size()) {
            return cards.get(nextCardIndex++);
        }
        return null; // or throw an exception if no cards are left
    }

    // Optionally, you might want to be able to reinitialize the deck
    public void reset() {
        cards.clear();
        generateDeck();
        shuffle();
    }
}