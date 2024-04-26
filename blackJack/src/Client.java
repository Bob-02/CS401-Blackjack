import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    
	public static void main(String[] args) throws Exception {
		BlackjackGUI blackjackGUI = new BlackjackGUI();
		
		int port = DEFAULT_SERVER_PORT;
		String host = DEFAULT_SERVER_ADDRESS;

		// Connect to the ServerSocket at host:port
		Socket socket = new Socket(host, port);
		System.out.println("Connected to " + host + ":" + port);
		
		// Output stream socket.
		OutputStream outputStream = socket.getOutputStream();

		// Create object output stream from the output stream to send an object through it
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

		// List of Message objects
		List<Message> messages = new ArrayList<>();
		messages.add(new Message(Type.Login, Status.New, "This is a test message!"));

		System.out.println("Sending Message Objects");
		objectOutputStream.writeObject(messages);

		System.out.println("Closing socket");
		socket.close();
		
		connectToMultithreadedServer();
	}

	private static void connectToMultithreadedServer() {
		// TODO Auto-generated method stub
		// establish a connection by providing host and port
				// number
				try (Socket socket = new Socket(DEFAULT_SERVER_ADDRESS,DEFAULT_SERVER_PORT)) {
					
					// writing to server
					PrintWriter out = new PrintWriter(
						socket.getOutputStream(), true);

					// reading from server
					BufferedReader in
						= new BufferedReader(new InputStreamReader(
							socket.getInputStream()));

					// object of scanner class
					Scanner sc = new Scanner(System.in);
					String line = null;

					while (!"exit".equalsIgnoreCase(line)) {
						
						// reading from user
						line = sc.nextLine();

						// sending the user input to server
						out.println(line);
						out.flush();

						// displaying server reply
						System.out.println("Server replied "
										+ in.readLine());
					}
					
					// closing the scanner object
					sc.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
