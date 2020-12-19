package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Config;
import watsonHandler.WatsonHandler;
import watsonHandler.WatsonHandlerImpl;

import java.util.Optional;
import java.util.function.Consumer;

import static com.amazon.ask.request.Predicates.intentName;


public class EverythingIntentHandler implements RequestHandler {
    private static final String CONTEXT = "watsonContext";
    private static final String INTENT_NAME = "EverythingIntent";
    private static final String SLOT_NAME = "EverythingSlot";
    private final Config config;

    private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

    public EverythingIntentHandler(Config config) {
        this.config = config;
    }

    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(INTENT_NAME));
    }

    public Optional<Response> handle(HandlerInput handlerInput) {
        var msg = getUserMessage(handlerInput);
        logger.info("msg received: " + msg);
        var sessionID = getWatsonSessionID(handlerInput);

        // in case of a new session, sending an empty msg instead
        if (sessionID == null) msg = "";

        var response = getResponseFromWatson(handlerInput, msg, sessionID);
        return handlerInput.getResponseBuilder()
                .withReprompt("Tell me what do you think")
                .withSpeech(response)
                .build();
    }

    private String getUserMessage(HandlerInput handlerInput) {
        var intent = (IntentRequest)handlerInput.getRequestEnvelope().getRequest();
        Slot slot = intent.getIntent().getSlots().get(SLOT_NAME);
        if (slot == null || slot.getValue() == null) return null; //TODO: properly handle the bugs
        return slot.getValue();
    }

    private String getWatsonSessionID(HandlerInput handlerInput) {
        return  (String)handlerInput.getAttributesManager().getSessionAttributes().get(CONTEXT);
    }


    private String getResponseFromWatson(HandlerInput handlerInput, String msg, String watsonSessionID) {
        Consumer<String> setSessionID = (id) -> handlerInput.getAttributesManager().getSessionAttributes().put(CONTEXT, id);

        WatsonHandler ws = new WatsonHandlerImpl(config);
        Optional<String> text = ws.assistantMessage(msg, watsonSessionID, setSessionID);

        if (text.isEmpty()) return null;
        return text.get();
    }
}
