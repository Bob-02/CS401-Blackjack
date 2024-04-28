import javax.swing.*;
import java.awt.*;

public class GUITest {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private String iconPath = "C:/Users/Zayyyy/Documents/CS401_BlackJack1/CS401-Blackjack/project pictures/icon.png"; // Path to the icon image


    public GUITest() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("BLACKJACK");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(32, 32, 32)); // A dark background for the card panel

        initializeLoginPanel();
        initializeGamePanel();
        initializeBlackjackTablePanel();

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    private void initializeLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(32, 32, 32)); // Dark background
        GridBagConstraints gbc = new GridBagConstraints();

        // Adding the icon to the side of the login panel
        ImageIcon icon = new ImageIcon(iconPath);
        JLabel iconLabel = new JLabel(icon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        loginPanel.add(iconLabel, gbc);

        // Resetting constraints for other components
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("WELCOME");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 20, 0);
        loginPanel.add(welcomeLabel, gbc);

        JLabel signInLabel = new JLabel("Sign In");
        signInLabel.setForeground(Color.WHITE);
        signInLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy++;
        loginPanel.add(signInLabel, gbc);

        JTextField usernameField = new JTextField(20);
        customizeComponent(usernameField);
        gbc.gridy++;
        loginPanel.add(usernameField, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        customizeComponent(passwordField);
        gbc.gridy++;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("LOGIN");
        customizeButton(loginButton);
        gbc.gridy++;
        loginPanel.add(loginButton, gbc);
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();  // Fetch the username entered
            String password = new String(passwordField.getPassword());  // Fetch the password entered
            String credentials = username + ":" + password;  // Combine username and password with a colon separator
            cardLayout.show(cardPanel, "Game");  // Move to the game panel
        });

        JButton registerButton = new JButton("SIGN UP NOW");
        customizeButton(registerButton);
        gbc.gridy++;
        loginPanel.add(registerButton, gbc);

        cardPanel.add(loginPanel, "Login");
        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Game"));
        registerButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Registration not implemented"));
    }

    private void initializeGamePanel() {
        // Custom game panel - This will be your actual game interface
        JPanel gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(new Color(0, 102, 0)); // Set to a green resembling a blackjack table

        GridBagConstraints gbcGame = new GridBagConstraints();
        gbcGame.gridwidth = GridBagConstraints.REMAINDER;
        gbcGame.anchor = GridBagConstraints.CENTER;
        gbcGame.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeToBlackjack = new JLabel("Welcome to BLACKJACK!", SwingConstants.CENTER);
        welcomeToBlackjack.setForeground(Color.WHITE); // White text
        welcomeToBlackjack.setFont(new Font("Arial", Font.BOLD, 40)); // Larger font size for the title
        gbcGame.insets = new Insets(20, 0, 20, 0);
        gamePanel.add(welcomeToBlackjack, gbcGame);

        JButton playButton = new JButton("PLAY");
        customizeButton(playButton);
        playButton.addActionListener(e -> cardLayout.show(cardPanel, "BlackjackTable"));
        gbcGame.insets = new Insets(10, 0, 10, 0);
        gamePanel.add(playButton, gbcGame);

        JButton exitButton = new JButton("EXIT");
        customizeButton(exitButton);
        exitButton.addActionListener(e -> frame.dispose());
        gamePanel.add(exitButton, gbcGame);

        JLabel footerLabel = new JLabel("This game is brought to you by Group 5", SwingConstants.CENTER);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbcGame.insets = new Insets(40, 0, 0, 0);
        gamePanel.add(footerLabel, gbcGame);

        cardPanel.add(gamePanel, "Game");
    }

    private void initializeBlackjackTablePanel() {
        JPanel blackjackTablePanel = new JPanel(new BorderLayout());
        blackjackTablePanel.setBackground(new Color(0, 102, 0)); // Green table background

        // Dealer's area
        JLabel dealerLabel = new JLabel("Dealer's Area", SwingConstants.CENTER);
        dealerLabel.setForeground(Color.WHITE);
        dealerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        blackjackTablePanel.add(dealerLabel, BorderLayout.NORTH);

        // Player's area
        JPanel playerArea = new JPanel();
        playerArea.setBackground(new Color(0, 150, 0)); // Slightly different green for contrast
        playerArea.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Card placeholders for players
        JLabel card1 = new JLabel(new ImageIcon("path_to_card_image"));
        JLabel card2 = new JLabel(new ImageIcon("path_to_card_image"));
        playerArea.add(card1);
        playerArea.add(card2);

        // Player actions
        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");
        JButton doubleDownButton = new JButton("Double Down");
        playerArea.add(hitButton);
        playerArea.add(standButton);
        playerArea.add(doubleDownButton);

        customizeButton(hitButton);
        customizeButton(standButton);
        customizeButton(doubleDownButton);

        blackjackTablePanel.add(playerArea, BorderLayout.CENTER);

        // Status area for messages like "You Win!" or "You Lose!"
        JLabel statusLabel = new JLabel("Place Your Bets", SwingConstants.CENTER);
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        blackjackTablePanel.add(statusLabel, BorderLayout.SOUTH);

        cardPanel.add(blackjackTablePanel, "BlackjackTable");
    }

    private void customizeComponent(JComponent component) {
        component.setPreferredSize(new Dimension(150, 30));
        component.setFont(new Font("Arial", Font.PLAIN, 14));
        component.setForeground(Color.WHITE);
        component.setBackground(new Color(64, 64, 64));
        if (component instanceof JTextField) {
            ((JTextField) component).setCaretColor(Color.WHITE);
        }
    }

    private void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUITest::new);
    }
}