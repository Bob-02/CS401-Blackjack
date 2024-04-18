//ClientHandler class

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
	        
	        // Get the first message from client. It should be a login message.
	        // Ignore anything else.
	        Message login = (Message) objectInputStream.readObject();
	        
	        // If we get a NEW login message, check the text supplied in the
	        // message and check the server account details in text file.
	        

	        if(login.getType() == Type.Login 
	        		&& login.getStatus() == Status.New) {
	        	
	        	
		        // look in the server here!!!
	        	Boolean loginValid = loginUser(login.getText());
	        	
		        // A valid dealer login: type login, status new, text dealer.
		        // A valid player login: type login, status new, text player.
		        if(loginValid == true) {
	
		        	// IF account details found Set status to success
		        	login.setStatus(Status.Success);
		        	
		        	// Send updated message back to the client
		        	objectOutputStream.writeObject(login);
		        	System.out.println("Login Successful from Client #"
		        						+ id + "!\n");
		        	
		        	// ADD PLAYER OR DEALER TO ONLINE LIST.
		        }
		        // If there is not Player or Dealer valid account.
		        // Close the client.
		        else {
		        	return;
		        }
	        	
	        	Message current = (Message) objectInputStream.readObject();
	        	
	        	// Keep reading for text messages until we get a logout message.
	        	// This is the main loop of the program.
	        	// All actions from the GUI will go through the client and sent
	        	// to the server here.
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
