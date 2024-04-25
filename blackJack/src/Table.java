import java.util.List;

public class Table {
	Dealer dealer;
	PlayingDeck deck;
	List<Player> players;
	double payout;

	public Table(Dealer dealer) {
		this.dealer = dealer;
		deck = new PlayingDeck(3);
	}

	public String getPlayingDeck() {
		return deck.toString();
	}

	public void startRound() {

	}

	public void shuffleCards() {
		deck.shuffleDeck();
	}
}
