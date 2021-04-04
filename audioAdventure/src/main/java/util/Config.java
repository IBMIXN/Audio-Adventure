package util;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Config contains various api keys for Alexa & Watson service requests. Change your configuration
 * in the `config.xml` file under the `resource` folder To add a new field, you can add one in the
 * `config.xml` file then add and initialize a new attribute in the class
 */
public final class Config {
  public final String alexaSkillId;
  public final String watsonApiKey;
  public final String watsonServiceUrl;
  public final String watsonAssistantId;

  /**
   * Construct config from `filename.xml`.
   *
   * @param fileName filename under resource folder
   */
  public Config(final String fileName) {
    var document = parsexml(loadResource(fileName));
    this.alexaSkillId = getValue("alexa-skill-id", document);
    this.watsonApiKey = getValue("watson-api-key", document);
    this.watsonServiceUrl = getValue("watson-service-url", document);
    this.watsonAssistantId = getValue("watson-assistant-id", document);
  }

  /**
   * Obtains the correct configuration file to run from the environment.
   *
   * @return Config
   */
  public static Config getConfigFromEnvironment() {
    String configFile = System.getenv(Constants.ENV_CONFIG);
    assert (configFile != null);

    return new Config(configFile);
  }

  /**
   * loadResource loads a file and returns it as an inputStream.
   *
   * @param fileName file path
   * @return InputStream
   */
  private InputStream loadResource(final String fileName) {
    return Config.class.getResourceAsStream(fileName);
  }

  /**
   * parseXML parses an InputStream and returns a Document.
   *
   * @param in input Stream
   * @return document
   */
  private Document parsexml(final InputStream in) {
    try {
      var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      return builder.parse(in);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * getValue reads a DOM node from the given document.
   *
   * @param tag name of the node
   * @param document xml document
   * @return trimmed value of the given tag
   */
  private String getValue(final String tag, final Document document) {
    XPath path = XPathFactory.newInstance().newXPath();
    String str = null;
    try {
      str = (String) path.evaluate("/Configuration/" + tag, document, XPathConstants.STRING);
      str = str.strip();
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return str;
  }

  @Override
  public String toString() {
    return "Config{"
        + "ALEXA_SKILL_ID='"
        + alexaSkillId
        + '\''
        + ", WATSON_API_KEY='"
        + watsonApiKey
        + '\''
        + ", WATSON_SERVICE_URL='"
        + watsonServiceUrl
        + '\''
        + ", WATSON_ASSISTANT_ID='"
        + watsonAssistantId
        + '\''
        + '}';
  }
}
