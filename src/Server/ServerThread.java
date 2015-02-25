package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {

	private ServerSocket myService;
	private boolean turn_off = false;

	public ServerThread() {
		try {
			myService = new ServerSocket(4441);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (!turn_off) {
				Socket s = myService.accept();
				new Thread(new ConnectionHandler(s)).start();
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

}
