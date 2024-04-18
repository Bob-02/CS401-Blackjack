import java.util.List;

public class Game {
	Table table;
	Dealer dealer;
	Status status;
	List<Player> lobby;
	long emptyTimeStamp;

	public Game() {

	}

	public Table getTable() {
		return table;
	}

	public boolean isTableFull() {
		return false;
	}

	public boolean isGameOpen() {
		return false;
	}

	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	public void removePlayer(Player player) {
		lobby.remove(player);
	}

	public void addPlayer(Player player) {
		lobby.add(player);
	}
}
