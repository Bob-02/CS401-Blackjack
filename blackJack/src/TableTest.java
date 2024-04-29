import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

class TableTest {

	@Test
	void testTable() {

		Dealer dealer = new Dealer("Billy", 1000);
		List<Player> testPlayers = new ArrayList<Player>();
		Player testPlayer = new Player("Bob", 1000);
		testPlayers.add(testPlayer);

		PlayingDeck testDeck = new PlayingDeck(3);

		assertEquals((52 * 3), (testDeck.playingDeck.get(0).cards.size() + testDeck.playingDeck.get(1).cards.size()
				+ testDeck.playingDeck.get(2).cards.size()));
		assertNotNull(dealer.getDealerName());
		assertNotNull(dealer.getCasinoFunds());
		assertNotEquals(0, testPlayers.size());
		assertNotNull(testPlayer.getPlayerName());
		assertNotNull(testPlayer.getPlayerFunds());
	}

	@Test
	void testShuffleCards() {
		PlayingDeck testDeck = new PlayingDeck(3);
		Card testCard = testDeck.playingDeck.get(0).cards.get(0);
		assertEquals(testCard, testDeck.playingDeck.get(0).cards.get(0));
		testDeck.shuffleDeck();
		assertNotEquals(testCard, testDeck.playingDeck.get(0).cards.get(0));
	}

	@Test
	void testDeal() {
		PlayingDeck testDeck = new PlayingDeck(3);
		assertNotNull(testDeck.dealACard());
	}

	@Test
	void testAddCardToPlayerHand() {
		Player player = new Player("Bob", 1000);
		Card card = new Card(Suit.Hearts, Rank.Queen);
		assertEquals(0, player.getPlayerHand().size());
		player.getPlayerHand().add(card);
		assertEquals(1, player.getPlayerHand().size());
	}

	@Test
	void testAddCardToDealerHand() {
		Dealer dealer = new Dealer("Billy", 1000);
		Card card = new Card(Suit.Hearts, Rank.Queen);
		assertEquals(0, dealer.getDealerHand().size());
		dealer.getDealerHand().add(card);
		assertEquals(1, dealer.getDealerHand().size());
	}

	@Test
	void testClearAllHands() {
		Random rand = new Random();
		Player player = new Player("Bob", 1000);
		PlayingDeck testDeck = new PlayingDeck(3);
		for (int i = 0; i < player.getPlayerHand().size(); i++) {
			Deck selectedDeck = testDeck.playingDeck.get(rand.nextInt((testDeck.playingDeck.size() - 0) + 1) + 0);
			selectedDeck.cards.add(player.getPlayerHand().get(i));
			player.getPlayerHand().remove(i);
		}

		Dealer dealer = new Dealer("Billy", 1000);
		for (int i = 0; i < dealer.getDealerHand().size(); i++) {
			Deck selectedDeck = testDeck.playingDeck.get(rand.nextInt((testDeck.playingDeck.size() - 0) + 1) + 0);
			selectedDeck.cards.add(dealer.getDealerHand().get(i));
			dealer.getDealerHand().remove(i);
		}

		assertEquals(0, player.getPlayerHand().size());
		assertEquals(0, dealer.getDealerHand().size());
	}

	@Test
	void testClearPlayerHand() {
		Random rand = new Random();
		Player player = new Player("Bob", 1000);
		PlayingDeck testDeck = new PlayingDeck(3);
		for (int i = 0; i < player.getPlayerHand().size(); i++) {
			Deck selectedDeck = testDeck.playingDeck.get(rand.nextInt((testDeck.playingDeck.size() - 0) + 1) + 0);
			selectedDeck.cards.add(player.getPlayerHand().get(i));
			player.getPlayerHand().remove(i);
		}

		assertEquals(0, player.getPlayerHand().size());
	}

	@Test
	void testClearDealerHand() {
		Random rand = new Random();
		Dealer dealer = new Dealer("Billy", 1000);
		PlayingDeck deck = new PlayingDeck(3);
		for (int i = 0; i < dealer.getDealerHand().size(); i++) {
			Deck selectedDeck = deck.playingDeck.get(rand.nextInt((deck.playingDeck.size() - 0) + 1) + 0);
			selectedDeck.cards.add(dealer.getDealerHand().get(i));
			dealer.getDealerHand().remove(i);
		}

		assertEquals(0, dealer.getDealerHand().size());
	}

}
