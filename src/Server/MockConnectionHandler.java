package Server;

import static org.mockito.Mockito.*;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.NamedNodeMap;

public class MockConnectionHandler {
	ServerThread server_handler = null;
	ConnectionHandler conn_handler = null;
	@Before
	public void thisIsStupid() {
		MockitoAnnotations.initMocks(this);
		server_handler = spy(new ServerThread());
		conn_handler = mock(ConnectionHandler.class);
		//doNothing().when(ch).request_handling(any(NamedNodeMap.class));
		/*doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				System.out.println("THIS IS A FUCKING VOID!");
				return null;
			}
			
		}).when(ch).run();*/
		//doReturn(ch).when(lol).newConnectionHandler(any(Socket.class));
		
		//Following prints "called with arguments: foo"
		//System.out.println(mock.someMethod("foo"));

	}
	
	@Test
	public void serverDeclinesConnection() {
		when(server_handler.newConnectionHandler(any(Socket.class))).then(new Answer<ConnectionHandler>() {

			@Override
			public ConnectionHandler answer(InvocationOnMock invocation) throws Throwable {
		        Object[] args = invocation.getArguments();
		        Socket insocket = ((Socket)args[0]);
		        System.out.println("Refused connection from kuk(en)!");
				//return (ConnectionHandler)((Socket)args[0]);
		        insocket.close();
		        server_handler.stopRunning();
				return null;
			
			
			}});
		server_handler.run();
		//System.out.println(verify(lol).newConnectionHandler(any(Socket.class)));
	}
	
	@Test
	public void clientClosesConnection() {
	}
}
