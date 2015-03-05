package Server;

import org.w3c.dom.NamedNodeMap;

public interface ConnectionHandlerInterface {

	/**
	  Description:
	  	Requests the current socket to be associated with a certain account.
	  Pre-condition:
	  	There should be a socket connection that has done a correct xml call.  
	  Post-condition:
	  	Associates the current socket to a certain Account number by giving the id to a string variable.
	  	Returns a xml response stating that request accepted.
	  Test-cases:
	  	MockConnectionHandler.TestServerConnect()
	*/
	public void request_handling(NamedNodeMap attrs);

	/**
	  Description:
	  	Adds a message from the user associated with the socket to another user.
	  Pre-condition:
	  	The socket should be associated with a user account.
	  	The xml associated with the call should specify a correct account.
	  Post-condition:
	  	Adds the message to another users messagequeue
	  	Returns a xml stating message added or error.
	  Test-cases:
	  	MockConnectionHandler.TestServerAddMsg()
	*/
	public void add_message_handler(NamedNodeMap attrs);

	/**
	  Description
	  	Deletes a message sent from the user associated with the socket.
	  Pre-condition:
	  	The socket should be associated with a user account.
	  	The xml associated with the call should specify a correct message id.
	  	There should exist a message with the specified id that is from the current useraccount.
	  Post-condition:
	  	Removes the message associated with the id.
	  	Returns a xml stating message removed or error.
	  Test-cases:
	  	MockConnectionHandler.TestServerDelete()
	*/
	public void delete_message_handler(NamedNodeMap attrs);

	/**
	  Description:
	  	Replaces a message sent from the user associated with the socket.
	  Pre-condition:
	  	The socket should be associated with a user account.
	  	The xml associated with the call should specify a correct message id.
	  	There should exist a message with the specified id that is from the current useraccount.
	  Post-condition:
	  	Removes the message associated with the id.
	  	Returns a xml stating message replaced or error.
	  Test-cases:
	  	MockConnectionHandler.TestServerReplace()
	*/
	public void replace_message_handler(NamedNodeMap attrs);

	/**
	  Description:
	  	Fetches all messages sent to the user associated with the socket.
	  Pre-condition:
	  	The socket should be associated with a user account.
	  	An xml should indicate that a fetch is started.
	  Post-condition:
		Returns xml node that contains all messages or the text "No messages" on an error.
	  Test-cases:
	  	MockConnectionHandler.testServerFetchMsg()
	*/
	public void fetch_messages_handler(NamedNodeMap attrs);

	/**
	  Description:
	  	Finalizes the fetch and tells the server to remove all fetched messages.
	  Pre-condition:
	  	The socket should be associated with a user account.
	  	There should have been an fetched call before using this call.
	  	An xml should indicate that a fetch complete is called.
	  Post-condition:
		removes all messages that were previously fetched.
		Returns a xml stating fetch_complete_ack or error.
	  Test-cases:
	  	MockConnectionHandler.testServerFetchComplete()
	*/
	public void fetch_complete_handler(NamedNodeMap attrs);

}
