import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import handlers.EverythingIntentHandler;
import java.util.HashMap;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;

/** SaveSessionInterceptor saves changes into DynamoDB. */
public class SaveSessionInterceptor
    implements com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

  @Override
  public void process(HandlerInput handlerInput, Optional<Response> optional) {
    if (!handlerInput.matches(intentName(EverythingIntentHandler.INTENT_NAME))) {
      return;
    }

    var sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();

    var sessionState = (String) sessionAttributes.get((Constants.STATE));
    var oldState = (String) sessionAttributes.get(Constants.STATE_OLD);

    // Update persistent storage whenever there's a change
    if (!oldState.equals(sessionState)) {
      handlerInput
          .getAttributesManager()
          .setPersistentAttributes(
              new HashMap<>() {
                {
                  put(Constants.STATE, sessionState);
                }
              });
      handlerInput.getAttributesManager().savePersistentAttributes();

      sessionAttributes.put(Constants.STATE_OLD, sessionState);
      logger.debug("Persistent state updated");
    }
  }
}
