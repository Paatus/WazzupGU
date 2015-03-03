package Server;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
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

		String[] sender_return = baos.toString().split("\n");
        Document doc = XMLHandler.loadXMLFromString(sender_return[0]);

        assertFalse("No response from request connection!",doc == null);

        Node node = doc.getChildNodes().item(0);
        NamedNodeMap attrs = node.getAttributes();

        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not correct from the server!", attrs.getNamedItem("id").getNodeValue().equals(client_id));

        Document doc_two = XMLHandler.loadXMLFromString(sender_return[1]);

        assertFalse("No response from add message connection!",doc_two == null);
        
        Node node_two = doc_two.getChildNodes().item(0);
        NamedNodeMap attrs_two = node_two.getAttributes();
        
        String id = attrs_two.getNamedItem("added").getNodeValue();

        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("message"));
        assertTrue("Something wrong with the message id!", !Pattern.matches("[a-zA-Z]+", attrs_two.getNamedItem("added").getNodeValue()));

        //----------------------------------------------------------//

		String fetch_sender_id = "0046123123456";
		String fetch_client_id = "0046666123456";

        req_msg_output = xml_header + "<request id=\"" + fetch_client_id + "\"/>";
        String fetch_msg_output = xml_header + "<fetch_messages/>";
        final_output = req_msg_output + "\n" + fetch_msg_output + "\n";
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();

		String[] fetcher_return = baos.toString().split("\n");
		System.out.println(baos.toString());
        doc = XMLHandler.loadXMLFromString(fetcher_return[0]);

        assertFalse("No xml from request connection from fetcher!",doc == null);

        node = doc.getChildNodes().item(0);
        attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned from the server!", attrs.getNamedItem("id").getNodeValue().equals(fetch_client_id));

        doc_two = XMLHandler.loadXMLFromString(fetcher_return[1]);

        assertFalse("No xml msg response for fetcher!",doc_two == null);
        
        node_two = doc_two.getChildNodes().item(0);
        attrs_two = node_two.getAttributes();

        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("Messages"));

        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("message").getNodeValue().equals(msg));
        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("id").getNodeValue().equals(id));
        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("recipient").getNodeValue().equals(fetch_client_id));
        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("sender").getNodeValue().equals(fetch_sender_id));

        for (int i = 0; i < node_two.getChildNodes().getLength(); ++i) {
        	System.out.print(node_two.getChildNodes().item(i).getAttributes().getNamedItem("message") + " "  );
        	System.out.print(node_two.getChildNodes().item(i).getAttributes().getNamedItem("id") + " "       );
        	System.out.print(node_two.getChildNodes().item(i).getAttributes().getNamedItem("recipient") + " ");
        	System.out.println(node_two.getChildNodes().item(i).getAttributes().getNamedItem("sender")       );
        }

        //assertTrue("Something wrong with the message id!", !Pattern.matches("[a-zA-Z]+", attrs_two.getNamedItem("added").getNodeValue()));
        /*for (String s : mamma) {
                System.out.println("meh: " + s);
        }*/
	}
	@Test
	public void testServerFetchComplete() throws IOException {
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
		String[] sender_return = baos.toString().split("\n");
        Document doc = XMLHandler.loadXMLFromString(sender_return[0]);
        if (doc == null) {
                System.out.println("oopsie");
        }
        Node node = doc.getChildNodes().item(0);
        NamedNodeMap attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned from the server!", attrs.getNamedItem("id").getNodeValue().equals(client_id));
        Document doc_two = XMLHandler.loadXMLFromString(sender_return[1]);
        if (doc_two == null) {
                System.out.println("oopsie");
        }
        Node node_two = doc_two.getChildNodes().item(0);
        NamedNodeMap attrs_two = node_two.getAttributes();
        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("message"));
        assertTrue("Something wrong with the message id!", !Pattern.matches("[a-zA-Z]+", attrs_two.getNamedItem("added").getNodeValue()));
		//String recip_id = "0046123123456";
		client_id = "0046666123456";
		//String msg = "U STOOPID";
        req_msg_output = xml_header + "<request id=\"" + client_id + "\"/>";
        add_msg_output = xml_header + "<fetch_messages/>";
        final_output = req_msg_output + "\n" + add_msg_output + "\n";
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();
		//System.out.println(baos.toString());
		String[] fetcher_return = baos.toString().split("\n");
        doc = XMLHandler.loadXMLFromString(fetcher_return[0]);
        if (doc == null) {
                System.out.println("oopsie");
        }
        node = doc.getChildNodes().item(0);
        attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned from the server!", attrs.getNamedItem("id").getNodeValue().equals(client_id));
        doc_two = XMLHandler.loadXMLFromString(fetcher_return[1]);
        if (doc_two == null) {
                System.out.println("oopsie");
        }
        node_two = doc_two.getChildNodes().item(0);
        attrs_two = node_two.getAttributes();
        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("Messages"));
        for (int i = 0; i < node_two.getChildNodes().getLength(); ++i) {
        	System.out.print(node_two.getChildNodes().item(i).getAttributes().getNamedItem("message") + " "  );
        	System.out.print(node_two.getChildNodes().item(i).getAttributes().getNamedItem("id") + " "       );
        	System.out.print(node_two.getChildNodes().item(i).getAttributes().getNamedItem("recipient") + " ");
        	System.out.println(node_two.getChildNodes().item(i).getAttributes().getNamedItem("sender")       );
        }
        //assertTrue("Something wrong with the message id!", !Pattern.matches("[a-zA-Z]+", attrs_two.getNamedItem("added").getNodeValue()));
        /*for (String s : mamma) {
                System.out.println("meh: " + s);
        }*/
	}

	
}
