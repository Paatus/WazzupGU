package Server;

import java.util.HashMap;
import java.util.Set;

public class MainServer implements MainServerInterface {
	
	// hashmap to store the messages in, using our Message class for storing the messages
	HashMap<Integer, Message> messages = new HashMap<>(); 
	
	// adds messages to the hashmap
	public int add(String msg, String from, String to)
	{
		// if the message is valid and from/to are phone numbers
		if(is_valid_message(msg) && is_phonenumber(from) && is_phonenumber(to))
		{
			// get a key
			int num = messages.size()+1;
			// initialize a new Message class using the data we have
			Message message = new Message(num, msg, from, to);
			// insert it at the key
			messages.put(num, message);
			// return the key
			return num;
		}
		// if the message was not valid or the from/to was not phone numbers
		return -1;
	}
	
	// delete a message
	public int delete(int key)
	{
		// if there is a message at the supplied key
		if(messages.get(key) != null)
		{
			// remove it
			messages.remove(key);
			// return the key
			return key;
		}
		// if there was no message at that key, return -1
		return -1;
	}
	
	// replaces the message within the message class, if it has not been fetched yet
	public int replace(int key, String newMsg)
	{
		// get the message at the supplied key
		Message msg = messages.get(key);
		// if we got a message (it's not null) and it's not fetched
		if(msg != null && !msg.isFetched())
		{
			// if the new message is a valid message
			if(is_valid_message(newMsg))
			{
				// set the new message in the message class we got at key to the new message
				msg.setMessage(newMsg);
				// return the key
				return key;
			}
		}
		// if there was no message at the key, or it was fetched, return -1
		return -1;
	}
	
	// fetches the messages for the supplied recipient
	public String fetch(String recip)
	{
		// if the recipient is a phone number
		if(is_phonenumber(recip))
		{
			// initialize a XMLHandler
			// this initializes a nex xml document
			XMLHandler handler = new XMLHandler();
			// initialize occurencies counter
			int occurencies = 0;
			// get all the keys in the hashmap
			Set<Integer> keys = messages.keySet();
			// enumerate through them
			for(Integer key : keys)
			{
				// get the message-class at key
				Message m = messages.get(key);
				// if the recipient of that message is the same as the supplied recipient
				if(m.getRecipient().equals(recip))
				{
					// set the fetched-variable to true
					m.setFetched();
					// add 1 to occurencies, meaning that we found a message
					occurencies++;
					// add the message to the xml
					// added in the format <msg ID="id" sender="from" recipient="to" message="message" />
					handler.AddMessage(m);
				}
			}
			// if there was no messages
			if(occurencies < 1)
			{
				return "No messages";
			}
			// if there was messages, return the entire xml
			return handler.getDocument();
		}
		// if the recipient was not a phone number
		return "No messages";
	}
	
	// finalize the fetch, this is ran after the fetch has returned the xml, to remove the messages that were fetched
	public int fetch_complete(String recip)
	{
		// if the recipient is a phone number
		if(is_phonenumber(recip))
		{
			// initialize a count for how many messages was removed
			int removed_count = 0;
			// get all the keys in the hashmap
			Set<Integer> keys = messages.keySet();
			// enumerate through them
			for(Integer key : keys)
			{
				// get the message class at the key
				Message m = messages.get(key);
				// if the recipient of the message-class is the same as the supplied and the message is fetched
				if(m.getRecipient().equals(recip) && m.isFetched() )
				{
					// remove the message from the hashmap
					messages.remove(key);
					// add 1 to the count
					removed_count++;
				}
			}
			// if messages were removed
			if(removed_count > 0)
				// return the count
				return removed_count;
		}
		// if the recipient is not a phone number or the count was not above 0
		return -1;
	}
	
	// checks if the supplied string is a valid message
	private boolean is_valid_message(String ID)
	{
		// if the string is not null, not empty, and between 0 and 65356 in length, it is valid
		if(ID != null && !ID.equals("") && ID.length() > 0 && ID.length() < 65356)
		{
			return true;
		}
		// otherwise it's not
		return false;
	}
	
	// checks if the supplied string is a valid telephone number
	private boolean is_phonenumber(String nr)
	{
		// if the string is not null, length is between 0 and 16, starts with "00" and all the characters in the sttring are numbers
		// then it's valid
		if(nr != null && nr.length() > 0 && nr.length() < 16 && nr.substring(0, 2).equals("00") && valid_numbers(nr))
		{
			return true;
		}
		return false;
	}
	
	// checks if string is purely numbers
	private boolean valid_numbers(String nr)
	{
		// loop through the string, char by char
		for(int i = 0; i < nr.length();i++)
		{
			// if the char is not a numeric value or negative
			if(Character.getNumericValue(nr.charAt(i)) < 0)
			{
				return false;
			}
		}
		// if it did not return false in the loop, return true
		return true;
	}
}