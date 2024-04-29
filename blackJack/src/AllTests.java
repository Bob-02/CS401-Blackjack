
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ CardTest.class, TableTest.class, PlayingDeckTest.class, DeckTest.class, DealerTest.class,
		PlayerTest.class })
class AllTests {

}
