package Server;

import java.util.concurrent.atomic.AtomicInteger;

public class IdManager {
	public final static AtomicInteger _id = new AtomicInteger();
	
	public static int generateId() {
		return _id.incrementAndGet();
	}
	
}
