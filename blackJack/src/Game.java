import java.util.List;

public class Game {
	Table table;
	Dealer dealer;
	TableStatus tableStatus;
	List<Player> lobby;
	long emptyTimeStamp;

	public Game(Dealer dealer, List<Player> lobby) {
		this.dealer = dealer;
		this.lobby = lobby;
		table = new Table(dealer);
		tableStatus = TableStatus.Open;
	}

	public Table getTable() {
		return table;
	}

	public boolean isTableFull() {
		return tableStatus == TableStatus.Full;
	}

	public boolean isGameOpen() {
		return tableStatus == TableStatus.Open;
	}

	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	public void removePlayer(Player player) {
		if (tableStatus == TableStatus.Full) {
			tableStatus = TableStatus.Open;
		}
		table.clearPlayerHand(player);
		table.players.remove(player);
		lobby.remove(player);

	}

	public void addPlayer(Player player) {
		if (tableStatus != TableStatus.Full) {
			lobby.add(player);
			table.players.add(player);
			if (table.players.size() == 7) {
				tableStatus = TableStatus.Full;
			}
		}
	}
}
