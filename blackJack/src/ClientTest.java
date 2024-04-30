import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
    }
    @Test
    public void testServerAddressAndPortConstants() {
        assertNotNull(Client.SERVER_ADDRESS);
        assertEquals(7777, Client.SERVER_PORT);
    }
    
    @Test
    public void testObjectStreamsInitialization() {
        assertNotNull(Client.objectOutputStream);
        assertNotNull(Client.objectInputStream);
    }
    
    @Test
    public void testNextButtonClick() {
        // Test getting the next button click
        String expectedFirstClick = "registerationButtonClicked";
        assertEquals(expectedFirstClick, client.getNextButtonClick()); // Use instance method

        // Simulate some button clicks and ensure they are returned in the correct order
        String[] expectedButtonClicks = {
                "anotherPlayerLogin",
                "opengameButtonClicked",
                "manuallyAddDealer"
        };
        for (String expectedClick : expectedButtonClicks) {
            String actualClick = client.getNextButtonClick(); // Use instance method
            assertEquals(expectedClick, actualClick);
        }
    }
    
    @Test
    public void testNextButtonClickNotNull() {
        // Test that the next button click is not null
        assertNotNull(client.getNextButtonClick());
    }
 
    @Test
    public void testMessageToGuiInitialization() {
        Client client = new Client();
        assertNull(client.getMessageTogui());
    }
    @Test
    public void testClickIndexInitialization() {
        assertEquals(0, Client.clickIndex);
    }
}
