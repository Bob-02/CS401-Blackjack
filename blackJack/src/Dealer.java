import java.util.ArrayList;
import java.util.List;

public class Dealer {
	String name;
	List<Card> hand;
	double casinoFunds;
	double casinoBet;
	Table table;

	public Dealer(String name, double funds) {
		this.name = name;
		this.casinoFunds = funds;
		casinoBet = 0;

		this.hand = new ArrayList<Card>();
	}

	public String getDealerName() {
		return name;
	}

	public List<Card> getDealerHand() {
		return hand;
	}

	public double getCasinoFunds() {
		return casinoFunds;
	}

	public double getBet() {
		return casinoBet;
	}

	public void setBet(double bet) {
		casinoBet = bet;
	}

	public void wonBet() {
		casinoFunds += casinoBet;
		casinoBet = 0;
	}

	public void lostBet() {
		casinoFunds -= casinoBet;
		casinoBet = 0;
	}

	public Table getTable() {
		return table;
	}
}
