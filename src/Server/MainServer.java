package Server;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

public class MainServer implements MainServerInterface {

	HashMap<Integer, Message> messages = new HashMap<>(); 
	
	/**
	  Description:
		returns a positive integer on success, this integer is the key in the Map
		on fail returns -1
	  Pre-condition:
	  Post-condition: 
	  Test-cases:
	  	testAdd()
	  	testEmptyAdd()
	*/
	public int add(String msg, String from, String to)
	{
		if(isValidID(msg) && isValidID(from) && isValidID(to))
		{
			int num = messages.size()+1;
			Message message = new Message(num, msg, from, to);
			messages.put(num, message);
			return num;
		}
		return -1;
	}
	
	/**
	  Description
		returns a positive integer on success, this integer is the key in the Map
		on fail returns -1
	  Pre-condition:
	  Post-condition:
	  Test-cases:
		testDelete()
		testWrongDelete()
	*/
	public int delete(int ID)
	{
		if(messages.get(ID) != null)
		{
			messages.remove(ID);
			return ID;
		}
		return -1;
	}
	
	
	/**
	  Description:
		returns the key to the modified message on success, -1 on fail
	  Pre-condition:
	  Post-condition: 
	  Test-cases:
	  	testReplace()
	  	testWrongReplace()
	  	testEmptyReplace()
	*/

	public int replace(int ID, String newMsg)
	{
		Message msg = messages.get(ID);
		if(msg != null)
		{
			if(isValidID(newMsg))
			{
				msg.setMessage(newMsg);
				messages.remove(ID);
				messages.put(ID, msg);
				return ID;	
			}
		}
		return -1;
	}
	
	/**

	  Description:
		returns xml string on success
		returns "No messages" if no messages are present for supplied recipient
	  Pre-condition:
	  Post-condition:
	  Test-cases:
		testXMLString()
		testXMLStringWrong()
	*/
	public String fetch(String recip)
	{
		XMLHandler handler = new XMLHandler();
		int occurencies = 0;
		Set<Integer> keys = messages.keySet();
		for(Integer key : keys)
		{
			Message m = messages.get(key);
			if(m.getRecipient().equals(recip))
			{
				occurencies++;
				handler.AddMessage(m);
			}
		}
		if(occurencies == 0)
		{
			return "No messages";
		}
		return handler.getDocument();
	}
	
	
	/**
	  Description:
	  	removes all the messages with supplied reciever
	  	returns a positive integer on success
	  	returns a negative integer on fail
	  Pre-condition:
	  Post-condition: 
	  Test-cases:
	  	testFetchComplete()
	  	testWrongFetchComplete()
	*/
	public int fetch_complete(String recip)
	{
		int removed_count = 0;
		Set<Integer> keys = messages.keySet();
		for(Integer key : keys)
		{
			Message m = messages.get(key);
			if(m.getRecipient().equals(recip))
			{
				messages.remove(key);
				removed_count++;
			}
		}
		if(removed_count > 0)
			return removed_count;
		return -1;
	}
	
	// checks if supplied string is valid and not empty
	private boolean isValidID(String ID)
	{
		if(ID != null && !ID.equals("") && ID.length() > 0)
		{
			return true;
		}
		return false;
	}
}