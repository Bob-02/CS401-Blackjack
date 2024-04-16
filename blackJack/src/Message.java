import java.io.Serializable;

enum Type{
	LoginPlayer, LoginDealer, Logout, 
	Text,
	CheckFunds, CheckFundHistory, AddFunds, CashOut,
	JoinGame, LeaveGame, OpenGame, CloseGame,
	JoinTable, LeaveTable,
	Default
	
	// Add new Types of messages as project features get built out.
}
enum Status{
	// Status added to messages to keep track of acknowledgment of an action.
	New, Success, Failed
}


public class Message implements Serializable {
    protected Type type;
    protected Status status;
    protected String text;

    public Message(){
        setType(Type.Default);
        setStatus(Status.New);
        setText("Undefined");
    }

    public Message(Type type, Status status, String text){
        setType(type);
        setStatus(status);
        setText(text);
    }

    private void setType(Type type){
    	this.type = type;
    }

    public void setStatus(Status status){
    	this.status = status;
    }

    public void setText(String text){
    	this.text = text;
    }

    public Type getType(){
    	return type;
    }

    public Status getStatus(){
    	return status;
    }

    public String getText(){
    	return text;
    }
    
    public void printMessage() {
    	System.out.println("Type: " + getType());
    	System.out.println("Status: " + getStatus());
    	System.out.println("Text: " + getText());
    }
}