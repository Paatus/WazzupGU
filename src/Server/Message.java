package Server;

public class Message {

	private String message;
	private String sender;
	private String recipient;
	private int ID;
	
	public Message(int ID, String msg, String from, String to)
	{
		this.ID = ID;
		message = msg;
		sender = from;
		recipient = to;
	}
	
	public void setMessage(String msg)
	{
		message = msg; 
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getSender()
	{
		return sender;
	}
	
	public String getRecipient()
	{
		return recipient;
	}
	
	public String getXMLString()
	{
		XMLHandler p = new XMLHandler();
		p.AddMessage(this);
		return p.getDocument();
	}
	
}
