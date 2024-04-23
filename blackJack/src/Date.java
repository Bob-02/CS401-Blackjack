
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
	private String date;
	
	public Date() {
		date = getCurrentDate();
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getCurrentDate() {
		DateTimeFormatter dateFormat =
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return now.format(dateFormat);
	}
}