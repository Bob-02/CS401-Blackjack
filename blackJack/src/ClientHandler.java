//ClientHandler class


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;

	private static int count = 1;
    private final int id;
    
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    
    private Player playerUser;
    private Dealer dealerUser;

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
			System.out.println(Server.getServerName());
	    	System.out.println(Server.getCasinoFunds());
	    	System.out.println(Server.getValidDealers());
	    	System.out.println(Server.getValidPlayers());
	        
	        // Get the first message from client. It should be a login message.
	        // Ignore anything else.
	        Message login = (Message) objectInputStream.readObject();
	        
	        // If we get a NEW login message, check the text supplied in the
	        // message and check the server account details in text file.
	        // Will also set the clientHandler to either a Player or Dealer.
	        login = validateUser(login);

	        
	        // If the login is validated then login is a success and we can send
	        // back the message to the client.
			if(login.getStatus() == Status.Success) {

				//objectOutputStream.writeObject(login);
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
				
				// Send back updated message to the Client.
				// At this point the only message passed would be a logout Type
				// Message.
				//objectOutputStream.writeObject(current);
				sendToClient(current);

				// Get another message from the client
				// In the future this might change to a List of Message.
				current = (Message) objectInputStream.readObject();
			}

			// On receipt of a ‘logout message’ should break out of the loop.
			// Then a status will be returned with ‘Success’, then the 
			// connection will be closed and the thread terminates.

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
			
			// Acknowledge logout Message
			msg.setStatus(Status.Success);
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
	        	
	        	String details[] = login.getText().split(":");
	    		String username = details[0];
	        	
	    		// Set target dealer to the client handler.
	    		// Set Player to null.
	    		// Get the Dealer from Server.getTargetDealer();
	        	if(loginType == "dealer") {
	        		
	        		dealerUser = Server.getTargetDealer(username);
	        		playerUser = null;
	        		
	        		
	        		// Print the list of the current online Dealers.
	        		//System.out.println(Server.getOnlineDealers());
	        	}
	        	
	        	// Set target player to client handler.
	        	// Set Dealer to null;
	        	// Get the player from Server.getTargetPlayer();
	        	if(loginType == "player") {
	        		
	        		playerUser = Server.getTargetPlayer(username);
	        		dealerUser = null;
	        		
	        		// Print the list of the current online Players.
	        		System.out.println(Server.getOnlinePlayers());
	        	}
	        	
	        	System.out.println("Login Successful -- " + loginType
	        			+ " Client #" + id + "\n");
	        	
	        	login.setStatus(Status.Success);
	        }
	        
	        // If neither player or dealer then the login is invalid
	        else {
	        	
	        	System.out.println("Login Failed -- Client #" + id + "\n");
	        	login.setStatus(Status.Failed);
	        }
	        
	        // Login should still contain the User Details.
	        // loginType should be
	        
	        
	        
	        login.setText(loginType);
		}	
		return login;		
	}
	
	// make a general send to client function
	// to send a message to the client whenever called from a function in the
	// main loop's switch.
	// Does not work, maybe not neaded?
	private void sendToClient(Message message) throws IOException {
		try {

			// Only brand new Message's with a Status of New will get handled.
			if(message.getStatus() == Status.New) {
				
				// If its a new message then handle that request from the Client
				handleMessage(message);
				
				// Send acknowledgment back to the client.
				objectOutputStream.writeObject(message);
			}	
	
		} catch (IOException e) {
			
			System.out.println("Something Borked! Closing socket!\n");
			clientSocket.close();
			
			e.printStackTrace();
		}

	}
	
	private void handleMessage(Message message) {
		//
		// Switch to handle all the various types of messages.
		// Will be controlled by enum Type
		// Data supplied for the servers action should be in the Message
		// text area.
		//
		// Build out the functions as needed and remember to update
		// the message before sending to the Client.
		//
		
		switch(message.getType()) {
			
			case ListGames:
				listGames(message);
				break;
				
			case ListPlayers:
				listPlayers(message);
				break;
				
			default:
				
				break;
		}
	}

	
	// Updates the Message's Status to Success and sets whatever text in text
	// field.
	private void updateMessageSuccess(Message message, String text) {
		
		// Update the Status of the Message.
		message.setStatus(Status.Success);
		
		// Update the text area with list of the games and details.
		message.setText(text);
	}
	

	// Sends a String back to the client with a list of all the games on the
	// Server with some details.
	private void listGames(Message message) {
		
		// Get list of games from Server.getGames()
		// Iterate through the list.
		// For Each Games concat a string: 
		//
		// Game+ID:TableStatus:DealerName:NumberOfPlayers\n
		// Game+ID:TableStatus:DealerName:NumberOfPlayers
		//
		
		String gameListString = null;
		List<Game> gameList = Server.getGames();
		
		Game lastGame = gameList.get(gameList.size() -1);
		
		for(Game g : gameList) {
			
			// If at last game on the list, print without newline character.
			if(g.equals(lastGame) ) {
				
				gameListString += "Game" + g.getID() + ":" 
							    + g.getTableStatus() + ":"
							    + g.getDealer().getDealerName() + ":"
							    + g.getTable().getPlayers().size();
			}
			
			// Else add the details to the string WITH newline characters.
			gameListString += "Game" + g.getID() + ":" 
							+ g.getTableStatus() + ":"
							+ g.getDealer().getDealerName() + ":"
							+ g.getTable().getPlayers().size() + "\n";
		}
		
		// Update the Status of the Message.
//		message.setStatus(Status.Success);
		
		// Update the text area with list of the games and details.
//		message.setText(gameListString);
		
		// Update the Status of the Message.
		// Update the text area with list of the games and details.
		updateMessageSuccess(message, gameListString);
	}
	
	
	// Lists the Players within a certain game.
	// The text area should contain the game's ID that wants to display its 
	// players.
	//
	// The message will update the text area in the Message with a string with 
	// that game's players.
	private void listPlayers(Message message) {
		
		// Get list of players.
		// Iterate through the list of players.
		// For each Player in the Game concat a string:
		//
		// PlayerName:Card,...,Card:Funds:CurrentBet\n
		// PlayerName:Card,...,Card:Funds:CurrentBet
		//
		// Where Card,...,Card is the players hand.
		
		String listOfPlayers = null;
		String gameID = message.getText();
		
		Game game = Server.getTargetGame(gameID);
		List<Player> players = game.getTable().getPlayers();
		
		Player lastPlayer = players.get(players.size() -1);
		
		for(Player p : players) {
			
			// If at the last player on list, print without newline character.
			if(p.equals(lastPlayer)) {
				listOfPlayers += p.getPlayerName() + ":"
							   + p.toStringPlayersHand() + ":"
							   + p.getPlayerFunds() + ":"
							   + p.currentBet;
			}
			
			// Else add the details to the string WITH newline characters.
			listOfPlayers += p.getPlayerName() + ":"
					   + p.toStringPlayersHand() + ":"
					   + p.getPlayerFunds() + ":"
					   + p.getBet() + "\n";
		}
		
		// Update the Status of the Message.
		// Update the text area with list of the games and details.
		updateMessageSuccess(message, listOfPlayers);
	}
}
