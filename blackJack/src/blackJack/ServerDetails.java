package blackJack;
import java.util.List;

public class ServerDetails {
	protected static ServerDetails uniqueInstance = new ServerDetails();
	
	// Server variables
	private String serverName;			
	private List<Game> games;			
	private List<Player> validPlayers;	
	private List<Dealer> validDealers;	
	private List<Player> onlinePlayers;	
	private List<Dealer> onlineDealers;	
	private double casinoFunds;			
	
	
	
	protected ServerDetails() {
		// Create a default server name
		// Load Player accounts from file to validPlayers
		// Load Dealer accounts from file to validDealers
		
	}
	
	public static synchronized ServerDetails getInstance() {
		return uniqueInstance;
	}
	
	// Other useful methods here.
}