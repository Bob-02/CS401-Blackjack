package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientConsole {
	public String SERVER_ADDRESS = "10.0.0.43";
	public int SERVER_PORT = 7777;
	public static ObjectOutputStream objectOutputStream;
	public static ObjectInputStream objectInputStream;
	public OutputStream outputStream;
	public InputStream inputStream;
	Scanner scan;
	
	protected void startgamePlay() throws IOException {
		scan = new Scanner(System.in);
		Socket socket = createConnection();
		initializeStreams(socket);
		System.out.println("Welcome to BlackJack Console Ui:\n 1.SignIn \n 2.Signup");
		int choice = scan.nextInt();
		switch(choice) {
		case 1:
			signIn();
			return;
		case 2:
			signUp();
			return;
		}
	}

	private void signIn() {
		String credentials = "";
		System.out.println("Enter your Username: ");
		credentials = credentials + scan.next();
		System.out.println("Enter your Password: ");
		credentials = credentials+":"+scan.next();
		System.out.println("Credentials sent to Server = "+ credentials);
		sendMessage(new Message(Type.Login,Status.New,credentials));
		System.out.println(receiveMessage());
		if (receiveMessage().status.toString()=="Success") {
			// Go to Next Screen
			System.out.println("Success, Going to Another Screen");
		}
	}

	private void signUp() {
		String signUpCredentials = "";
		System.out.println("USE SIGNUP PAGE\nEnter your Username: ");
		signUpCredentials = signUpCredentials + scan.next();
		System.out.println("Enter your Password: ");
		signUpCredentials = signUpCredentials+":"+scan.next();
		System.out.println("Credentials sent to Server = "+ signUpCredentials);
		sendMessage(new Message(Type.Register,Status.New,signUpCredentials));
		System.out.println(receiveMessage());
		if (receiveMessage().status.toString()=="Success") {
			// Go to Next Screen
		}
		
	}

	Socket createConnection() {

		Socket socket = null;
		try {
			socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connected to server.");

		return socket;
	}

	void initializeStreams(Socket socket) throws IOException {
		// Initialize streams
		outputStream = socket.getOutputStream();
		inputStream = socket.getInputStream();
		objectOutputStream = new ObjectOutputStream(outputStream);
		objectInputStream = new ObjectInputStream(inputStream);
		System.out.println("Output Input Streams Initialized");
	}

	public void sendMessage(Message message) {
		try {
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
		} catch (IOException e) {
			System.err.println("Error sending message: " + e.getMessage());
		}
	}

	public Message receiveMessage() {
		try {
			return (Message) objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error receiving message: " + e.getMessage());
			return null;
		}
	}

}