import java.util.List;

public class Player {
	String name;
	List<Card> hand;
	double funds;

	public Player() {

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
