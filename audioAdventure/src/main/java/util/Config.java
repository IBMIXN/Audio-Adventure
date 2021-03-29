package util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.ExportException;

/**
 * Config contains various api keys for Alexa & Watson service requests
 * Change your configuration in the `config.xml` file under the `resource` folder
 *
 * To add a new field, you can add one in the `config.xml` file then add and initialize
 * a new attribute in the class
 */
public class Config {
    public final String ALEXA_SKILL_ID;
    public final String WATSON_API_KEY;
    public final String WATSON_SERVICE_URL;
    public final String WATSON_ASSISTANT_ID;

    public Config(String fileName) {
        var document = parseXML(loadResource(fileName));
        this.ALEXA_SKILL_ID = getValue("alexa-skill-id", document);
        this.WATSON_API_KEY = getValue("watson-api-key", document);
        this.WATSON_SERVICE_URL = getValue("watson-service-url", document);
        this.WATSON_ASSISTANT_ID = getValue("watson-assistant-id", document);
    }

    /**
     * loadResource loads a file and returns it as an inputStream
     * @param fileName file path
     * @return InputStream
     */
    private InputStream loadResource(String fileName) {
        return Config.class.getResourceAsStream(fileName);
    }

    /**
     * parseXML parses an InputStream and returns a Document
     * @param in input Stream
     * @return document
     */
    private Document parseXML(InputStream in) {
        try {
            var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(in);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getValue reads a DOM node from the given document
     * @param tag name of the node
     * @param document xml document
     * @return trimmed value of the given tag
     */
    private String getValue(String tag, Document document) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String str = null;
        try {
            str = (String) xPath.evaluate("/Configuration/" + tag, document, XPathConstants.STRING);
            str = str.strip();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public String toString() {
        return "Config{" +
                "ALEXA_SKILL_ID='" + ALEXA_SKILL_ID + '\'' +
                ", WATSON_API_KEY='" + WATSON_API_KEY + '\'' +
                ", WATSON_SERVICE_URL='" + WATSON_SERVICE_URL + '\'' +
                ", WATSON_ASSISTANT_ID='" + WATSON_ASSISTANT_ID + '\'' +
                '}';
    }
}
