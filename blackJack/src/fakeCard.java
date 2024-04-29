
import javax.swing.ImageIcon;

public class fakeCard {
    private Suit suit;
    private Rank rank;
    private int cardValue;
    private ImageIcon image;

    // Constructor for Card that initializes all necessary properties
    public fakeCard(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        setCardValue();
        loadCardImage();
    }

    // Determine the card's value based on its rank, useful for games like Blackjack
    private void setCardValue() {
        switch (this.rank) {
            case Two: cardValue = 2; break;
            case Three: cardValue = 3; break;
            case Four: cardValue = 4; break;
            case Five: cardValue = 5; break;
            case Six: cardValue = 6; break;
            case Seven: cardValue = 7; break;
            case Eight: cardValue = 8; break;
            case Nine: cardValue = 9; break;
            case Ten: 
            case Jack: 
            case Queen: 
            case King: cardValue = 10; break;
            case Ace: cardValue = 11; break; // Ace typically values at 11, but can be 1; handling should be in game logic.
        }
    }

    // Load the image icon for the card from a resource path
    private void loadCardImage() {
        String rankPrefix = getRankPrefix(this.rank);
        String suitPrefix = getSuitPrefix(this.suit);
        String imagePath = "Cards/" + rankPrefix + "-" + suitPrefix + ".png";
        this.image = new ImageIcon(imagePath);
    }

    // Helper method to convert Rank to its filename abbreviation
    private String getRankPrefix(Rank rank) {
        switch (rank) {
            case Ace: return "A";
            case Two: return "2";
            case Three: return "3";
            case Four: return "4";
            case Five: return "5";
            case Six: return "6";
            case Seven: return "7";
            case Eight: return "8";
            case Nine: return "9";
            case Ten: return "10";
            case Jack: return "J";
            case Queen: return "Q";
            case King: return "K";
            default: return ""; // Not expected to happen
        }
    }

    // Helper method to convert Suit to its filename abbreviation
    private String getSuitPrefix(Suit suit) {
        switch (suit) {
            case Clubs: return "C";
            case Spades: return "S";
            case Hearts: return "H";
            case Diamonds: return "D";
            default: return ""; // Not expected to happen
        }
    }

    // Getters for the card's properties
    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getCardValue() {
        return cardValue;
    }

    public ImageIcon getImage() {
        return image;
    }

    // Return a string representation of the card, e.g., "Ace of Hearts"
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}