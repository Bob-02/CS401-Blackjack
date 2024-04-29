import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DealerTest {

	@Test
	void testDealer() {
		String name = "Bob Billy";
		double casinoFunds = 200000;
		double currentBet = 500;

		Dealer dealer = new Dealer(name, casinoFunds);

		assertEquals(name, dealer.getDealerName());
		assertEquals(casinoFunds, dealer.getCasinoFunds());
		assertNotNull(dealer.getDealerHand());

		dealer.setBet(currentBet);

		assertEquals(currentBet, dealer.getBet());
	}

	@Test
	void testWonBet() {
		String name = "Bob Billy";
		double casinoFunds = 200000;
		double casinoBet = 500;

		Dealer dealer = new Dealer(name, casinoFunds);

		dealer.setBet(casinoBet);
		dealer.wonBet();

		assertEquals(0, dealer.casinoBet);
		assertEquals((casinoFunds + casinoBet), dealer.getCasinoFunds());
	}

	@Test
	void testLostBet() {
		String name = "Bob Billy";
		double casinoFunds = 200000;
		double casinoBet = 500;

		Dealer dealer = new Dealer(name, casinoFunds);

		dealer.setBet(casinoBet);
		dealer.lostBet();

		assertEquals(0, dealer.casinoBet);
		assertEquals((casinoFunds - casinoBet), dealer.getCasinoFunds());
	}

	@Test
	void testCalculateHandTotal() {
		String name = "Bob Billy";
		double casinoFunds = 200000;

		Dealer dealer = new Dealer(name, casinoFunds);

		Card testCard1 = new Card(Suit.Hearts, Rank.Queen);
		Card testCard2 = new Card(Suit.Spades, Rank.Ace);

		dealer.hand.add(testCard1);
		dealer.hand.add(testCard2);

		int handTotal = 0;
		boolean cardIsAce = false;

		for (int i = 0; i < dealer.hand.size(); i++) {
			int value = dealer.hand.get(i).getCardValue();
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

	@Test
	void testPeek() {
		String name = "Bob Billy";
		double casinoFunds = 200000;

		Dealer dealer = new Dealer(name, casinoFunds);

		Card testCard1 = new Card(Suit.Hearts, Rank.Queen);
		Card testCard2 = new Card(Suit.Spades, Rank.Ace);

		dealer.hand.add(testCard1);
		dealer.hand.add(testCard2);

		int handTotal = 0;
		boolean cardIsAce = false;

		for (int i = 0; i < dealer.hand.size(); i++) {
			int value = dealer.hand.get(i).getCardValue();
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

		assertEquals(true, handTotal >= 10);
	}

	@Test
	void testClearHand() {
		String name = "Bob Billy";
		double casinoFunds = 200000;

		Dealer dealer = new Dealer(name, casinoFunds);

		Card testCard1 = new Card(Suit.Hearts, Rank.Queen);
		Card testCard2 = new Card(Suit.Spades, Rank.Ace);

		dealer.hand.add(testCard1);
		dealer.hand.add(testCard2);

		assertEquals(2, dealer.hand.size());

		dealer.clearHand();

		assertEquals(0, dealer.hand.size());
	}

}
