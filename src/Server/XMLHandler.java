package Server;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class XMLHandler {
	
	// initialize Builder stuff
	private DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder icBuilder;
    private Document doc;
	
    // constructor, initializes
    public XMLHandler()
    {
    	this("Messages");
    }
    
    public XMLHandler(String rootTitle)
    {
    	try
    	{
    		// create a new document and add a root element
    		icBuilder = icFactory.newDocumentBuilder();
    		doc = icBuilder.newDocument();
            Element rootElem = doc.createElement(rootTitle);
            doc.appendChild(rootElem);
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    // adds a message, this is done once for every message in the fetch-function
    public void AddMessage(Message m)
    {
    	try {
    		// get the root element(<Messages>)
    		Element rootElem = doc.getDocumentElement();
            // append child elements to root element
            rootElem.appendChild(birth(doc, m));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // adds the <msg> tag to the document
    // creates children
    private Node birth(Document doc, Message m)
    {
    	// create a new element
    	Element message = doc.createElement("msg");
    	// add all the parts
    	message.setAttribute("id", ""+m.getID());
    	message.setAttribute("sender", m.getSender());
    	message.setAttribute("recipient", m.getRecipient());
    	message.setAttribute("message", m.getMessage());
    	// return it
    	return message;
    }
    
    // converts the xml to a string, which will be sent to the client in later parts
    public String getDocument()
    {
    	try {
    		// Java stuff, not important
    		TransformerFactory tf = TransformerFactory.newInstance();
    		Transformer transformer = tf.newTransformer();
    		StringWriter writer = new StringWriter();
    		transformer.transform(new DOMSource(doc), new StreamResult(writer));
    		// replaces all newlines with nothing, making it all be in one line
    		return writer.getBuffer().toString().replaceAll("\n|\r", "");
    	} catch(Exception e) {
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }
    
    public static String getDocument(Document d) {
    	try {
    		// Java stuff, not important
    		TransformerFactory tf = TransformerFactory.newInstance();
    		Transformer transformer = tf.newTransformer();
    		StringWriter writer = new StringWriter();
    		transformer.transform(new DOMSource(d), new StreamResult(writer));
    		// replaces all newlines with nothing, making it all be in one line
    		return writer.getBuffer().toString().replaceAll("\n|\r", "");
    	} catch(Exception e) {
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }
    
    public Document getDoc() {
    	return doc;
    }
    
    public void addAttribute(String name, String value)
    {
    	try {
    		Element rootElem = doc.getDocumentElement();
    		rootElem.setAttribute(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Document loadXMLFromString(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try 
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            //e.printStackTrace();  
        } 
        return null;
    }
    
}
