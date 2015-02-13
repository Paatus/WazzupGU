package Server;

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

public class XMLHandler {
	
	// initialize Builder stuff
	DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder icBuilder;
    Document doc;
	
    // constructor, initializes
    public XMLHandler()
    {
    	try
    	{
    		// create a new document and add a root element
    		icBuilder = icFactory.newDocumentBuilder();
    		doc = icBuilder.newDocument();
            Element rootElem = doc.createElement("Messages");
            doc.appendChild(rootElem);
    	} catch(Exception e){};
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
    	message.setAttribute("ID", ""+m.getID());
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
    		return e.getMessage();
    	}
    }
    
}
