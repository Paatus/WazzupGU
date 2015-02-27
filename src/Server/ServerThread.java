package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {

	public ServerSocket myService;
	private boolean turn_off = false;

	public ServerThread() {
		try {
			myService = new ServerSocket(4444);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
		    try {
		        myService.close();
		        System.out.println("The server is shut down!");
		    } catch (IOException e) {}
		}});
	}

	@Override
	public void run() {
		try {
			while (!turn_off) {
				Socket s = myService.accept();
				System.out.println("You suck stoopid½!");
				ConnectionHandler ch = newConnectionHandler(s);
				new Thread(ch).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopRunning() {
		turn_off = true;
		try {
			myService.close();
		} catch (IOException e) {
		}
	}
	
	public ConnectionHandler newConnectionHandler(Socket s) {
		return new ConnectionHandler(s);
	}

}
