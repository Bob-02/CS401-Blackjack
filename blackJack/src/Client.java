import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 7777;

    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    public static void main(String[] args) {
    	
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to server.");

            // Initialize streams
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectInputStream = new ObjectInputStream(inputStream);
            
            BlackjackGUI gui = new BlackjackGUI();
           
            // Send login message
            String credentials = gui.getLoginCredentials();
            sendMessage(new Message(Type.Login, Status.New, "dealer3:letmein"));
//            sendMessage(new Message(Type.Login, Status.New, credentials));
            System.out.println("Sent Credential-" + credentials);
            //gui.setLoginListener((username, password) -> sendLoginDetails(username, password)); // Set login listener
           

            // Receive and process response
            Message response = receiveMessage();
            System.out.println("Received: " + response.getType() + " " + response.getStatus());

            // Sending additional messages
            sendMessage(new Message(Type.AddFunds, Status.New, "1user1"));
            response = receiveMessage();
            System.out.println("Received: " + response.getType() + " " + response.getStatus() + " " + response.getText());
            
            sendMessage(new Message(Type.JoinGame,Status.New, "1user1"));
            response = receiveMessage();
            System.out.println("Received: " + response.getType() + " " + response.getStatus() + " " + response.getText());

            // Close the socket
            socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
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