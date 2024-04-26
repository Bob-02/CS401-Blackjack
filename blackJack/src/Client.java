import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";   // default server address
    private static final int DEFAULT_SERVER_PORT = 7777;               // default server port
    
    	// Output stream socket.
 		private static ObjectOutputStream objectOutputStream;
 		//Input Stream socket
		private static ObjectInputStream objectInputStream;
    
	public static void main(String[] args) throws Exception {
//		BlackjackGUI blackjackGUI = new BlackjackGUI();
		
		int port = DEFAULT_SERVER_PORT;
		String host = DEFAULT_SERVER_ADDRESS;

		// Connect to the ServerSocket at host:port
		Socket socket = new Socket(host, port);
		System.out.println("Connected to " + host + ":" + port);
		
		// Output stream socket
		OutputStream outputStream = socket.getOutputStream();

		//Input Stream socket
		InputStream inputStream = socket.getInputStream();
		
		// Create object output stream from the output stream to send an object through it
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		Message message = new Message(Type.Login,Status.New,"luser1:letmein");
		System.out.println("Sending Message Objects");
		objectOutputStream.writeObject(message);
		
		//Create object input stream to get messages from the server 
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		// Read the object from the input stream and cast it to Message
		Message receivedMessage = (Message) objectInputStream.readObject();
		
		System.out.println(receivedMessage.getType()+" "+receivedMessage.getStatus());
		
		// List of Message objects
//		List<Message> messages = new ArrayList<>();
		
//		messages.add(new Message(Type.Login, Status.New, "luser1:letmein"));
		
		playBlackJack(new Message(Type.JoinTable,Status.New,"1user1"));
		//System.out.println("Closing socket");
		//socket.close();
		
	}

	private static void playBlackJack(Message message) {
		sendMessage(message);
		
	}

	private static void sendMessage(Message message) {
		try {
            // Write the message object to the output stream
            objectOutputStream.writeObject(message);
            
            // Flush the output stream to ensure the message is sent immediately
            objectOutputStream.flush();
            
            // Read the response from the server
            Message receivedMessage = (Message) objectInputStream.readObject();
            
            // Process the received message as needed
            System.out.println("Received: " + receivedMessage.getType() + " " + receivedMessage.getStatus());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
		
	}


}
