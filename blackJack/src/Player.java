import java.util.List;

public class Player {
	String name;
	List<Card> hand;
	double funds;

	public Player(String name, double funds) {
		this.name = name;
		this.funds = funds;
	}

	public String getPlayerName() {
		return name;
	}

	public List<Card> getPlayerHand() {
		return hand;
	}

	public double getPlayerFunds() {
		return funds;
	}
}
