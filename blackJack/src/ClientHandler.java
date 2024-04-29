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
    private Game usersGame;

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
	    	System.out.println("end of server details anything beyond is past respondToClient()\n\n");
	        
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
				updateMessageFailed(login, login.getText());
				logMessage(login);
				
				clientSocket.close();
			}


			// This is the main loop of the program.
			// All actions from the GUI will go through the client and send
			// requests to the server here.
			//
			// On receipt of a ‘logout message’ should break out of the loop.
			// Then a status will be returned with ‘Success’, then the 
			// connection will be closed and the thread terminates.
			//
			// The Player or Dealer will be removed from the Server's 
			// onlinePlayers or onlineDealers List.

			// Keep reading for messages until we get a logout message.
			Message request = (Message) objectInputStream.readObject();
			
			while (!isLogginOut(request)) {
				
				// Respond back to Client's request with an updated message.
				respondToClient(request);

				// Get another message from the client
				// In the future this might change to a List of Message.
				request = (Message) objectInputStream.readObject();
			}

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
	private Boolean isLogginOut(Message msg) throws IOException {
		
		// If the message is of Type Logout and New return TRUE.
		if(msg.getType() == Type.Logout && msg.getStatus() == Status.New) {
			
			// Acknowledge logout Message
			msg.setStatus(Status.Success);
			
			// Username is supplied in the message. Log user out of the Server.
			logoutUser(msg.getText());
			
			// Print message to the terminal (make a log of what happened).
			logMessage(msg);
			
			// Send updated LOGOUT message back to the client
			objectOutputStream.writeObject(msg);

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
			return;
		}
		
		// Remove Dealer
		if(dealer != null && player == null ) {
			
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
	        	}
	        	
	        	// Set target player to client handler.
	        	// Set Dealer to null;
	        	// Get the player from Server.getTargetPlayer();
	        	if(loginType == "player") {
	        		
	        		playerUser = Server.getTargetPlayer(username);
	        		dealerUser = null;	
	        	}
	        	
	        	login.setStatus(Status.Success);
	        }
	        
	        // If neither player or dealer then the login is invalid
	        else {

	        	login.setStatus(Status.Failed);
	        }
	        
	        // Login should still contain the User Details.
	        login.setText(loginType);
		}
		// Print message to the terminal (make a log of what happened).
		logMessage(login);
		
		return login;		
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
	
	
	// A general send Message back to Client function.
	// A Message request is supplied by the Client and gets handled by the 
	// message handler. Then updates the message accordingly and sends the 
	// response back to the Client.
	private void respondToClient(Message message) throws IOException {
		try {

			// Only brand new Message's with a Status of New will get handled.
			if(message.getStatus() == Status.New) {
				
				// If its a new message then handle that request from the Client
				handleMessage(message);
			}
			
			// If its not a brand New Message than its an invalid request from 
			// the Client.
			else {

				updateMessageFailed(message, 
						"Invalid Request from the Client!");
			}
			
			// Print message to the terminal (make a log of what happened).
			logMessage(message);
			
			// Send acknowledgment back to the client.
			objectOutputStream.writeObject(message);
			
	
		} catch (IOException e) {
			
			System.out.println("Something Borked! Closing socket!\n");
			clientSocket.close();
			
			e.printStackTrace();
		}

	}
	
	
	// Message handler
	//
	// Switch to handle all the various types of messages.
	// Controlled by the Message's Type.
	// Message's request data is supplied in the Message text field. A servers
	// action should be tied to the Message Type and data associated in the text
	// area.
	//
	private void handleMessage(Message message) {

		// Build out the functions as needed and remember to update
		// the message before sending to the Client.
		//
		switch(message.getType()) {
			
			// Creates a new Player on the server. Details supplied in message.
			case Register:
				registerUser(message);
				break;
			
			// Sends a list of all Games on the server.
			case ListGames:
				listGames(message);
				break;
			
			// Sends a list of all online Players on the Server.
			case ListPlayersOnline:
				listPlayersOnline(message);
				break;
				
			// Sends a list of all online Dealers on the Server.
			case ListDealersOnline:
				listDealersOnline(message);
				break;
				
			// Sends a list of all Players in a Game by its Game ID.
			case ListPlayersInGame:
				listPlayersInGame(message);
				break;
				
			// Opens a new game on the Server and returns the new Game ID.
			case OpenGame:
				openGame(message);
				break;
				
			// Closes a Game on the Server using a Game ID from the Client.
			case CloseGame:
				closeGame(message);
				break;
			
			// Player/Dealer is added to game. Client supplies the Game's ID.
			case JoinGame:
				joinGame(message);
				break;
			
			// Player/Dealer is removed from game. Client supplies Games' ID.
			case LeaveGame:
				leaveGame(message);
				break;
				
			// Sends back the Game ID of which game the Player was put into.
			case QuickJoin:
				quickJoin(message);
				break;
				
			// A Player wants to see their funds. A Dealer the Casino's funds.
			case CheckFunds:
				checkFunds(message);
				break;
				
			// A player wants to add funds.
			case AddFunds:
				addFunds(message);
				break;		
			
			// All Players places their bets for a round of Blackjack. 
			// Starts a round of blackjack.
			case Bet:
				roundOfBlackjack(message);
				break;
			
				
				
			// DO NOTHING
			default:
				break;
		}
	}
	
	
	// When the dealer wants to start a game of Blackjack in a game. The client
	// will request that action by sending a request of Type StartRound.
	// This will start a round in the Server.
	private void roundOfBlackjack(Message message) {
		
		//String gameID = message.getText();
		//usersGame = Server.getTargetGame(gameID);
		
		// If not a valid game just do nothing.
		if(usersGame ==  null) {
			return;
		}
		
		
		// A while loop switch to control the game.
		switch(message.getStatus()) {
			default :
				break;
			
			
		}

		
		// A request a from the Client to place bets for Players.
		// this will update usersGame.
		// The string should come in as follows:
		// 
		// username:Bet\n
		// username:Bet\n
		// username:Bet
		//
		if(message.getType() == Type.Bet) {
			// bet does this
			usersGame.getTable().shuffleCards();		// Client handler does nothing
			usersGame.getBets(message.getText());// getBets takes all players bets.
			usersGame.getTable().dealCards();		// updates all players hands
		}
		
		// update gui here.. They get to see all new hands and bets.
		
		usersGame.checkBlackjack();
		usersGame.hitOrStand();
		usersGame.dealerTurn();
		usersGame.settleBets();
		usersGame.printFunds();	// might not be needed?
		usersGame.clearHands();
		
	}

	
	// The client supplies in a request with the users details. 
	// Server responds if the user has been registered or if the username
	// given is taken.
	//
	// username:password
	//
	// Return response to client with whatever Server.registerUser returns.
	private void registerUser(Message message){

		String status;
		
		try {
			
			status = Server.registerUser(message.getText());
						
			// If the user is already registered.
			if(status.equals("taken") ) {
				
				updateMessageFailed(message, "Username already taken!");
				return;
			}
			
			// If the user was registered.
			if(status.equals("registerd") ) {
				
				updateMessageSuccess(message, 
									 "You have registered to the Server!");
			}
			
			// If there is a wrong format supplied.
			updateMessageFailed(message, "Error");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		String gameListString = "";
		List<Game> gameList = Server.getGames();
		
		// If there are no game send back to the Client a Failed message.
		if(gameList == null) {
			updateMessageFailed(message, "There are no active Games!");
			return;
		}
		
		Game lastGame = gameList.get(gameList.size() -1);
		
		for(Game g : gameList) {
			
			String ID = g.getID();
			TableStatus tableStatus = g.getTableStatus();
			String dealerName = g.getDealer().getDealerName();
			int numPlayers = g.getTable().getPlayers().size();
			
			if(dealerName == null) {
				dealerName = "No Dealer";
			}
			
			gameListString += ID + ":" + tableStatus + ":" + dealerName + ":"
				    		  + numPlayers;
			
			// If at last game on the list, print without newline character.
			if(!g.equals(lastGame) ) {
				
				gameListString += "\n";
//								g.getID() + ":" 
//							    + g.getTableStatus() + ":"
//							    + g.getDealer().getDealerName() + ":"
//							    + g.getTable().getPlayers().size();
			}
			
			// Else add the details to the string WITH newline characters.
//			gameListString += g.getID() + ":" 
//							+ g.getTableStatus() + ":"
//							+ g.getDealer().getDealerName() + ":"
//							+ g.getTable().getPlayers().size() + ;
		}
		
		
		// Update the Status of the Message.
		// Update the text area with list of the games and details.
		updateMessageSuccess(message, gameListString);
	}
	
	//NEED TO FIX
	// PRINTS
	
	// Lists all Players online in the Server or nothing at all.
	private void listPlayersOnline(Message message) {

		String playersOnlineString = "";
		List<Player> playersOnline = Server.getOnlinePlayers();
		
		// If no Players online send a Success message back to the client.
		if(playersOnline == null) {
			updateMessageFailed(message, "There are no Players online!");
			return;
		}
		
		Player lastPlayer = playersOnline.get(playersOnline.size() -1);
	
		// Each player on the list gets printed.
		for(Player p : playersOnline) {
	
			// If at the last Player on the list print w/o the comma.
			if(p.equals(lastPlayer) ) {
				playersOnlineString += p.getPlayerName();

			}
			
			else {
				
				playersOnlineString += p.getPlayerName() + ",";
			}
		}
		
		// Update the Status of the Message.
		// Update the text area with list of the games and details.
		updateMessageSuccess(message, playersOnlineString);
	}
	
	
	// List all Dealers online in the Server.
	private void listDealersOnline(Message message) {

		String dealersOnlineString = null;
		List<Dealer> dealersOnline = Server.getOnlineDealers();
		
		// If no Dealers online send a Success message back to the client.
		if(dealersOnline == null || dealersOnline.size() == 0) {
			updateMessageFailed(message, "There are no Dealers online!");
			return;
		}
		
		Dealer lastDealer = dealersOnline.get(dealersOnline.size() -1);
		
		// Each player on the list gets printed.
		for(Dealer d : dealersOnline) {
			
			// If at the last Player on the list print w/o the comma.
			if(d.equals(lastDealer) ) {
				dealersOnlineString += d.getDealerName();
			}
			
			dealersOnlineString += d.getDealerName() + ",";
		}
		
		// Update the Status of the Message.
		// Update the text area with list of the games and details.
		updateMessageSuccess(message, dealersOnlineString);
	}
	
	
	// Needs to be renamed to what it really is.
	// Updates the Client with the state in a game of Blackjack.
	//
	// Lists the Players within a certain game.
	// The text area should contain the game's ID that wants to display its 
	// players.
	//
	// The message will update the text area in the Message with a string with 
	// that game's players.
	private void listPlayersInGame(Message message) {
		
		// Get list of players.
		// Iterate through the list of players.
		// For each Player in the Game concat a string:
		//
		// PlayerName:Card,...,Card:Funds:CurrentBet\n
		// PlayerName:Card,...,Card:Funds:CurrentBet
		//
		// Where Card,...,Card is the players hand.
		
		String listOfPlayers = "";
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
	

	// Opens/Creates a game and returns a Game ID.
	private void openGame(Message message) {

		Game newGame = new Game();
		Server.getGames().add(newGame);
		updateMessageSuccess(message, newGame.getID());
		
	}
	
	
	// Closes the game with the supplied Game ID and returns the same ID.
	private void closeGame(Message message) {

		Game gameToRemove = Server.getTargetGame(message.getText());
		
		// If we didn't find the game to remove.
		if(gameToRemove == null) {
			updateMessageFailed(message, "No Game with that ID!");
			return;
		}
		
		// Else remove the game.
		Server.getGames().remove(gameToRemove);
		updateMessageSuccess(message, message.getText());
	}		


	// The message will have the Game ID that the PlayerUser or DealerUser wants
	// to join.
	private void joinGame(Message message) {
		
		Game gameToJoin = Server.getTargetGame(message.getText());
		
		// If a Dealer wants to join a game as a DEALER
		if(dealerUser != null && playerUser == null) {
			
			gameToJoin.setDealer(dealerUser);
			usersGame = gameToJoin;
			return;
		}
		
		// If a Player wants to join a game
		if(playerUser != null && dealerUser == null) {
			gameToJoin.addPlayer(playerUser);
			usersGame = gameToJoin;
		}
	}

	
	// The message will have the Game ID. A playerUser or dealerUser will be 
	// removed from that game.
	private void leaveGame(Message message) {
		
		Game gameToLeave = Server.getTargetGame(message.getText());

		// If a Dealer wants to leave a game.
		if(dealerUser != null && playerUser == null) {
			gameToLeave.removeDealer(dealerUser);
			usersGame = null;
			return;
		}
		
		// If a Player wants to leave a game.
		if(playerUser != null && dealerUser == null) {
			gameToLeave.removePlayer(playerUser);
			usersGame = null;
		}
		
	}
	
	// User join the first Open Game's Table.
	// Nothing is supplied by the message.
	// Return the Game's ID that the player has joined.
	private void quickJoin(Message message) {
		
		String gameID = "";
		List<Game> games = Server.getGames();
		
		// If there are no game send back to the Client a Failed message.
		if(games == null || games.size() == 0) {
			updateMessageFailed(message, "There are no open Games!");
			return;
		}
		
		// For every game on the server.
		for(Game g : games) {
			
			// If the Table is Open, add the player to the game/table.
			if(g.getTableStatus() == TableStatus.Open) {
				
				gameID = g.getID();
				g.addPlayer(playerUser);
				usersGame = g;
				
				// Update the status of the Message as Success.
				// Send back the Client a Game's ID.
				updateMessageSuccess(message, gameID);
			}
		}
	}
	
	
	// A Player wants to see their funds. A Dealer the Casino's funds.
	// The message is of Type CheckFunds and has the Player/Dealer name.
	// 
	private void checkFunds(Message message) {
		
		String username = message.getText();
		Player player = Server.getTargetPlayer(username);
		Dealer dealer = Server.getTargetDealer(username);
		String funds;
		
		// Then a player is checking their funds.
		if(player != null && dealer == null) {
			
			funds = String.valueOf(player.getPlayerFunds());
			updateMessageSuccess(message, funds);
			return;
		}
		
		// Then its a dealer checking the Casino's funds.
		else if(dealer != null && player == null) {
			
			funds = String.valueOf(Server.getCasinoFunds());
			updateMessageSuccess(message, funds);
			return;
		}
		
		updateMessageFailed(message, "");
	}
	
	
	// A player wants to add funds to their account by giving a their name and
	// how much
	//
	// username:0000
	//
	private void addFunds(Message message) {
		
		String request[] = message.getText().split(":");
		
		if(request.length != 2) {
			updateMessageFailed(message, "");
			return;
		}
		
		Player player = Server.getTargetPlayer(request[0]);
		Double fundsToAdd = Double.valueOf(request[1]);
		
		if(player == null) {
			updateMessageFailed(message, "");
			return;
		}
		
		// Just add the funds to the player.
		player.funds += fundsToAdd;
		updateMessageSuccess(message, "Funds added!");
	}
}
