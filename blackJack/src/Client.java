import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 7777;

    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;
    private static final int TIMEOUT_MS = 25000; // 25 seconds

    public static void main(String[] args) {
    	
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to server.");
            
            // Set socket timeout
            socket.setSoTimeout(TIMEOUT_MS);

            // Initialize streams
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectInputStream = new ObjectInputStream(inputStream);
            
            BlackjackGUI gui = new BlackjackGUI();
           
            // Send login message
            String credentials = gui.getLoginCredentials();
            while (credentials != null) {
            	sendMessage(new Message(Type.Login, Status.New, credentials));	
            
//          sendMessage(new Message(Type.Login, Status.New, credentials));
            System.out.println("Sent Credential-" + credentials);
            //gui.setLoginListener((username, password) -> sendLoginDetails(username, password)); // Set login listener
            }

            // Receive and process response
            Message response = receiveMessage();
            if (response == null) {
                System.err.println("Error: Timed out while waiting for response.");
                return; // Exit the program or handle the error accordingly
            }
            System.out.println("Received: " + response.getType() + " " + response.getStatus());

            if (response != null && response.getStatus() == Status.Success) {
                System.out.println("Login successful.");

                // Main loop to send and receive messages
                while (true) {
                    // Send a message
                    createAndSendMessage();

                    // Receive a message
                    Message receivedMessage = receiveMessage();
                    if (receivedMessage != null) {
                        System.out.println("Received: " + receivedMessage.getText());

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

    private static void createAndSendMessage() {
		// TODO Auto-generated method stub
    	// Create and send the message
        sendMessage(new Message(Type.AddFunds, Status.New, "AddFunds"));
        sendMessage(new Message(Type.CheckFundHistory, Status.New, "CheckFundHistory"));
    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
//    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
//    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
//    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
//    	sendMessage(new Message(Type.OpenGame, Status.New, ""));
//        sendMessage(new Message(Type.ListGames, Status.New, "ListGames"));
//        sendMessage(new Message(Type.ListPlayersOnline, Status.New, "ListPlayersOnline"));
//        sendMessage(new Message(Type.ListDealersOnline, Status.New, "ListDealersOnline"));
//        sendMessage(new Message(Type.ListPlayersInGame, Status.New, "1"));
        sendMessage(new Message(Type.QuickJoin, Status.New, "dealer3"));
        sendMessage(new Message(Type.LeaveGame, Status.New, "1"));
        sendMessage(new Message(Type.CloseGame, Status.New, "1"));
        //sendMessage(new Message(Type.CashOut, Status.New, "CashOut"));
        sendMessage(new Message(Type.Logout, Status.New, "dealer3"));
	}

	private static void sendMessage(Message message) {
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
}