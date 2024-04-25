import java.util.List;
import java.util.Random;

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
		clearAllHands();
		dealCards();
	}

	public void shuffleCards() {
		deck.shuffleDeck();
	}

	public void dealCards() {

		shuffleCards();

		for (Player player : players) {
			addCardToPlayerHand(player, pickRandomCard());
		}

		addCardToDealerHand(dealer, pickRandomCard());
	}

	public Card pickRandomCard() {
		Random rand = new Random();

		Deck selectedDeck = deck.playingDeck.get(rand.nextInt((deck.playingDeck.size() - 0) + 1) + 0);

		Card selectedCard = selectedDeck.cards.get(((selectedDeck.cards.size() - 0) + 1) + 0);

		selectedDeck.cards.remove(selectedCard);

		return selectedCard;
	}

	public void addCardToPlayerHand(Player player, Card card) {
		player.getPlayerHand().add(card);
	}

	public void addCardToDealerHand(Dealer dealer, Card card) {
		dealer.getDealerHand().add(card);
	}

	public void clearAllHands() {
		for (Player player : players) {
			clearPlayerHand(player);
		}

		clearDealerHand(dealer);
	}

	public void clearPlayerHand(Player player) {
		Random rand = new Random();
		for (int i = 0; i < player.getPlayerHand().size(); i++) {
			Deck selectedDeck = deck.playingDeck.get(rand.nextInt((deck.playingDeck.size() - 0) + 1) + 0);
			selectedDeck.cards.add(player.getPlayerHand().get(i));
			player.getPlayerHand().remove(i);
		}
	}

	public void clearDealerHand(Dealer dealer) {
		Random rand = new Random();
		for (int i = 0; i < dealer.getDealerHand().size(); i++) {
			Deck selectedDeck = deck.playingDeck.get(rand.nextInt((deck.playingDeck.size() - 0) + 1) + 0);
			selectedDeck.cards.add(dealer.getDealerHand().get(i));
			dealer.getDealerHand().remove(i);
		}
	}
}
