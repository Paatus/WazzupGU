package Server;

public interface MainServerInterface {
	
	/**
	  Description:
	  	Adds a message to the server.
	  Pre-condition:
	  	there should exist a Collection in which to store messages, within the class.
		msg should be non-null and have a positive length up to 65535.
		from & to should be valid telephone numbers that follow the E.164 standard.
	  Post-condition:
	  	adds the message to the collection
		returns positive integer on success
		returns negative integer on fail
	  Test-cases:
	  	MainTest.MainAddTest.testAdd()
	  	MainTest.MainAddTest.testEmptyAdd()
	  	MainTest.MainAddTest.testHugeAdd()
	  	MainTest.MainAddTest.testWrongNumberAdd()
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
		MainTest.MainDeleteTest.testDelete()
		MainTest.MainDeleteTest.testWrongDelete()
	*/
	public int delete(int ID);
	
	/**
	  Description:
		replaces message with corresponding id, with newMsg
	  Pre-condition:
	  	there should exist a Collection in which to store messages, within the class.
	  	there should exist a message with supplied id for success.
		message should be non-null and have a positive length up to 65535.
	  Post-condition:
	  	changes the message-string for message with corresponding id
	  	returns a positive integer on success
		returns a negative integer on fail
	  Test-cases:
	  	MainTest.MainReplaceTest.testReplace()
	  	MainTest.MainReplaceTest.testWrongReplace()
	  	MainTest.MainReplaceTest.testEmptyReplace()
	  	MainTest.MainReplaceTest.testHugeReplace()
	*/
	public int replace(int ID, String newMsg);
	
	/**
	  Description:
	  	Fetches all the messages for the supplied telephone number
	  Pre-condition:
		there should exist a Collection in which to store messages, within the class
		recip should be valid telephone numbers that follow the E.164 standard.
		there should be at least 1 message for supplied receiver in the collection
	  Post-condition:
	 	sets the fetched metadata to true for all messages with supplied recipient
		returns xml string on success
		returns "No messages" if no messages are present for supplied recipient
	  Test-cases:
		MainTest.MainFetchTest.testXMLString()
		MainTest.MainFetchTest.testXMLStringWrong()
		MainTest.MainFetchTest.testXMLNumberFormatWrong()
		MainTest.MainFetchTest.testWrongNumberXML()
	*/
	public String fetch(String recip);
	
	/**
	  Description:
	  	Finalizes the fetch, and removes the messages from the server, also sends xml data to client?
	  Pre-condition:
		there should exist a Collection in which to store messages, within the class
		recip should be valid telephone numbers that follow the E.164 standard.
		there should be at least 1 message for supplied receiver in the collection
	  Post-condition:
		removes all messages with supplied recipient from the collection
		returns a positive integer on success
	  	returns a negative integer on fail
	  Test-cases:
	  	MainTest.MainFetchCompleteTest.testFetchComplete()
	  	MainTest.MainFetchCompleteTest.testWrongFetchComplete()
	  	MainTest.MainFetchCompleteTest.testWrongNumberFetchComplete()
	*/
	public int fetch_complete(String recip);
}