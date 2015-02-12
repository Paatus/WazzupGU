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

	DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder icBuilder;
    Document doc;
	
    public XMLHandler()
    {
    	try
    	{
    		icBuilder = icFactory.newDocumentBuilder();
    		doc = icBuilder.newDocument();
            Element rootElem = doc.createElement("Messages");
            doc.appendChild(rootElem);
    	} catch(Exception e){};
    }
    
    public void AddMessage(Message m)
    {
    	try {
    		Element rootElem = doc.getDocumentElement();
            // append child elements to root element
            rootElem.appendChild(birth(doc, m));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // creates children
    private Node birth(Document doc, Message m)
    {
    	Element message = doc.createElement("msg");
    	message.setAttribute("ID", ""+m.getID());
    	message.setAttribute("sender", m.getSender());
    	message.setAttribute("recipient", m.getRecipient());
    	message.setAttribute("message", m.getMessage());
    	return message;
    }
    
    public String getDocument()
    {
    	try {
    		TransformerFactory tf = TransformerFactory.newInstance();
    		Transformer transformer = tf.newTransformer();
    		StringWriter writer = new StringWriter();
    		transformer.transform(new DOMSource(doc), new StreamResult(writer));
    		return writer.getBuffer().toString().replaceAll("\n|\r", "");
    	} catch(Exception e) {
    		return e.getMessage();
    	}
    }
    
}
