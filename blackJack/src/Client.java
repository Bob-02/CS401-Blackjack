import java.io.*;
import java.net.Socket;

public class Client {
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 7777;

	private static ObjectOutputStream objectOutputStream;
	private static ObjectInputStream objectInputStream;
	// private static final int TIMEOUT_MS = 25000; // 25 seconds
	private static BlackjackGUI gui;
	private String messageTogui;
	private static Client client;
	private static int clickIndex = 0; // To keep track of the index of the next simulated button click

	public static void main(String[] args) {
		gui = new BlackjackGUI(client);
		client = new Client();

		try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
			System.out.println("Connected to server.");

			// Set socket timeout
			// socket.setSoTimeout(TIMEOUT_MS);

			// Initialize streams
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectInputStream = new ObjectInputStream(inputStream);

			// Send login message
			String credentials = gui.waitForLogin();
			System.out.println("Credentials about to be sent is : " + credentials);
			sendMessage(new Message(Type.Login, Status.New, credentials));

//          sendMessage(new Message(Type.Login, Status.New, credentials));
			System.out.println("Sent Credential-" + credentials);
			// gui.setLoginListener((username, password) -> sendLoginDetails(username,
			// password)); // Set login listener

			// Receive and process response
			Message response = receiveMessage();
			if (response == null) {
				System.err.println("Error: Timed out while waiting for response.");
				return; // Exit the program or handle the error accordingly
			}
			System.out.println("Received: " + response.getType() + " " + response.getStatus());

			if (response != null && response.getStatus() == Status.Success) {
				System.out.println("Login successful.\n");

				// Main loop to send and receive messages
				while (true) {
					// Send a message
					createAndSendMessage();

					// Receive a message
					Message receivedMessage = receiveMessage();
					if (receivedMessage != null) {
						System.out.println("Received: " +receivedMessage.getType()+", "+receivedMessage.getStatus()+", " +receivedMessage.getText()+"\n");
						client.setMessageTogui(receivedMessage.status.toString());

						// Check if it's a logout message
						if (receivedMessage.getType() == Type.Logout && receivedMessage.getStatus() == Status.Success) {
							System.out.println("Received logout message. Exiting.");
							break;
						}
					}
				}
			} else {
				System.out.println("Login failed.");
			}

//            // Sending additional messages
//            sendMessage(new Message(Type.AddFunds, Status.New, "1user1"));
//            response = receiveMessage();
//            System.out.println("Received: " + response.getType() + " " + response.getStatus() + " " + response.getText());
//            
//            sendMessage(new Message(Type.JoinGame,Status.New, "1user1"));
//            response = receiveMessage();
//            System.out.println("Received: " + response.getType() + " " + response.getStatus() + " " + response.getText());

			// Close the socket
			socket.close();
			System.out.println("Disconnected from server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
////CreateandSendMessage Playground
//    private static void createAndSendMessage() {
//		// TODO Auto-generated method stub
//    	// Create and send the message
//        sendMessage(new Message(Type.AddFunds, Status.New, "AddFunds"));
//        sendMessage(new Message(Type.CheckFundHistory, Status.New, "CheckFundHistory"));
//    	sendMessage(new Message(Type.OpenGame, Status.New, "luser1"));
//    	sendMessage(new Message(Type.OpenGame, Status.New, "1user2"));
////    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
////    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
////    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
////        sendMessage(new Message(Type.ListGames, Status.New, "ListGames"));
////        sendMessage(new Message(Type.ListPlayersOnline, Status.New, "ListPlayersOnline"));
////        sendMessage(new Message(Type.ListDealersOnline, Status.New, "ListDealersOnline"));
////        sendMessage(new Message(Type.ListPlayersInGame, Status.New, "1"));
////        sendMessage(new Message(Type.QuickJoin, Status.New, "dealer3"));
//        sendMessage(new Message(Type.LeaveGame, Status.New, "1"));
//        sendMessage(new Message(Type.CloseGame, Status.New, "1"));
//        //sendMessage(new Message(Type.CashOut, Status.New, "CashOut"));
//        sendMessage(new Message(Type.Logout, Status.New, "luser1"));
//	}

	private static void createAndSendMessage() {
		// Get button clicks from GUI
//        String buttonClick = gui.buttonClicks();
		String buttonClick = getNextButtonClick();
		System.out.println("This Button Is Clicked --> " + buttonClick);

		// Process button click and send corresponding message to the server
		switch (buttonClick) {
		case "registerationButtonClicked":
			sendMessage(new Message(Type.Register,Status.New,"luser9:letmein"));
			break;
			
		case "anotherPlayerLogin":
			sendMessage(new Message(Type.Login, Status.New, "luser2:letmein"));
			break;
		case "manuallyAddDealer":
			sendMessage(new Message(Type.Login,Status.New,"dealer1:letmein"));
			break;
			
		case "opengameButtonClicked":
			sendMessage(new Message(Type.OpenGame, Status.New, ""));
			break;
			
		case "checkFundButtonClicked":
			sendMessage(new Message(Type.CheckFunds,Status.New,"luser2"));
			break;
			
		case "playButtonClicked":
			sendMessage(new Message(Type.OpenGame, Status.New, ""));
//                Message receivedMessage = receiveMessage();
//                String msgtogui = receivedMessage.getStatus().toString();
//                client.setMessageTogui(msgtogui);
//                System.out.println("Message to GUI SET ="+ msgtogui);
			break;
			
		case "viewGamesButtonClicked":
			sendMessage(new Message(Type.ListGames, Status.New, "ListGames"));
			break;
			
		case "viewPlayersButtonClicked":
			sendMessage(new Message(Type.ListPlayersOnline, Status.New, "ListPlayersOnline"));
			break;
			
		case "placeBetButtonClicked":
			sendMessage(new Message(Type.Bet,Status.New,"luser1:500\n1user2:500"));
			break;
			
		case "hitButtonClicked":
			sendMessage(new Message(Type.HitOrStand,Status.New,"luser1:h"));
			break;
			
		case "standButtonClicked":
			sendMessage(new Message(Type.HitOrStand,Status.New,"1user1:s"));
			break;
			
		case "addfundsButtonClicked":
			sendMessage(new Message(Type.AddFunds, Status.New, "luser1:1251"));
			System.out.println("Funds Adding functionality Pass");
			break;
		
		case "joingameButtonClicked":
			sendMessage(new Message(Type.JoinGame, Status.New, "1"));
			
		case "listplayersOnlineButtonClicked":
			sendMessage(new Message(Type.ListPlayersOnline, Status.New, "ListPlayersOnline"));
			System.out.println("List of Players functionality Pass");
			break;
			
		case "listdealerOnlineButtonClicked":
			sendMessage(new Message(Type.ListDealersOnline, Status.New, "ListDealersOnline"));
			break;
			
		case "listplayersInGameButtonClicked":
			sendMessage(new Message(Type.ListPlayersInGame, Status.New, "1"));
			break;
			
		case "quickJoinButtonClicked":
			sendMessage(new Message(Type.QuickJoin, Status.New, ""));
			break;
			
		case "logoutButtonClicked":
			sendMessage(new Message(Type.Logout, Status.New, "luser1"));
			break;
		

		default:
			// Default action if no button is clicked
			sendMessage(new Message(Type.Default, Status.New, "NoAction"));
			break;
		}
	}

	public String getMessageTogui() {
		return messageTogui;
	}

	public void setMessageTogui(String messageTogui) {
		this.messageTogui = messageTogui;
		//System.out.println("This message is sent to Gui:" + messageTogui);
	}

	private static String getNextButtonClick() {
		// List of simulated button clicks
		String[] buttonClicks = 
			{ 
				"registerationButtonClicked",
				"anotherPlayerLogin",
				"opengameButtonClicked",
				"manuallyAddDealer",
				"viewGamesButtonClicked",
				"addfundsButtonClicked",
				"listplayersOnlineButtonClicked",
				"listdealerOnlineButtonClicked",
				"joingameButtonClicked",
				"listplayersInGameButtonClicked",
				"checkFundButtonClicked",
				"placeBetButtonClicked",
				"hitButtonClicked",
				"listplayersInGameButtonClicked",
				"quickJoinButtonClicked",
				"viewGamesButtonClicked", 
				"viewPlayersButtonClicked",
				"logoutButtonClicked" 
				
			};

		// Return the next button click and update the index for the next call
		String nextButtonClick = buttonClicks[clickIndex];
		clickIndex = (clickIndex + 1) % buttonClicks.length;
		return nextButtonClick;
	}

	public static void sendMessage(Message message) {
		try {
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
		} catch (IOException e) {
			System.err.println("Error sending message: " + e.getMessage());
		}
	}

	private static Message receiveMessage() {
		try {
			return (Message) objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error receiving message: " + e.getMessage());
			return null;
		}
	}

	public String getButtonClicksFromGui() {
		gui.waitForButtonClick();
		return gui.buttonClicks();
	}
}