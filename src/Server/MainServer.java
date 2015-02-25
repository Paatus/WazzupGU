package Server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class MainServer implements MainServerInterface {
	
	// hashmap to store the messages in, using our Message class for storing the messages
	private final static HashMap<String, LinkedList<Message>> messages = new HashMap<>(); 
	private final static HashMap<Integer, String> user_list = new HashMap<>(); 
	
	// adds messages to the hashmap
	public int add(String msg, String from, String to)
	{
		// if the message is valid and from/to are phone numbers
		if(is_valid_message(msg) && is_phonenumber(from) && is_phonenumber(to))
		{
			// get a key
			int num = IdManager.generateId();
			// initialize a new Message class using the data we have
			Message message = new Message(num, msg, from, to);
			// insert it at the key
			LinkedList<Message> temp_list;
			if (messages.containsKey(to)) {
				temp_list = messages.get(to);
			} else {
				temp_list = new LinkedList<Message>();
			}
			temp_list.add(message);
			user_list.put(new Integer(num), to);
			messages.put(to, temp_list);
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
		boolean status = false;
		if(user_list.containsKey(new Integer(key)))
		{
			// remove it
			String id_num = user_list.get(new Integer(key));
			LinkedList<Message> temp_list = messages.get(id_num);
			for (int i = 0; i < temp_list.size(); ++i) {
				if (temp_list.get(i).getID() == key) {
					temp_list.remove(i);
					status = true;
					break;
				}
			}
			user_list.remove(new Integer(key));
			// return the key
			if (status)
				return key;
			else
				return -1;
		}
		// if there was no message at that key, return -1
		return -1;
	}
	
	// replaces the message within the message class, if it has not been fetched yet
	public int replace(int key, String newMsg)
	{
		if(user_list.containsKey(key) && is_valid_message(newMsg))
		{
		// get the message at the supplied key
			String usr_id = user_list.get(key);
			LinkedList<Message> msg_list = messages.get(usr_id);
			int pos = -1;
			for (int i = 0; i < msg_list.size(); ++i) {
				if (msg_list.get(i).getID() == key) {
					pos = i;
					break;
				}
			}
			if (pos == -1) {
				return -1;
			}
			Message msg = msg_list.get(pos);
            msg.setMessage(newMsg);
            return key;
		}
		return -1;
	}
	
	// fetches the messages for the supplied recipient
	public String fetch(String recip)
	{
		// if the recipient is a phone number
		if(is_phonenumber(recip) && messages.containsKey(recip))
		{
			// initialize a XMLHandler
			// this initializes a nex xml document
			XMLHandler handler = new XMLHandler();
			// initialize occurencies counter
			String usr_id = recip;

			int occurencies = 0;
			// get all the keys in the hashmap
			LinkedList<Message> msg_list = messages.get(usr_id);
			// enumerate through them
			for (int i = 0; i < msg_list.size(); ++i) {
				if (!msg_list.get(i).isFetched()) {
					// set the fetched-variable to true
					msg_list.get(i).setFetched();
					// add 1 to occurencies, meaning that we found a message
					occurencies++;
					// add the message to the xml
					// added in the format <msg ID="id" sender="from" recipient="to" message="message" />
					handler.AddMessage(msg_list.get(i));
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
		if(is_phonenumber(recip) && messages.containsKey(recip))
		{
			String usr_id = recip;
			int removed_count = 0;
			LinkedList<Message> msg_list = messages.get(usr_id);
			LinkedList<Message> temp_list = new LinkedList<Message>();
			for(Message msg : msg_list)
			{
				if(msg.isFetched())
				{
					temp_list.add(msg);
				}
			}
			for (Message msg : temp_list) {
				user_list.remove(msg.getID());
				msg_list.remove(msg);
				removed_count++;
			}
			if(removed_count > 0)
				return removed_count;
		}
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