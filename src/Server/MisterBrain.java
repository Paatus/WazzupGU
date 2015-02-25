package Server;

import java.io.Console;

public class MisterBrain {

	public static void main(String[] args) {
		ServerThread st = new ServerThread();
		Thread t = new Thread(st);
		t.start();
		while (true) {
		}
	}

}
