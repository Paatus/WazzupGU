package Server;

import org.w3c.dom.NamedNodeMap;

public interface ConnectionHandlerInterface {
	public void request_handling(NamedNodeMap attrs);
	public void add_message_handler(NamedNodeMap attrs);
	public void delete_message_handler(NamedNodeMap attrs);
	public void replace_message_handler(NamedNodeMap attrs);
	public void fetch_messages_handler(NamedNodeMap attrs);
	public void fetch_complete_handler(NamedNodeMap attrs);
}
