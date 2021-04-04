import static util.Constants.SLOT_NAME;
import static util.Constants.STATE;
import static util.Constants.STATE_OLD;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import handlers.EverythingIntentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.State;

/** Intercepts requests on new sessions Initialize or retrieve old data from the DynamoDB. */
public class NewSessionInterceptor
    implements com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

  @Override
  public void process(HandlerInput handlerInput) {
    // On a new session, create/restore data from DynamoDB
    logger.info("NewSessionInterceptor Called");
    if (handlerInput.getRequestEnvelope().getSession().getNew()) {
      var attributeManager = handlerInput.getAttributesManager();
      var attributes = attributeManager.getPersistentAttributes();

      String message = "";

      if (attributes.get(STATE) == null) {
        attributes.put(STATE, State.New.toString());
      } else {
        var curState = State.valueOf((String) attributes.get(STATE));
        if (curState != State.New) { // If CheckPoint1/2 are set, send special tests
          message = curState.getSecret();
        }
      }

      attributes.put(STATE_OLD, attributes.get(STATE));
      var request = handlerInput.getRequestEnvelope().getRequest();
      if (request instanceof IntentRequest) {
        var intent = (IntentRequest) request;
        var slot = Slot.builder().withName(SLOT_NAME).withValue(message).build();
        intent.getIntent().getSlots().put(SLOT_NAME, slot);
      }
      attributeManager.setSessionAttributes(attributes);
    }
  }
}
