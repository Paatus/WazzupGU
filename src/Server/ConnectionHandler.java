package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ConnectionHandler implements Runnable, ConnectionHandlerInterface {
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private XMLHandler handler;
	private String user_id = null;
	private MainServer server;

	public ConnectionHandler(Socket s) {
		this.server = new MainServer();
		this.client = s;
		handler = new XMLHandler();
		try {
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		// XMLHandler handler;
		String string;
		try {
			while ((string = in.readLine()) != null) {
				try {
					System.out.println("got: " + string);
					Document doc = XMLHandler.loadXMLFromString(string);
					if (doc == null) {
						System.out.println("oopsie");
						break;
					}
					Node node = doc.getChildNodes().item(0);
					NamedNodeMap attrs = node.getAttributes();
					switch (node.getNodeName()) {
                        case "request":
                                request_handling(attrs);
                                break;
                        case "add_message":
                                add_message_handler(attrs);
                                break;
                        case "delete_message":
                                delete_message_handler(attrs);
                                break;
                        case "replace_message":
                                replace_message_handler(attrs);
                                break;
                        case "fetch_messages":
                                fetch_messages_handler(attrs);
                                break;
                        case "fetch_complete":
                                fetch_complete_handler(attrs);
                                break;
					}
					// System.out.println("Recieved \"" + string + "\" from " +
					// client.getInetAddress().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
	}
	
	public void request_handling(NamedNodeMap attrs) {
        handler = new XMLHandler("accept");
        user_id = attrs.getNamedItem("id").getNodeValue();
        handler.addAttribute("id", user_id);
        String ret_str = handler.getDocument();
        out.println(ret_str);
	}
	public void add_message_handler(NamedNodeMap attrs) {
        String content = attrs.getNamedItem("content")
                        .getNodeValue();
        String to = attrs.getNamedItem("reciever")
                        .getNodeValue();
        int id = server.add(content, user_id, to);
        System.out.println("Added '" + content
                        + "' for recipient " + to);
        if (id > 0) {
                handler = new XMLHandler("message");
                handler.addAttribute("added", id + "");
        } else {
                handler = new XMLHandler("error");
                handler.addAttribute("reason", "DU SUGER SNOPP");
        }
        out.print(handler.getDocument());
        out.flush();
	}

	public void delete_message_handler(NamedNodeMap attrs) {
        int del_key = Integer.parseInt(attrs.getNamedItem("id")
                        .getNodeValue());
        int id_del = server.delete(del_key);
        if (id_del > 0) {
                handler = new XMLHandler("message");
                handler.addAttribute("deleted", id_del + "");
        } else {
                handler = new XMLHandler("error");
                handler.addAttribute("reason", "DU SUGER SNOPP");
        }
        out.print(handler.getDocument());
        out.flush();
	}

	public void replace_message_handler(NamedNodeMap attrs) {
        int rep_key = Integer.parseInt(attrs.getNamedItem("id")
                        .getNodeValue());
        String rep_str = attrs.getNamedItem("content")
                        .getNodeValue();
        int id_rep = server.replace(rep_key, rep_str);
        if (id_rep > 0) {
                handler = new XMLHandler("message");
                handler.addAttribute("replaced", id_rep + "");
        } else {
                handler = new XMLHandler("error");
                handler.addAttribute("reason", "DU SUGER SNOPP");
        }
        out.print(handler.getDocument());
        out.flush();
	}

	public void fetch_messages_handler(NamedNodeMap attrs) {
        String xml = server.fetch(user_id);
        System.out.println("fetch for " + user_id);
        out.print(xml);
        out.flush();
	}

	public void fetch_complete_handler(NamedNodeMap attrs) {
        int fetch_num = server.fetch_complete(user_id);
        if (fetch_num > 0) {
                handler = new XMLHandler("fetch_complete_ack");
        } else {
                handler = new XMLHandler("error");
                handler.addAttribute("reason", "DU SUGER SNOPP");
        }
        out.print(handler.getDocument());
        out.flush();
	}
}
