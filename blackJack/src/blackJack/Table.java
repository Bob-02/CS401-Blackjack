package blackJack;

public class Table {
	Dealer dealer;
	PlayingDeck deck;
	Player[] players = new Player[7];
	double payout;

	public Table() {

	}

	public String getPlayingDeck() {
		return deck.toString();
	}

	public void startRound() {

	}

	public void shuffleCards() {
		// TODO: make sure shuffling works
		deck.shuffleDeck();
	}
}
