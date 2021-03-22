package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Config;
import watsonHandler.WatsonHandler;
import watsonHandler.WatsonHandlerImpl;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;
import static util.Constants.SLOT_NAME;


public class EverythingIntentHandler implements RequestHandler {

    private static final String INTENT_NAME = "EverythingIntent";
    private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

    private final WatsonHandler handler;

    public EverythingIntentHandler(WatsonHandler watsonHandler) {
        this.handler = watsonHandler;
    }

    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(INTENT_NAME).or(requestType(LaunchRequest.class)));
    }

    public Optional<Response> handle(HandlerInput handlerInput) {
        var msg = getUserMessage(handlerInput);
        logger.info("User input " + msg);

        var response = getResponseFromWatson(handlerInput, msg);

        return handlerInput.getResponseBuilder()
                .withReprompt("Tell me what do you think")
                .withSpeech(response)
                .build();
    }



    private String getUserMessage(HandlerInput handlerInput) {
        var intent = (IntentRequest)handlerInput.getRequestEnvelope().getRequest();
        Slot slot = intent.getIntent().getSlots().get(SLOT_NAME);
        if (slot == null || slot.getValue() == null) {
            logger.warn("Empty Slot value");
            return null;
        }
        return slot.getValue();
    }

    private String getResponseFromWatson(HandlerInput handlerInput, String msg) {
        var sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
        Optional<String> text = handler.getResponseFromWatson(msg, sessionAttributes);

        if (text.isEmpty()) {
            logger.warn("Empty watson response");
            return null;
        }
        return text.get();
    }
}
