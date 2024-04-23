import java.util.List;

public class Dealer {
	String name;
	List<Card> hand;
	double casinoFunds;
	Table table;

	public Dealer() {

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

	public Table getTable() {
		return table;
	}
}
