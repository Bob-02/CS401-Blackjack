//ClientHandler class


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	//private final ServerDetails server;
	private static int count = 1;
    private final int id;

	// Constructor
	public ClientHandler(Socket socket)
	{
		this.clientSocket = socket;
		//this.server = server;
		this.id = count++;
	}

	public void run()
	{
		try {
			
			// Make functions in Server call with Server.
			Server.getServerName();
				
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
	        login = validateUser(login);
		        
	        // add this part into validateUser()
	        /* From HERE */
	        
	        // If the login is validated then login is a success and we can send
	        // back the message to the client.
			if(login.getStatus() == Status.Success) {
				System.out.println("Login Successful -- Client #" + id + "\n");
				
				// Send back updated message to the client.
				objectOutputStream.writeObject(login);
			}
			
			// If status of the message was not updated to Success then it has 
			// to be a failed login.
			// Close the client.
			else {
				System.out.println("Invalid credentials supplied, "
								   	+ "closing socket!");
				clientSocket.close();
			}
			/* To HERE */

			// Keep reading for messages until we get a logout message.
			Message current = (Message) objectInputStream.readObject();

			// This is the main loop of the program.
			// All actions from the GUI will go through the client and sent
			// to the server here.
			while (!isLogginOut(current)) {


				//
				// Switch to handle all the various types of messages.
				// Will be controlled by enum Type
				// Data supplied for the servers action should be in the Message
				// text area.
				//
				// Build out the functions as needed and remember to update
				// the message before sending to the Client.
				//

				// Send back updated message to the Client.
				objectOutputStream.writeObject(current);

				// Get another message from the client
				// In the future this might change to a List of Message.
				current = (Message) objectInputStream.readObject();
			}

			// Set the logout process in isLogginout()
			
			/* From Here */
			
			// On receipt of a ‘logout message’ should break out of the loop.
			// Then a status will be returned with ‘Success’, then the 
			// connection will be closed and the thread terminates.
			current.setStatus(Status.Success);
			
			// 

			// Send updated message back to the client
			objectOutputStream.writeObject(current);
			System.out.println("Client #" + id + " Logged out at "
					+ new Date().getCurrentDate());

			// Don't forget to close the client durr.
			clientSocket.close();
			
			/* To Here */

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
	
	private Boolean isNewLogin(Message login) {
		
		// The message is valid if of Type Login and has a Status of New.
		if(login.getType() == Type.Login && login.getStatus() == Status.New) {
			return true;
		}
		
		return false;
	}
	
	private Boolean isLogginOut(Message msg) {
		
		// If the message is of Type Logout and New return TRUE.
		if(msg.getType() == Type.Logout && msg.getStatus() == Status.New) {
			return true;
		}
		
		// logout process Here!
		// Copy from above.
		
		return false;
	}

	
	private Message validateUser(Message login) {
		
        // If we get a NEW login message, check the text supplied in the
        // message and check the server account details in text file.
		if(isNewLogin(login)) {

	        // Login user will return a string as either a:
			// "dealer", "player" or "invalid".
        	String loginType = Server.loginUser(login.getText());
        	
        	// IF account details found Set status to success
	        if(loginType == "dealer" || loginType == "player") {

	        	login.setStatus(Status.Success);
	        }
	        
	        // If neither player or dealer then the login is invalid
	        else {
	        	login.setStatus(Status.Failed);
	        }
	        
	        login.setText(loginType);
		}
		
		// Add logged in succesful message code here!
		
		
		return login;		
	}
}
