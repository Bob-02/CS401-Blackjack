import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Table {
	Dealer dealer;
	PlayingDeck deck;
	List<Player> players;
	double payout;

	public Table(Dealer dealer, List<Player> players) {
		this.dealer = dealer;
		deck = new PlayingDeck(3);

		this.players = new ArrayList<Player>();
		for (Player player : players) {
			this.players.add(new Player(player.getPlayerName(), player.getPlayerFunds()));
		}

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

		for (Player player : players) {
			addCardToPlayerHand(player, deal());
		}

		addCardToDealerHand(dealer, deal());
	}

	public Card deal() {
		return deck.dealACard();
	}

	public void addCardToPlayerHand(Player player, Card card) {
		player.getPlayerHand().add(card);
		System.out.println("Player, " + player.getPlayerName() + ", has Hand: " + player.getPlayerHand().toString());
	}

	public void addCardToDealerHand(Dealer dealer, Card card) {
		dealer.getDealerHand().add(card);
		System.out.println("Dealer, " + dealer.getDealerName() + ", has Hand: " + dealer.getDealerHand().toString());
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

	public static int pickRandomIndex(int Min, int Max) {
		return (int) (Math.random() * (Max - Min)) + Min;
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
