
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Server {
    public static void main(String[] args) 
    		throws IOException, ClassNotFoundException {

    	// Initiate server values when the server starts up.
    	Server initServerDetails = new Server();

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
				ClientHandler clientSock = 
						new ClientHandler(client);

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
		
		// Casino starts off with 2 mil.
		Server.casinoFunds = new BigDecimal("2000000.00");
		
    	// Load valid registered Players and Dealers from both files.
    	loadValidUsers("players.txt", "dealers.txt");
		
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
		File playerFile = new File(dealerFilename);
		
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
	
	// Read from player's file, fund file.
	// For now just do a simple add of funds to the player	
	public static void loadPlayerFunds(Player player) {
	}
	
	public static String loginUser(String text) {
		String userType;
		// TODO Auto-generated method stub
		return userType;
	}
    
}

