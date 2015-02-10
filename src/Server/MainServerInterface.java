package Server;

public interface MainServerInterface {
	
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
	public int add(String msg, String from, String to);
	
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
	public int delete(int ID);
	
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
	public int replace(int ID, String newMsg);
	
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
	public String fetch(String recip);
	
	
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
	public int fetch_complete(String recip);

}