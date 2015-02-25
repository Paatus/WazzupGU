package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

	Socket clientSocket = null;
	DataOutputStream outToServer;
	XMLHandler handler;
	BufferedReader inFromServer;
	
	public Client()
	{
		try {
			clientSocket = new Socket("localhost", 4445);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String request(String id)
	{
		String ret = "";
		handler = new XMLHandler("request");
		handler.addAttribute("id", id);
		try
		{
			outToServer.writeBytes(handler.getDocument() + "\n");
			while((ret = inFromServer.readLine()) != null)
			{
				return ret;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String addMessage(String to, String message)
	{
		String ret = "";
		handler = new XMLHandler("add_message");
		handler.addAttribute("content", message);
		handler.addAttribute("reciever", to);
		try {
			outToServer.writeBytes(handler.getDocument());
			ret = inFromServer.readLine();
		} catch(Exception e) {}
		return ret;
	}
	
	public String deleteMessage(String id)
	{
		String ret = "";
		handler = new XMLHandler("delete_message");
		handler.addAttribute("id", id);
		try {
			outToServer.writeBytes(handler.getDocument());
			while((ret = inFromServer.readLine()) != null);
		} catch(Exception e) {}
		return ret;
	}
	
	public String replaceMessage(String id, String content)
	{
		String ret = "";
		handler = new XMLHandler("replace_message");
		handler.addAttribute("id", id);
		handler.addAttribute("content", content);
		try {
			outToServer.writeBytes(handler.getDocument());
			ret = inFromServer.readLine();
		} catch(Exception e) {}
		return ret;
	}
	
	public String fetchMessages()
	{
		String ret = "";
		handler = new XMLHandler("fetch_messages");
		try {
			outToServer.writeBytes(handler.getDocument());
			ret = inFromServer.readLine();
		} catch(Exception e) {}
		return ret;
	}
	
	public String fetchComplete()
	{
		String ret = "";
		handler = new XMLHandler("fetch_complete");
		try {
			outToServer.writeBytes(handler.getDocument());
			ret = inFromServer.readLine();
		} catch(Exception e) {}
		return ret;
	}
}
