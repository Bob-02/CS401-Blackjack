import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUITest extends JFrame {
    private Game game;
    private JPanel playerPanel;
    private JPanel dealerPanel;

    public GUITest(Game game) {
        this.game = game;
        setTitle("Blackjack Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeGUI();
    }

    private void initializeGUI() {
        playerPanel = new JPanel(new FlowLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        add(playerPanel, BorderLayout.SOUTH);

        dealerPanel = new JPanel(new FlowLayout());
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        add(dealerPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");
        controlPanel.add(hitButton);
        controlPanel.add(standButton);
        add(controlPanel, BorderLayout.CENTER);

        hitButton.addActionListener(e -> playerHit());
        standButton.addActionListener(e -> playerStand());

        setVisible(true);
    }

    private void playerHit() {
        Player player = game.getTable().getPlayers().get(0);
        Card card = game.getTable().deal();
        player.getPlayerHand().add(card);
        updatePlayerPanel();
    }

    private void playerStand() {
        updateDealerPanel();
    }

    private void updatePlayerPanel() {
        playerPanel.removeAll();
        List<Card> hand = game.getTable().getPlayers().get(0).getPlayerHand();
        for (Card card : hand) {
            JLabel cardLabel = new JLabel(new ImageIcon(card.getImage().getImage()));
            playerPanel.add(cardLabel);
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    private void updateDealerPanel() {
        dealerPanel.removeAll();
        List<Card> hand = game.getDealer().getDealerHand();
        for (Card card : hand) {
            JLabel cardLabel = new JLabel(new ImageIcon(card.getImage().getImage()));
            dealerPanel.add(cardLabel);
        }
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    public static void main(String[] args) {
        Dealer dealer = new Dealer("Dealer", 10000);
        Player player = new Player("Player", 1000);
        List<Player> players = new ArrayList<>();
        players.add(player);
        Game game = new Game(dealer, players);
        new GUITest(game);
    }
}