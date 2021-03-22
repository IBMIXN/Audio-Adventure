import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import handlers.EverythingIntentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.State;

import static util.Constants.*;

/**
 * Intercepts requests on new sessions
 * Initialize or retrieve old data from the DynamoDB
 */
public class NewSessionInterceptor implements com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

    @Override
    public void process(HandlerInput handlerInput) {
        // On a new session, create/restore data from DynamoDB
        logger.info("New Session created");
        if (handlerInput.getRequestEnvelope().getSession().getNew()) {
            var attributeManager = handlerInput.getAttributesManager();
            var attributes = attributeManager.getPersistentAttributes();

            String message = "";

            if (attributes.get(STATE) == null) {
                attributes.put(STATE, State.New.toString());
            } else {
                var curState = State.valueOf((String) attributes.get(STATE));
                if (curState != State.New) // If CheckPoint1/2 are set, send special tests
                    message = curState.getSecret();
            }

            attributes.put(STATE_OLD, attributes.get(STATE));
            var intent = (IntentRequest)handlerInput.getRequestEnvelope().getRequest();
            var slot = Slot.builder()
                    .withName(SLOT_NAME)
                    .withValue(message)
                    .build();
            intent.getIntent().getSlots().put(SLOT_NAME, slot);

            attributeManager.setSessionAttributes(attributes);
        }
    }
}
