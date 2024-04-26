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
    
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

	// Constructor
	public ClientHandler(Socket socket) throws IOException
	{
		this.clientSocket = socket;
		this.id = count++;
		
		this.outputStream = this.clientSocket.getOutputStream();
		this.objectOutputStream = new ObjectOutputStream(this.outputStream);
		
		this.inputStream = this.clientSocket.getInputStream();
		this.objectInputStream = new ObjectInputStream(this.inputStream);
	}

	public void run()
	{
		try {
	        
	        // Get the first message from client. It should be a login message.
	        // Ignore anything else.
	        Message login = (Message) objectInputStream.readObject();
	        
	        // If we get a NEW login message, check the text supplied in the
	        // message and check the server account details in text file.
	        login = validateUser(login);

	        
	        // If the login is validated then login is a success and we can send
	        // back the message to the client.
			if(login.getStatus() == Status.Success) {

				sendToClient(login);
			}
			
			// If status of the message was not updated to Success then it has 
			// to be a failed login.
			// Close the client.
			else {
				System.out.println("Invalid credentials supplied, "
								   	+ "closing socket!");
				clientSocket.close();
			}


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
				//objectOutputStream.writeObject(current);
				sendToClient(current);

				// Get another message from the client
				// In the future this might change to a List of Message.
				current = (Message) objectInputStream.readObject();
			}

			// On receipt of a ‘logout message’ should break out of the loop.
			// Then a status will be returned with ‘Success’, then the 
			// connection will be closed and the thread terminates.
			current.setStatus(Status.Success);
			

			// Send updated message back to the client
			//objectOutputStream.writeObject(current);
			sendToClient(current);
			System.out.println("Client #" + id + " Logged out at "
					+ new Date().getCurrentDate());

			// Don't forget to close the client durr.
			clientSocket.close();

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
			
			// logout process Here!
			// Copy from above.
			
			return true;
		}
		

		// Else this message is not a logout message. Proceed to process the
		// message accordingly.
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
	        	
	        	System.out.println("Login Successful -- Client #" + id + "\n");
	        	login.setStatus(Status.Success);
	        }
	        
	        // If neither player or dealer then the login is invalid
	        else {
	        	
	        	System.out.println("Login Failed -- Client #" + id + "\n");
	        	login.setStatus(Status.Failed);
	        }
	        
	        login.setText(loginType);
		}	
		return login;		
	}
	
	// make a general send to client function
	// to send a message to the client whenever called from a function in the
	// main loop's switch.
	private void sendToClient(Message message) throws IOException {
		
	    // Data TOO the client
        OutputStream outputStream;
		try {
			
			// Object
			outputStream = clientSocket.getOutputStream();
	        ObjectOutputStream objectOutputStream =
	        		new ObjectOutputStream(outputStream);
	        
	        objectOutputStream.writeObject(message);
	        
		} catch (IOException e) {
			System.out.println("Something Borked! Closing socket!\n");
			clientSocket.close();
			e.printStackTrace();
		}
	}
	
}
