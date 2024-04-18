import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) 
    		throws IOException, ClassNotFoundException {
    	
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