import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
	Table table;
	Dealer dealer;
	static TableStatus tableStatus;
	List<Player> lobby;
	long emptyTimeStamp;
	private Scanner gameManager = new Scanner(System.in);

	public Game(Dealer dealer, List<Player> lobby) {
		this.dealer = dealer;
		this.lobby = lobby;
		table = new Table(dealer, lobby);
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

	public void getBets() {
		double betValue;

		for (int i = 0; i < table.players.size(); i++) {
			if (table.players.get(i).getPlayerFunds() > 0) {
				do {
					System.out.print("How much do you want to bet " + table.players.get(i).getPlayerName()
							+ (" (1-" + table.players.get(i).getPlayerFunds()) + ")? ");
					betValue = gameManager.nextDouble();
					table.players.get(i).setBet(betValue);
				} while (!(betValue > 0 && betValue <= table.players.get(i).getPlayerFunds()));
				System.out.println("");
			}

		}

	}

	// Initial check for dealer or player Blackjack
	public void checkBlackjack() {
		// System.out.println();

		if (dealer.doesTheDealerHaveBlackJack()) {
			System.out.println("Dealer has BlackJack!");
			for (int i = 0; i < table.players.size(); i++) {
				if (table.players.get(i).calculateHandTotal() == 21) {
					System.out.println(table.players.get(i).getPlayerName() + " pushes");
					table.players.get(i).pushed();
				} else {
					System.out.println(table.players.get(i).getPlayerName() + " loses");
					table.players.get(i).lostBet();
				}
			}
		} else {
			if (dealer.peek()) {
				System.out.println("Dealer peeks and does not have a BlackJack");
			}

			for (int i = 0; i < table.players.size(); i++) {
				if (table.players.get(i).calculateHandTotal() == 21) {
					System.out.println(table.players.get(i).getPlayerName() + " has blackjack!");
					table.players.get(i).calculateHandTotal();
				}
			}
		}
	}

	// This code takes the user commands to hit or stand
	public void hitOrStand() {
		String command;
		char c;
		for (int i = 0; i < table.players.size(); i++) {
			if (table.players.get(i).getBet() > 0) {
				System.out.println();
				System.out
						.println(table.players.get(i).getPlayerName() + " has " + table.players.get(i).hand.toString());

				do {
					do {
						System.out.print(table.players.get(i).getPlayerName() + " (H)it or (S)tand? ");
						command = gameManager.next();
						c = command.toUpperCase().charAt(0);
					} while (!(c == 'H' || c == 'S'));
					if (c == 'H') {
						table.addCardToPlayerHand(table.players.get(i), table.deal());
						System.out.println(table.players.get(i).getPlayerName() + " has "
								+ table.players.get(i).getPlayerHand().toString());
					}
				} while (c != 'S' && table.players.get(i).calculateHandTotal() <= 21);
			}
		}
	}

	// This code calculates all possible outcomes and adds or removes the player
	// bets

	public void settleBets() {
		System.out.println();

		for (int i = 0; i < table.players.size(); i++) {
			if (table.players.get(i).getBet() > 0) {
				if (table.players.get(i).calculateHandTotal() > 21) {
					System.out.println(table.players.get(i).getPlayerName() + " has busted");
					table.players.get(i).lostBet();
				} else if (table.players.get(i).calculateHandTotal() == dealer.calculateHandTotal()) {
					System.out.println(table.players.get(i).getPlayerName() + " has pushed");
					table.players.get(i).pushed();
				} else if (table.players.get(i).calculateHandTotal() < dealer.calculateHandTotal()
						&& dealer.calculateHandTotal() <= 21) {
					System.out.println(table.players.get(i).getPlayerName() + " has lost");
					table.players.get(i).lostBet();
				} else if (table.players.get(i).calculateHandTotal() == 21) {
					System.out.println(table.players.get(i).getPlayerName() + " has won with blackjack!");
					table.players.get(i).hasBlackjack();
				} else {
					System.out.println(table.players.get(i).getPlayerName() + " has won");
					table.players.get(i).wonBet();

				}
			}
		}

	}

	public static void main(String[] args) {
		Dealer dealer = new Dealer("Billy", 1000);
		List<Player> testPlayers = new ArrayList<Player>();

		Player testPlayer = new Player("Bob", 1000);
		testPlayers.add(testPlayer);

		Game testGame = new Game(dealer, testPlayers);

		testGame.table.dealCards();
		testGame.table.dealCards();
		testGame.table.dealCards();

		testPlayer.setBet(500);
		dealer.setBet(500);

		System.out.println("Player, " + testPlayer.getPlayerName() + " has bet: " + testPlayer.getBet());
		System.out.println("Dealer, " + dealer.getDealerName() + " has bet: " + dealer.getBet());

		testPlayer.wonBet();
		dealer.lostBet();

		System.out
				.println("Player, " + testPlayer.getPlayerName() + " has total funds: " + testPlayer.getPlayerFunds());
		System.out.println("Dealer, " + dealer.getDealerName() + " has total funds: " + dealer.getCasinoFunds());

	}
}
