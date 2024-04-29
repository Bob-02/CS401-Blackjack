import javax.swing.*;

public class GUITest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Test Frame");
            JPanel testPanel = new JPanel();
            ImageIcon icon = new ImageIcon("Cards/A-H.png"); // Example path that works
            JLabel label = new JLabel(icon);
            testPanel.add(label);
            testFrame.add(testPanel);
            testFrame.pack();
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setVisible(true);
        });
    }
}