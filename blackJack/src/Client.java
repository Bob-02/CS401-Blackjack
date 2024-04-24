import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in); // System.in is a standard input stream.
		System.out.print("Enter the port number to connect to: <7777>");
		int port = sc.nextInt();
		System.out.print("Enter the host address to connect to: <localhost> ");
		String host = sc.next();

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

		System.out.print("Enter message info. <enter> to quit\n");
		String msg = sc.next();
		messages.add(new Message(null, null, msg));

		System.out.println("Sending Message Objects");
		objectOutputStream.writeObject(messages);

		System.out.println("Closing socket");
		socket.close();
	}
}
