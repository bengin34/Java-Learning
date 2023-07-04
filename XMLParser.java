import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser {
    
    /*
     * Get the document builder
     * Get document
     * normalize the xml structure
     * Get all elements by tag name
     */


    public static void main(String[] args) {
       // Get the Document Builder

       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       try {
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Get Document
        Document document = builder.pars(new File(pathname:"input.xml"))
       } catch (Exception e) {
        e.printStackTrace();
       }
    }
}
