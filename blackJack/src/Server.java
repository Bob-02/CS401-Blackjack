
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// make private?
// make a Server variable and refer to itself in constructor.
// make a getter for Server so that client handler can user getters from here?
public class Server {
    public static void main(String[] args) 
    		throws IOException, ClassNotFoundException {

    	// Initiate server values when the server starts up.
    	Server initServerDetails = new Server();
    	
//    	System.out.println(Server.getServerName());
//    	System.out.println(Server.getCasinoFunds());
//    	System.out.println(Server.getValidDealers());
//    	System.out.println(Server.getValidPlayers());

    	// Print local host to console. Let others know where to connect.
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost.getHostAddress());
        
        // Create a ServerSock on local host:7777
        ServerSocket server = new ServerSocket(7777);
        server.setReuseAddress(true);
        
        // Print date and time server starts.
        System.out.println("Server start time: " + new Date().getCurrentDate());
        System.out.println("Server awaiting connections...\n");

        try {

			// running infinite loop for getting
			// client request
			while (true) {

				// socket object to receive incoming client
				// requests
				Socket client = server.accept();

				// Displaying that new client is connected
				// to server
				System.out.println("New client connected from: "
								   + client.getInetAddress().getHostAddress()
								   + " at " + new Date().getCurrentDate());

				// create a new thread object
				ClientHandler clientSock = new ClientHandler(client);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
				
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    // Server details go here. They must be static because main is static.
	private static String serverName;			
	private static List<Game> games;			
	private static List<String> validPlayers;	
	private static List<String> validDealers;	
	private static List<Player> onlinePlayers;	
	private static List<Dealer> onlineDealers;	
	private static BigDecimal casinoFunds;
	
	
	// Startup of server should initiate new variables. Give server a name, 
	// start up new instances of the variables. Add casino funds.
	public Server() {
		Server.serverName = "Group 5 Blackjack Server.";
		Server.games = new ArrayList<>();
		Server.validPlayers =  new ArrayList<>();
		Server.validDealers =  new ArrayList<>();
		Server.onlinePlayers =  new ArrayList<>();
		Server.onlineDealers =  new ArrayList<>();
		
		// Casino starts off with 2 million bucks.
		Server.casinoFunds = new BigDecimal("2000000.00");
		
    	// Load valid registered Players and Dealers from both files.
    	loadValidUsers("dealers.txt", "players.txt");
		
	}
    
	public static BigDecimal getCasinoFunds() {
		return casinoFunds;
	}

	public static void setCasinoFunds(BigDecimal casinoFunds) {
		Server.casinoFunds = casinoFunds;
	}

	public static List<Game> getGames() {
		return games;
	}

	public static List<String> getValidPlayers() {
		return validPlayers;
	}

	public static List<String> getValidDealers() {
		return validDealers;
	}

	public static List<Player> getOnlinePlayers() {
		return onlinePlayers;
	}

	public static List<Dealer> getOnlineDealers() {
		return onlineDealers;
	}

	
	public static String getServerName() {
		return serverName;
	}
	
	
	// With the supplied filename for Players and Dealers. Registered users will
	// be added to each valid Dealer and Player list.
	public static void loadValidUsers(String dealerFilename, 
									  String playerFilename) {
		
		File dealerFile = new File(dealerFilename);
		File playerFile = new File(playerFilename);
		
		try (Scanner dealer = new Scanner(dealerFile);
	         Scanner player = new Scanner(playerFile) ) {
			
	            // Continue the loop as long as either file has more data
	            while (dealer.hasNextLine() || player.hasNextLine()) {
	            	
	            	// When dealer has a line read add to the list
	                if (dealer.hasNextLine()) {
	                	
	                    String line1 = dealer.nextLine();
	                    
	                    // If the details are NOT in the list add them else do
	                    // nothing.
	                    if(!validDealers.contains(line1) ) {
	                    	validDealers.add(line1);
	                    }
	                }
	                
	                if (player.hasNextLine()) {
	                	
	                    String line2 = player.nextLine();
	                    // If the details are NOT in the list add them else do
	                    // nothing.
	                    if(!validPlayers.contains(line2) ) {
	                    	validPlayers.add(line2);
	                    }
	                }
	            }
	        } catch (FileNotFoundException e) {
	        	
	            System.out.println("One of the files was not found: "
	            				   + e.getMessage());
	        }
	}
	
	
	// loginUser will take the supplied text string from the message class.
	// String format userDetails should be USERNAME:PASSWORD
	// userDetails will be used to check validPlayers and validDealers and
	// return a string to send back to the client to load the GUI.
	public static String loginUser(String userDetails) {
		
		String details[] = userDetails.split(":");
		String username = details[0];
		
		// If the details match in the Dealers list and not in the Players list
		// then the details are a valid Dealer.
		if(validDealers.contains(userDetails) 
		   && !validPlayers.contains(userDetails)) {
			
			// Create new Dealer
			// Dealer will always get a set of funds from casinoFunds
			Dealer newDealer = new Dealer(username, getCasinoFunds("10000.00"));
			
			// Add Dealer to onlineDealers IFF that Dealer isn't already logged
			// in.
			if(onlineDealers.contains(newDealer) ) {
				return "invalid";
			}
			
			onlineDealers.add(newDealer);
			
			// Return dealer type to client.
			return "dealer";
		}
		
		// Now do the same and check the Players list.
		else if(validPlayers.contains(userDetails) 
				&& !validDealers.contains(userDetails)) {
			
			// Create new Player
			// Players are a public class just set its funds a default amount.
			Player newPlayer = new Player(username, 1000.00);
			
			// Add Player to onlinePlayers IFF that Player isn't already logged
			// in.
			if(onlinePlayers.contains(newPlayer) ) {
				return "invalid";
			}
			
			onlinePlayers.add(newPlayer);
			
			// Return player type to client.
			return "player";
		}
		
		// If details don't match either list its invalid.
		return "invalid";
	}

	
	// Registers a new Player to the Server.
	// returns a string as either registered. or User name taken.
	public static String registerUser(String userDetails) throws IOException {
		
		// look up in the list, if in there, return user already registered.
		// if not add to list
		
		String details[] = userDetails.split(":");
		
		// Details in the wrong format, immediately ignore.
		if(details.length != 2) {
			
			return "";
		}
		
		String username = details[0];
		
		if(validPlayers.contains(userDetails)) {

			return "taken";
		}
		
		// add to players file
		try(FileWriter file = new FileWriter("players.txt", true)) {
			file.append(userDetails + "\n");
		}
		
		
		validPlayers.add(userDetails);
		
		Player newPlayer = new Player(username, 1000.00);
		onlinePlayers.add(newPlayer);
		
		return "registerd";
	}
	
	
	// Gets funds from Server.
	private static double getCasinoFunds(String request) {
		
		BigDecimal rounded = null;
		BigDecimal requested = new BigDecimal(request);	// double to BigDecimal.
		
		// Get funds from casinoFunds.
		// If compareTo() returns with a negative. Then request is less than
		// casinoFunds.
		if(requested.compareTo(casinoFunds) < 0) {	// if(request < casinoFunds)
			
			// The funds are available subtract from casinoFunds by request. 
			// Send back in type double.
			
			// Round the request like how a bank would.
			rounded = requested.setScale(2, RoundingMode.HALF_EVEN);
			
			// Subtract requested amount from the casino funds.
			casinoFunds.subtract(rounded);
			return rounded.doubleValue();
		}
		
		return 0;
	}
	
	
	// Read from player's file, fund file.
	// For now just do a simple add of funds to the player with a default amount
	// of cash to use for now.
	public static double getPlayerFunds(String player) {
		return 10000.00;
	}
	
	
	// This adds a set amount of funds to the casino.
	public static void addCasinoFunds(String amount) {
		
		// Convert amount string to a decimal value.
		BigDecimal bdAmount = 
				new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
		
		// Add to casinoFunds.
		casinoFunds.add(bdAmount);
	}
	
	
	// Gets a target Player using a supplied name.
	public static Player getTargetPlayer(String username) {
		
		// Go through the list and if we find a matching player by the username
		// return that player.
		for(Player p : onlinePlayers ) {
			
			if(p.getPlayerName().equals(username) ) {
				
				return p;
			}
		}
		
		
		// If not found on the list.
		return null;
	}
	
	
	// Gets a target Dealer using a supplied name.
	public static Dealer getTargetDealer(String username) {
		
		// Go through the list and if we find a matching Dealer by the username
		// return that Dealer.
		for(Dealer d : onlineDealers) {
			
			if(d.getDealerName().equals(username) ) {
				
				return d;
			}
		}
		
		
		// If not found on the list.
		return null;
	}
	
	
	// Gets a target game by its ID.
	public static Game getTargetGame(String ID) {
		
		// Go through all the games on the list until we find a matching ID.
		for(Game g : games) {
			
			// If a game matches the ID wanted return that game.
			if(g.getID() == ID) {
				
				return g;
			}	
		}

		// If not found on the list.
		return null;
	}	
}