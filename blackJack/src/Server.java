import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class Server {
    public static void main(String[] args) 
    		throws IOException, ClassNotFoundException {

    	// Server variables, Ask if they should be static, b/c server is static?
    	String serverName;			// Passed to client to get server details
    	List<Game> games;			// Passed to client to get server details
    	List<player> validPlayers;	// Loaded from files to validate logins
    	List<Dealer> validDealers;	// Loaded from files to validate logins
    	List<player> onlinePlayers;	// Passed to client to get server details
    	List<Dealer> onlineDealers;	// Passed to client to get server details
    	double casinoFunds;			// Passed to client to get server details
    	
    	// make facade class of server details
    	
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
}