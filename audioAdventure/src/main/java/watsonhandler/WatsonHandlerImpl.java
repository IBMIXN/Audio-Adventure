package watsonhandler;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageInputOptions;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import handlers.EverythingIntentHandler;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Config;
import util.Constants;
import util.State;

/** A WatsonHandler Implementation. */
public class WatsonHandlerImpl implements WatsonHandler {
  private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);
  private final Config config;
  private Assistant assistant;

  public WatsonHandlerImpl(Config config) {
    this.config = config;
    getAssistant();
  }

  @Override
  public Optional<String> getResponseFromWatson(
      String input, Map<String, Object> sessionAttributes) {
    var id = (String) sessionAttributes.get(Constants.SESSION_ID);
    id = createSessionIfNecessary(sessionAttributes, id);

    try {
      // Invoke a Watson Assistant method
      var options = buildMessages(id, input);

      MessageResponse messageResponse = assistant.message(options).execute().getResult();
      String response = parseResponse(messageResponse);
      logger.info(response);
      updateSessionAttributes(messageResponse, sessionAttributes);

      return Optional.of(response);
    } catch (NotFoundException e) {
      // Handle Not Found (404) exception
      logger.warn("Handle Not Found (404) exception" + e.getMessage());
      return Optional.empty();
    } catch (RequestTooLargeException e) {
      // Handle Request Too Large (413) exception
      logger.warn("Handle Request Too Large (413) exception" + e.getMessage());
      return Optional.empty();
    } catch (ServiceResponseException e) {
      // Base class for all exceptions caused by error responses from the service
      logger.warn("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Get the assistant.
   * <p></p>
   * for testing purpose only
   *
   * @return Assistant
   */
  public Assistant getAssistant() {
    if (this.assistant != null) {
      return this.assistant;
    }

    IamAuthenticator authenticator = new IamAuthenticator(config.watsonApiKey);
    Assistant assistant = new Assistant("2020-09-24", authenticator);
    assistant.setServiceUrl(config.watsonServiceUrl);

    this.assistant = assistant;
    return assistant;
  }

  /**
   * Creats a new session.
   *
   * @param sessionAttributes session variables
   * @param id session id
   *
   * @return new/old session id
   */
  public String createSessionIfNecessary(Map<String, Object> sessionAttributes, String id) {
    if (id == null || id.isEmpty()) {
      CreateSessionOptions options =
          new CreateSessionOptions.Builder(config.watsonAssistantId).build();
      SessionResponse sessionResponse = assistant.createSession(options).execute().getResult();
      id = sessionResponse.getSessionId();
      logger.info("Generated new session with id:" + id);

      sessionAttributes.put(Constants.SESSION_ID, id);
    }

    return id;
  }

  private MessageOptions buildMessages(String id, String input) {
    MessageInputOptions inputOptions =
        new MessageInputOptions.Builder().returnContext(true).build();
    MessageInput inputAssistant =
        new MessageInput.Builder().messageType("text").text(input).options(inputOptions).build();

    return new MessageOptions.Builder(config.watsonAssistantId, id)
        .input(inputAssistant)
        .build();
  }

  private String parseResponse(MessageResponse messageResponse) {
    List<RuntimeResponseGeneric> responseList = messageResponse.getOutput().getGeneric();
    StringBuilder responseString = new StringBuilder();

    for (RuntimeResponseGeneric str : responseList) {
      responseString.append(str.text());
    }

    return responseString.toString();
  }

  /**
   * Updates State attributes.
   *
   * @param messageResponse Watson Assistant's response
   * @param sessionAttributes sessionAttributes
   */
  public void updateSessionAttributes(
      MessageResponse messageResponse, Map<String, Object> sessionAttributes) {
    var skills = messageResponse.getContext().skills();
    // Watson API by default use "main skill"
    var contextVariables = skills.get("main skill").userDefined();
    if (contextVariables != null) { // not defined, when entered from the control nodes
      var newStateStr =
          (String) contextVariables.getOrDefault(Constants.STATE, State.New.toString());
      sessionAttributes.put(Constants.STATE, newStateStr);
    }
  }
}
