package Server;

public interface MainServerInterface {
	
	/**
	  Description:
	  	Adds a message to the server.
	  Pre-condition:
	  	there should exist a Collection in which to store messages, within the class
		msg should be non-null and have a positive length (max length?)
		from & to should be valid telephone numbers (rules?)
	  Post-condition:
	  	adds the message to the collection
		returns positive integer on success
		returns negative integer on fail
	  Test-cases:
	  	testAdd()
	  	testEmptyAdd()
	*/
	public int add(String msg, String from, String to);
	
	/**
	  Description
	  	Deletes the message with the corresponding id.
	  Pre-condition:
	  	there should exist a Collection in which to store messages, within the class
		there should exist a message with supplied id for success
	  Post-condition:
	  	removes the message with supplied id from the collection
		returns a positive integer on success
		returns a negative integer on fail	
	  Test-cases:
		testDelete()
		testWrongDelete()
	*/
	public int delete(int ID);
	
	/**
	  Description:
		replaces message with corresponding id, with newMsg
	  Pre-condition:
	  	there should exist a Collection in which to store messages, within the class
	  	there should exist a message with supplied id for success
	  Post-condition:
	  	changes the message-string for message with corresponding id
	  	returns a positive integer on success
		returns a negative integer on fail
	  Test-cases:
	  	testReplace()
	  	testWrongReplace()
	  	testEmptyReplace()
	*/
	public int replace(int ID, String newMsg);
	
	/**

	  Description:
	  	Fetches all the messages for the supplied telelphone number
	  Pre-condition:
		there should exist a Collection in which to store messages, within the class
		recip should be a valid telephone number (rules?)
		there should be at least 1 message for supplied reciever in the collection
	  Post-condition:
		returns xml string on success
		returns "No messages" if no messages are present for supplied recipient
	  Test-cases:
		testXMLString()
		testXMLStringWrong()
	*/
	public String fetch(String recip);
	
	
	/**
	  Description:
	  	Finalizes the fetch, and removes the messages from the server, also sends xml data to client?
	  Pre-condition:
		there should exist a Collection in which to store messages, within the class
		recip should be a valid telephone number (rules?)
		there should be at least 1 message for supplied reciever in the collection
	  Post-condition:
		removes all messages with supplied reciepient from the collection
		returns a positive integer on success
	  	returns a negative integer on fail
	  Test-cases:
	  	testFetchComplete()
	  	testWrongFetchComplete()
	*/
	public int fetch_complete(String recip);
}