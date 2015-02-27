package Server;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MockConnectionHandler {
	public ServerThread server_handler = null;
	public ConnectionHandler conn_handler = null;
	public Socket socket = null;
	public String xml_header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

	@Before
	public void preTestMethod() {
		MainServer.reset_server();
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

		when(server_handler.newConnectionHandler(any(Socket.class))).then(new Answer<ConnectionHandler>() {

			@Override
			public ConnectionHandler answer(InvocationOnMock invocation) throws Throwable {
		        Object[] args = invocation.getArguments();
		        Socket insocket = (Socket)args[0];
				return new ConnectionHandler(insocket);
			}});

	}
	
	@Test
	public void testServerConnect() throws IOException {
		String client_id = "0046123123456";
        String text = xml_header + "<request id=\"" + client_id + "\"/>" + '\n';
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(text.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();
		//System.out.println(baos.toString());
        Document doc = XMLHandler.loadXMLFromString(baos.toString());
        if (doc == null) {
                System.out.println("oopsie");
        }
        Node node = doc.getChildNodes().item(0);
        NamedNodeMap attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("Wrong id returned when request!", attrs.getNamedItem("id").getNodeValue().equals(client_id));
	}

	@Test
	public void testServerAddMsg() throws IOException {
		String client_id = "0046123123456";
		String recip_id = "0046666123456";
		String msg = "U STOOPID";
        String req_msg_output = xml_header + "<request id=\"" + client_id + "\"/>";
        String add_msg_output = xml_header + "<add_message content=\"" + msg + "\" reciever=\""+ recip_id +"\"/>";
        String final_output = req_msg_output + "\n" +  add_msg_output;
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();
		//System.out.println(baos.toString());
		String[] mamma = baos.toString().split("\n");
        Document doc = XMLHandler.loadXMLFromString(mamma[0]);
        if (doc == null) {
                System.out.println("oopsie");
        }
        Node node = doc.getChildNodes().item(0);
        NamedNodeMap attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned from the server!", attrs.getNamedItem("id").getNodeValue().equals(client_id));
        Document doc_two = XMLHandler.loadXMLFromString(mamma[1]);
        if (doc_two == null) {
                System.out.println("oopsie");
        }
        Node node_two = doc_two.getChildNodes().item(0);
        NamedNodeMap attrs_two = node_two.getAttributes();
        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("message"));
        assertTrue("Something wrong with the message id!", !Pattern.matches("[a-zA-Z]+", attrs_two.getNamedItem("added").getNodeValue()));
	}
	
	@Test
	public void testServerFetchMsg() throws IOException {
		{
		String client_id = "0046123123456";
		String recip_id = "0046666123456";
		String msg = "U STOOPID";
        String req_msg_output = xml_header + "<request id=\"" + client_id + "\"/>";
        String add_msg_output = xml_header + "<add_message content=\"" + msg + "\" reciever=\""+ recip_id +"\"/>";
        String final_output = req_msg_output + "\n" + add_msg_output;
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();
		//System.out.println(baos.toString());
		String[] mamma = baos.toString().split("\n");
        Document doc = XMLHandler.loadXMLFromString(mamma[0]);
        if (doc == null) {
                System.out.println("oopsie");
        }
        Node node = doc.getChildNodes().item(0);
        NamedNodeMap attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned from the server!", attrs.getNamedItem("id").getNodeValue().equals(client_id));
        Document doc_two = XMLHandler.loadXMLFromString(mamma[1]);
        if (doc_two == null) {
                System.out.println("oopsie");
        }
        Node node_two = doc_two.getChildNodes().item(0);
        NamedNodeMap attrs_two = node_two.getAttributes();
        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("message"));
        assertTrue("Something wrong with the message id!", !Pattern.matches("[a-zA-Z]+", attrs_two.getNamedItem("added").getNodeValue()));
		}
		{
		//String recip_id = "0046123123456";
		String client_id = "0046666123456";
		//String msg = "U STOOPID";
        String req_msg_output = xml_header + "<request id=\"" + client_id + "\"/>";
        String add_msg_output = xml_header + "<fetch_messages/>";
        String final_output = req_msg_output + "\n" + add_msg_output + "\n";
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();
		//System.out.println(baos.toString());
		String[] mamma = baos.toString().split("\n");
        Document doc = XMLHandler.loadXMLFromString(mamma[0]);
        if (doc == null) {
                System.out.println("oopsie");
        }
        Node node = doc.getChildNodes().item(0);
        NamedNodeMap attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned from the server!", attrs.getNamedItem("id").getNodeValue().equals(client_id));
        Document doc_two = XMLHandler.loadXMLFromString(mamma[1]);
        if (doc_two == null) {
                System.out.println("oopsie");
        }
        Node node_two = doc_two.getChildNodes().item(0);
        NamedNodeMap attrs_two = node_two.getAttributes();
        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("Messages"));
        for (int i = 0; i < node_two.getChildNodes().getLength(); ++i) {
        	System.out.println(node_two.getChildNodes().item(i).getAttributes().getNamedItem("message"));
        	System.out.println(node_two.getChildNodes().item(i).getAttributes().getNamedItem("id"));
        	System.out.println(node_two.getChildNodes().item(i).getAttributes().getNamedItem("recipient"));
        	System.out.println(node_two.getChildNodes().item(i).getAttributes().getNamedItem("sender"));
        }
        //assertTrue("Something wrong with the message id!", !Pattern.matches("[a-zA-Z]+", attrs_two.getNamedItem("added").getNodeValue()));
        /*for (String s : mamma) {
                System.out.println("meh: " + s);
        }*/
        }
	}
	
}
