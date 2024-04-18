import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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


//ClientHandler class
class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private static int count = 1;
    private final int id;

	// Constructor
	public ClientHandler(Socket socket)
	{
		this.clientSocket = socket;
		this.id = count++;
	}

	public void run()
	{
		try {
				
		    // Data TOO the client
	        OutputStream outputStream = clientSocket.getOutputStream();
	        ObjectOutputStream objectOutputStream =
	        		new ObjectOutputStream(outputStream);
	        
	        // Data FROM the client
	        InputStream inputStream = clientSocket.getInputStream();
	        ObjectInputStream objectInputStream = 
	        		new ObjectInputStream(inputStream);
	        
	        // get message from client.
	        Message current = (Message) objectInputStream.readObject();
	        
	        // If we get a NEW login message, check the text supplied in the
	        // message and check the server account details in text file.
	        
	        // A valid dealer, type login, status new, text, dealer.
	        // A valid player, type login, status new, text, player.
	        if(current.getType() == Type.Login 
	        		&& current.getStatus() == Status.New) {
	        	
	        	
		        // look in the server here!!!
		        // Boolean valid = loginUser(current.getText());
		        // if(valid) {
	
	        	// IF account details found Set status to success
	        	current.setStatus(Status.Success);
	        	
	        	// Send updated message back to the client
	        	objectOutputStream.writeObject(current);
	        	System.out.println("Login Successful from Client #"
	        						+ id + "!\n");
	        	
	        	//}
	        	
	        	// Keep reading for text messages until we get a logout message.'
	        	// add 'valid' flag to loop.
	        	while(current.getType() != Type.Logout
	        			&& current.getStatus() == Status.New) {
	        		
	        		// Get a message from the user. 
	        		current = (Message) objectInputStream.readObject();
	        		
	        		//
	        		// Switch to handle all the various types of messages?
	        		//
	        		
	        		}
	        	
	        	
	        	// On receipt of logout, a ‘logout message’ will be returned
	        	// with status of ‘success’, then the connection will be
	        	// closed by the server and the thread terminates.
	        	current.setStatus(Status.Success);
	        	
	        	// Send updated message back to the client
	        	objectOutputStream.writeObject(current);
	        	System.out.println("Client #" + id + " Logged out at "
	        			+ new Date().getCurrentDate());
	        }

		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}
}

public boolean loginUser(String string) {
	
}


class Date {
	private String date;
	
	public Date() {
		date = getCurrentDate();
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getCurrentDate() {
		DateTimeFormatter dateFormat =
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return now.format(dateFormat);
	}
}
