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

        doc = XMLHandler.loadXMLFromString(fetcher_return[0]);

        assertFalse("No xml from request connection from fetcher!",doc == null);

        node = doc.getChildNodes().item(0);
        attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned correctly from the server!", attrs.getNamedItem("id").getNodeValue().equals(fetch_client_id));

        doc_two = XMLHandler.loadXMLFromString(fetcher_return[1]);

        assertFalse("No xml msg response for fetcher!",doc_two == null);
        
        node_two = doc_two.getChildNodes().item(0);
        attrs_two = node_two.getAttributes();

        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("Messages"));

        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("message").getNodeValue().equals(msg));
        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("id").getNodeValue().equals(id));
        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("recipient").getNodeValue().equals(fetch_client_id));
        assertTrue("",node_two.getChildNodes().item(0).getAttributes().getNamedItem("sender").getNodeValue().equals(fetch_sender_id));
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
        String fetchcomplete_msg_output = xml_header + "<fetch_complete/>";
        final_output = req_msg_output + "\n" + fetch_msg_output + "\n" + fetchcomplete_msg_output + "\n";
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();

		String[] fetcher_return = baos.toString().split("\n");

        doc = XMLHandler.loadXMLFromString(fetcher_return[0]);

        assertFalse("No xml from request connection from fetcher!",doc == null);

        node = doc.getChildNodes().item(0);
        attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned correctly from the server!", attrs.getNamedItem("id").getNodeValue().equals(fetch_client_id));

        doc_two = XMLHandler.loadXMLFromString(fetcher_return[1]);

        assertFalse("No xml msg response for fetcher!",doc_two == null);
        
        node_two = doc_two.getChildNodes().item(0);
        attrs_two = node_two.getAttributes();

        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("Messages"));

        assertTrue("Wrong msg returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("message").getNodeValue().equals(msg));
        assertTrue("Wrong id returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("id").getNodeValue().equals(id));
        assertTrue("Wrong recipient returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("recipient").getNodeValue().equals(fetch_client_id));
        assertTrue("Wrong sender returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("sender").getNodeValue().equals(fetch_sender_id));

        Document doc_three = XMLHandler.loadXMLFromString(fetcher_return[2]);

        assertFalse("No xml msg response for fetcher!",doc_three == null);
        
        Node node_three = doc_three.getChildNodes().item(0);

        assertTrue("accept not returned from the server!", node_three.getNodeName().equals("fetch_complete_ack"));
	}

	@Test
	public void testServerDelete() throws IOException {
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

		String fetch_client_id = "0046666123456";

        req_msg_output = xml_header + "<request id=\"" + fetch_client_id + "\"/>";
        String fetch_msg_output = xml_header + "<delete_message id=\"" + id +"\"/>";
        final_output = req_msg_output + "\n" + fetch_msg_output + "\n";
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(baos);
		server_handler.run();

		String[] fetcher_return = baos.toString().split("\n");

        doc = XMLHandler.loadXMLFromString(fetcher_return[0]);

        assertFalse("No xml from request connection from fetcher!",doc == null);

        node = doc.getChildNodes().item(0);
        attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned correctly from the server!", attrs.getNamedItem("id").getNodeValue().equals(fetch_client_id));

        doc_two = XMLHandler.loadXMLFromString(fetcher_return[1]);

        assertFalse("No xml msg response for fetcher!",doc_two == null);
        
        node_two = doc_two.getChildNodes().item(0);
        attrs_two = node_two.getAttributes();

        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("message"));

        assertTrue("",attrs_two.getNamedItem("deleted").getNodeValue().equals(id));
	}

	@Test
	public void testServerReplaceMsg() throws IOException {
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
        
		String replace_msg = "U SMARTIZ... NOT!";
        String replace_req = xml_header + "<replace_message id=\"" + id + "\" content=\""+ replace_msg +"\"/>";
        final_output = req_msg_output + "\n" + replace_req;
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(final_output.getBytes()));
        ByteArrayOutputStream del_baos = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(del_baos);
		server_handler.run();

		String[] delete_return = del_baos.toString().split("\n");
        Document del_doc = XMLHandler.loadXMLFromString(delete_return[0]);

        assertFalse("No response from request connection!",doc == null);

        Node del_node = del_doc.getChildNodes().item(0);
        NamedNodeMap del_attrs = node.getAttributes();

        assertTrue("accept not returned from the server!", del_node.getNodeName().equals("accept"));
        assertTrue("accept not correct from the server!", del_attrs.getNamedItem("id").getNodeValue().equals(client_id));

        Document del_doc_2 = XMLHandler.loadXMLFromString(delete_return[1]);

        assertFalse("No response from add message connection!",del_doc_2 == null);
        
        Node del_node_2 = del_doc_2.getChildNodes().item(0);
        NamedNodeMap del_attrs_2 = del_node_2.getAttributes();
        
        //String id = attrs_two.getNamedItem("added").getNodeValue();

        assertTrue("accept not returned from the server!", del_node_2.getNodeName().equals("message"));
        assertTrue("Something wrong with the message id!", del_attrs_2.getNamedItem("replaced").getNodeValue().equals(id));

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

        doc = XMLHandler.loadXMLFromString(fetcher_return[0]);

        assertFalse("No xml from request connection from fetcher!",doc == null);

        node = doc.getChildNodes().item(0);
        attrs = node.getAttributes();
        assertTrue("accept not returned from the server!", node.getNodeName().equals("accept"));
        assertTrue("accept not returned correctly from the server!", attrs.getNamedItem("id").getNodeValue().equals(fetch_client_id));

        doc_two = XMLHandler.loadXMLFromString(fetcher_return[1]);

        assertFalse("No xml msg response for fetcher!",doc_two == null);
        
        node_two = doc_two.getChildNodes().item(0);
        attrs_two = node_two.getAttributes();

        assertTrue("accept not returned from the server!", node_two.getNodeName().equals("Messages"));

        assertTrue("Wrong msg returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("message").getNodeValue().equals(replace_msg));
        assertTrue("Wrong id returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("id").getNodeValue().equals(id));
        assertTrue("Wrong recipient returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("recipient").getNodeValue().equals(fetch_client_id));
        assertTrue("Wrong sender returned in msg!",node_two.getChildNodes().item(0).getAttributes().getNamedItem("sender").getNodeValue().equals(fetch_sender_id));
	}

	
}
