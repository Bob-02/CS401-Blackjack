import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PlayerTest {

	@Test
	void testPlayer() {
		String name = "Billy Bob";
		double funds = 1000;
		double currentBet = 500;

		Player player = new Player(name, funds);

		assertEquals(name, player.getPlayerName());
		assertEquals(funds, player.getPlayerFunds());
		assertNotNull(player.getPlayerHand());

		player.setBet(currentBet);

		assertEquals(currentBet, player.getBet());
	}

	@Test
	void testWonBet() {
		String name = "Billy Bob";
		double funds = 1000;
		double currentBet = 500;

		Player player = new Player(name, funds);

		player.setBet(currentBet);
		player.wonBet();

		assertEquals(0, player.currentBet);
		assertEquals((funds + currentBet), player.getPlayerFunds());
	}

	@Test
	void testLostBet() {
		String name = "Billy Bob";
		double funds = 1000;
		double currentBet = 500;

		Player player = new Player(name, funds);

		player.setBet(currentBet);
		player.lostBet();

		assertEquals(0, player.currentBet);
		assertEquals((funds - currentBet), player.getPlayerFunds());
	}

	@Test
	void testHasBlackjack() {
		String name = "Billy Bob";
		double funds = 1000;
		double currentBet = 500;

		Player player = new Player(name, funds);

		player.setBet(currentBet);
		player.hasBlackjack();

		assertEquals(0, player.currentBet);
		assertEquals((funds + (currentBet * 1.5)), player.getPlayerFunds());
	}

	@Test
	void testPushed() {
		String name = "Billy Bob";
		double funds = 1000;
		double currentBet = 500;

		Player player = new Player(name, funds);

		player.setBet(currentBet);
		player.pushed();

		assertEquals(0, player.currentBet);
		assertEquals(funds, player.getPlayerFunds());
	}

	@Test
	void testClearHand() {
		String name = "Billy Bob";
		double funds = 1000;

		Player player = new Player(name, funds);

		Card testCard1 = new Card(Suit.Hearts, Rank.Queen);
		Card testCard2 = new Card(Suit.Spades, Rank.Ace);

		player.hand.add(testCard1);
		player.hand.add(testCard2);

		assertEquals(2, player.hand.size());

		player.clearHand();

		assertEquals(0, player.hand.size());

	}

	@Test
	void testCalculateHandTotal() {
		String name = "Billy Bob";
		double funds = 1000;

		Player player = new Player(name, funds);

		Card testCard1 = new Card(Suit.Hearts, Rank.Queen);
		Card testCard2 = new Card(Suit.Spades, Rank.Ace);

		player.hand.add(testCard1);
		player.hand.add(testCard2);

		int handTotal = 0;
		boolean cardIsAce = false;

		for (int i = 0; i < player.hand.size(); i++) {
			int value = player.hand.get(i).getCardValue();
			if (value > 10) {
				value = 10;
			} else if (value == 1) {
				cardIsAce = true;
			}
			handTotal += value;
		}
		if (cardIsAce && handTotal + 10 <= 21) {
			handTotal += 10;
		}

		assertEquals(21, handTotal);
	}

}
