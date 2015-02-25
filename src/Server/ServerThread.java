package Server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread {

	ServerSocket myService;	
	public ServerThread()
	{
		try {
		    myService = new ServerSocket(4445);
		    while(true)
		    {

		    	Socket s = myService.accept();
		    	new Thread(new ConnectionHandler(s)).start();
		    }
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args)
	{
		new ServerThread();
	}
}
