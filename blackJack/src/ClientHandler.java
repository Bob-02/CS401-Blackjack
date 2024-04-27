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
    
    // make a SINGLE generic type to hold either Player or Dealer
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
	    	System.out.println(Server.getOnlineDealers());
	    	System.out.println(Server.getValidPlayers());
	    	System.out.println(Server.getOnlinePlayers());
	        
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
				
				// Explicitly send message back to the Client b/c sendToClient()
				// does not handle logins.
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


			// Keep reading for messages until we get a logout message.
			Message current = (Message) objectInputStream.readObject();

			// This is the main loop of the program.
			// All actions from the GUI will go through the client and send
			// requests to the server here.
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
			//
			// The Player or Dealer will be removed from the Server's 
			// onlinePlayers or onlineDealrs.

			// Send updated LOGOUT message back to the client
			objectOutputStream.writeObject(current);
			
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


	public int getClientID() {
		return id;
	}
	
	private Boolean isNewLogin(Message login) {
		
		// The message is valid if of Type Login and has a Status of New.
		if(login.getType() == Type.Login && login.getStatus() == Status.New) {
			return true;
		}
		
		return false;
	}
	
	
	// Checks the message to see if a Client is requesting to logout and then
	// updates the message.
	// Used to break out of the ClientHandler.
	private Boolean isLogginOut(Message msg) {
		
		// If the message is of Type Logout and New return TRUE.
		if(msg.getType() == Type.Logout && msg.getStatus() == Status.New) {
			
			// Acknowledge logout Message
			msg.setStatus(Status.Success);
			
			// Username is supplied in the message.
			logoutUser(msg.getText());
			
			// Print message to the terminal (make a log of what happened).
			logMessage(msg);
			
			return true;
		}		

		// Else this message is not a logout message. Proceed to process the
		// message accordingly.
		return false;
	}
	
	
	// A Dealer or Player name is given and is removed from Server if valid.
	private void logoutUser(String user) {
		
		Player player = Server.getTargetPlayer(user);
		Dealer dealer = Server.getTargetDealer(user);
		
		// Remove Player
		if(player != null && dealer == null) {
			
			Server.getOnlinePlayers().remove(player);
		}
		
		// Remove Dealer
		else if(dealer != null && player == null ) {
			
			Server.getOnlineDealers().remove(dealer);
		}		
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
	        	
	        	System.out.println("Login Successful -- <" + loginType
	        			+ "> Client #" + id + "\n");
	        	
	        	login.setStatus(Status.Success);
	        }
	        
	        // If neither player or dealer then the login is invalid
	        else {
	        	
	        	System.out.println("Login Failed -- Client #" + id + "\n");
	        	login.setStatus(Status.Failed);
	        }
	        
	        // Login should still contain the User Details.
	        login.setText(loginType);
		}
		// Print message to the terminal (make a log of what happened).
		logMessage(login);
		
		return login;		
	}
	
	// A general send Message back to Client function.
	// A Message request is supplied by the Client and gets handled by the 
	// message handler. Then updates the message accordingly and sends the 
	// response back to the Client.
	private void sendToClient(Message message) throws IOException {
		try {

			// Only brand new Message's with a Status of New will get handled.
			if(message.getStatus() == Status.New) {
				
				// If its a new message then handle that request from the Client
				handleMessage(message);
				
				// Send acknowledgment back to the client.
				objectOutputStream.writeObject(message);
				
				// Print message to the terminal (make a log of what happened).
				logMessage(message);
			}
			
			// If its not a brand new message than its an invalid request from 
			// the Client.
			else {
				System.out.println("Invalid Request from the Client!");
			}
	
		} catch (IOException e) {
			
			System.out.println("Something Borked! Closing socket!\n");
			clientSocket.close();
			
			e.printStackTrace();
		}

	}
	

	
	
	// Message handler
	private void handleMessage(Message message) {

		// Switch to handle all the various types of messages.
		// Controlled by the Message's Type.
		// Message request data is supplied in the Message text field. A servers
		// action should be the Message Type and data associated in the text
		// area.
		//
		// Build out the functions as needed and remember to update
		// the message before sending to the Client.
		//
		switch(message.getType()) {
			
			// Sends a list of all Games on the server.
			case ListGames:
				listGames(message);
				break;
				
			// Sends a list of all Players in a Game by its Game ID.
			case ListPlayers:
				listPlayers(message);
				break;
				
			// Sends back the Game ID of which game the Player was put into.
			case QuickJoin:
				quickJoin(message);
				break;
				
			default:
				// DO NOTHING
				break;
		}
	}	
	
	
	// Prints a log to the terminal saying what was sent to the Client. 
	private void logMessage(Message message) {
		
		Type request = message.getType();
		Status status = message.getStatus();
		String data = message.getText();
		String timeStamp = new Date().getCurrentDate();
		int id = getClientID();
		
		
		// Client# id <type>[status]: timeStamp 
		// data
		System.out.println("Client# " + id + " <" + request + ">[" + status 
						   + "]:" + timeStamp + "\n" + data);
	}


	// Updates the Message's Status to Success and sets whatever text in text
	// field.
	private void updateMessageSuccess(Message message, String text) {
		
		// Update the Status of the Message.
		message.setStatus(Status.Success);
		
		// Update the text area.
		message.setText(text);
	}
	
	
	// Updates the Message's Status to Success and sets whatever text in text
	// field.
	private void updateMessageFailed(Message message, String text) {
		
		// Update the Status of the Message.
		message.setStatus(Status.Failed);
		
		// Update the text area.
		message.setText(text);
	}
	

	// Sends a String back to the client with a list of all the games on the
	// Server with some details.
	private void listGames(Message message) {
		
		// Get list of games from Server.getGames()
		// Iterate through the list.
		// For Each Games concat a string: 
		//
		// GameID:TableStatus:DealerName:NumberOfPlayers\n
		// GameID:TableStatus:DealerName:NumberOfPlayers
		//
		
		String gameListString = null;
		List<Game> gameList = Server.getGames();
		
		// If there are no game send back to the Client a Failed message.
		if(gameList == null) {
			updateMessageFailed(message, "There are no active Games!");
			return;
		}
		
		Game lastGame = gameList.get(gameList.size() -1);
		
		for(Game g : gameList) {
			
			// If at last game on the list, print without newline character.
			if(g.equals(lastGame) ) {
				
				gameListString += g.getID() + ":" 
							    + g.getTableStatus() + ":"
							    + g.getDealer().getDealerName() + ":"
							    + g.getTable().getPlayers().size();
			}
			
			// Else add the details to the string WITH newline characters.
			gameListString += g.getID() + ":" 
							+ g.getTableStatus() + ":"
							+ g.getDealer().getDealerName() + ":"
							+ g.getTable().getPlayers().size() + "\n";
		}
		
		
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
		
		// If there is no game by supplied ID
		if(game == null) {
			updateMessageFailed(message, "Game Not Found!");
			return;
		}
		
		List<Player> players = game.getTable().getPlayers();
		
		Player lastPlayer = players.get(players.size() -1);
		
		for(Player p : players) {
			
			// If at the last player on list, print without newline character.
			if(p.equals(lastPlayer)) {
				listOfPlayers += p.getPlayerName() + ":"
							   + p.toStringPlayersHand() + ":"
							   + p.getPlayerFunds() + ":"
							   + p.getBet();
			}
			
			// Else add the details to the string WITH newline characters.
			listOfPlayers += p.getPlayerName() + ":"
					   + p.toStringPlayersHand() + ":"
					   + p.getPlayerFunds() + ":"
					   + p.getBet() + "\n";
		}
		
		// Update the Status of the Message.
		// Update the text area with list of the players and details.
		updateMessageSuccess(message, listOfPlayers);
	}
	
	
	// User join the first Open Game's Table.
	// Nothing is supplied by the message.
	// Return the Game's ID that the player has joined.
	private void quickJoin(Message message) {
		
		String gameID = null;
		List<Game> games = Server.getGames();
		
		// If there are no game send back to the Client a Failed message.
		if(games == null) {
			updateMessageFailed(message, "There are no open Games!");
			return;
		}
		
		// For every game on the server.
		for(Game g : games) {
			
			// If the Table is Open, add the player to the game/table.
			if(g.getTableStatus() == TableStatus.Open) {
				
				gameID = g.getID();
				g.addPlayer(playerUser);
				
				// Update the status of the Message as Success.
				// Send back the Client a Game's ID.
				updateMessageSuccess(message, gameID);
			}
		}
		
		// If there are no Open Games, update the message as Failed.
		updateMessageFailed(message, "No open Games!");
	}
}
