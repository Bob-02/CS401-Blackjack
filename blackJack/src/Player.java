import java.util.ArrayList;
import java.util.List;

public class Player {
	String name;
	List<Card> hand;
	double funds;
	double currentBet;

	public Player(String name, double funds) {
		this.name = name;
		this.funds = funds;
		currentBet = 0;

		this.hand = new ArrayList<Card>();
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

	public double getBet() {
		return currentBet;
	}

	public void setBet(double bet) {
		currentBet = bet;
	}

	public void wonBet() {
		funds += currentBet;
		currentBet = 0;
	}

	public void lostBet() {
		funds -= currentBet;
		currentBet = 0;
	}
}
