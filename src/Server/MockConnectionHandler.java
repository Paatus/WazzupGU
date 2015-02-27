package Server;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
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
	public ServerThread server_handler = null;
	public ConnectionHandler conn_handler = null;
	public Socket socket = null;
	@Before
	public void thisIsStupid() {
		MockitoAnnotations.initMocks(this);
		server_handler = mock(ServerThread.class);
		conn_handler = mock(ConnectionHandler.class);
		socket = mock(Socket.class);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				((ServerThread)invocation.getMock()).newConnectionHandler(socket).run();
				return null;
			}
			
		}).when(server_handler).run();
		//doNothing().when(ch).request_handling(any(NamedNodeMap.class));
		//doReturn(ch).when(lol).newConnectionHandler(any(Socket.class));
		
		//Following prints "called with arguments: foo"
		//System.out.println(mock.someMethod("foo"));

	}
	
	@Test
	public void serverDeclinesConnection() throws IOException {
        String text = "<request id=\"0046123123456\"/>";
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(text.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		when(server_handler.newConnectionHandler(any(Socket.class))).then(new Answer<ConnectionHandler>() {

			@Override
			public ConnectionHandler answer(InvocationOnMock invocation) throws Throwable {
		        Object[] args = invocation.getArguments();
		        Socket insocket = (Socket)args[0];
		        //when(insocket.getInputStream()).thenReturn(new ByteArrayInputStream(text.getBytes()));
		        //System.out.println("Refused connection from kuk(en)!");
				//return (ConnectionHandler)((Socket)args[0]);
		        //insocket.close();
		        //§server_handler.stopRunning();
				return new ConnectionHandler(insocket);
			
			
			}});
		server_handler.run();
		System.out.println(baos.toString());
		//System.out.println(verify(lol).newConnectionHandler(any(Socket.class)));
	}
	
	@Test
	public void clientClosesConnection() {
	}
}
