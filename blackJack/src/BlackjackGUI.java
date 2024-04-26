
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class BlackjackGUI {
    private JFrame frame; // Main window frame for the GUI
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    // Constructor
    public BlackjackGUI() {
        initializeGUI(); // Initialize GUI components when the object is created
    }

	// Method to initialize the whole GUI
    private void initializeGUI() {
        frame = new JFrame("Blackjack Casino Game"); // Create the frame with a title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        frame.setSize(800, 600); // Set size of the frame
        
        // CardLayout allows switching between different panels (like login and game panels)
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout); // Panel that holds the different screens

        // Login Panel Setup
        JPanel loginPanel = new JPanel(new GridLayout(3, 2)); // Grid layout for orderly inputs
        JLabel usernameLabel = new JLabel("Username:"); // Label for username
        JTextField usernameField = new JTextField(); // Text field for username input
        JLabel passwordLabel = new JLabel("Password:"); // Label for password
        JPasswordField passwordField = new JPasswordField(); // Password field for secure password input
        JButton loginButton = new JButton("Login"); // Login button
        JButton registerButton = new JButton("Register"); // Register button

        // Add components to the login panel
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        // Main Game Panel
        JPanel gamePanel = new JPanel(); // Panel for the game interface
        gamePanel.add(new JLabel("Welcome to the Blackjack Table!")); // Welcome label

        // Add both panels to the card panel with identifiers
        cardPanel.add(loginPanel, "Login");
        cardPanel.add(gamePanel, "Game");

        // Action Listeners for buttons
        loginButton.addActionListener(e -> sendLogin(usernameField.getText(), new String(passwordField.getPassword())));
        registerButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Registration Module not implemented."));

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    // Handle login by sending a message to the server and processing the response.
    private void sendLogin(String username, String password) {
        try {
            Message loginMessage = new Message(Type.Login, Status.New, username + ":" + password);
            outputStream.writeObject(loginMessage);
            //outputStream.flush();
            Message response = (Message) inputStream.readObject();
            if (response.getStatus() == Status.Success) {
                JOptionPane.showMessageDialog(frame, "Login Successful");
                cardLayout.show(cardPanel, "Game");
            } else {
                JOptionPane.showMessageDialog(frame, "Login Failed");
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Error communicating with server");
            e.printStackTrace();
        }
    }

    // Main method to run the application.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlackjackGUI::new);
    }
}