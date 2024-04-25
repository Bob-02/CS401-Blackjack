import javax.swing.*;
import java.awt.*;

public class GUITest {
    private JFrame frame; 
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public GUITest() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Blackjack Casino Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        JPanel gamePanel = new JPanel();
        gamePanel.add(new JLabel("Welcome to the Blackjack Table!"));

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(gamePanel, "Game");

        // Action Listeners for buttons - ensure no call to sendLogin
        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Game"));
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Game"));

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUITest::new);
    }
}