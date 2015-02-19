package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Network {

	ServerSocket MyService;	
	public Network()
	{
		try {
		    MyService = new ServerSocket(4444);
		    while(true)
		    {

		    	Socket s = MyService.accept();
		    	new Thread(new clientHandler(s)).start();
		    }
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args)
	{
		new Network();
	}
	
	private class clientHandler implements Runnable {
		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		private XMLHandler handler;
		
		public clientHandler(Socket s) {
			this.client = s;
			handler = new XMLHandler();
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		    	out = new PrintWriter(client.getOutputStream(), true);
			}catch(Exception e) {}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			XMLHandler handler;
			MainServer server = new MainServer();
			String user_id = null;
				String string;
				try {
				while((string = in.readLine()) != null) {
					try {
						System.out.println("got " + string);
						Document doc = XMLHandler.loadXMLFromString(string);
						Node node = doc.getChildNodes().item(0);
						NamedNodeMap attrs = node.getAttributes();
						switch(node.getNodeName()) {
						case "request":
							handler = new XMLHandler("accept");
							user_id = attrs.getNamedItem("id").getNodeValue();
							handler.addAttribute("id",user_id + "");
							out.print(handler.getDocument());
							out.flush();
							break;
						case "add_message":
							String content = attrs.getNamedItem("content").getNodeValue();
							String to = attrs.getNamedItem("reciever").getNodeValue();
							int id = server.add(content, user_id, to);
							System.out.println("Added '" + content + "' for recipient " + to);
							if(id > 0)
							{
								handler = new XMLHandler("message");
								handler.addAttribute("added",id + "");
							} else {
								handler = new XMLHandler("error");
								handler.addAttribute("reason", "DU SUGER SNOPP");
							}
							out.print(handler.getDocument());
							out.flush();
							break;
						case "delete_message":
							int del_key = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							int id_del = server.delete(del_key);
							if(id_del > 0)
							{
								handler = new XMLHandler("message");
								handler.addAttribute("deleted",id_del + "");
							} else {
								handler = new XMLHandler("error");
								handler.addAttribute("reason", "DU SUGER SNOPP");
							}
							out.print(handler.getDocument());
							out.flush();
							break;
						case "replace_message":
							int rep_key = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							String rep_str = attrs.getNamedItem("content").getNodeValue();
							int id_rep = server.replace(rep_key, rep_str);
							if(id_rep > 0)
							{
								handler = new XMLHandler("message");
								handler.addAttribute("replaced", id_rep + "");
							} else {
								handler = new XMLHandler("error");
								handler.addAttribute("reason", "DU SUGER SNOPP");
							}
							out.print(handler.getDocument());
							out.flush();
							break;
						case "fetch_messages":
							String xml = server.fetch(user_id);
							System.out.println("fetch for " + user_id);
							out.print(xml);
							out.flush();
							break;
						case "fetch_complete":
							int kuk = server.fetch_complete(user_id);
							if(kuk > 0)
							{
								handler = new XMLHandler("fetch_complete_ack");
							} else {
								handler = new XMLHandler("error");
								handler.addAttribute("reason", "DU SUGER SNOPP");
							}
							out.print(handler.getDocument());
							out.flush();
							break;
						}
						//System.out.println("Recieved \"" + string + "\" from " + client.getInetAddress().toString());
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				}catch(Exception e) {}
		}
		
	}
}
