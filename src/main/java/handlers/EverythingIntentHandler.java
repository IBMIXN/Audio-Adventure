package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import watsonHandler.WatsonHandler;
import watsonHandler.WatsonHandlerImp;

import java.util.Optional;
import java.util.function.Consumer;

import static com.amazon.ask.request.Predicates.intentName;


public class EverythingIntentHandler implements RequestHandler {
    private static final String CONTEXT = "watsonContext";
    private static final String INTENT_NAME = "EverythingIntent";
    private static final String SLOT_NAME = "EveryThingSlot";
//    private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

    public boolean canHandle(HandlerInput handlerInput) {
        System.out.println(EverythingIntentHandler.class  + " - can handle" + handlerInput.matches(intentName(INTENT_NAME)));
        return handlerInput.matches(intentName(INTENT_NAME));
    }

    public Optional<Response> handle(HandlerInput handlerInput) {
        var msg = getUserMessage(handlerInput);
        var sessionID = getWatsonSessionID(handlerInput);
        var response = getResponseFromWatson(handlerInput, msg, sessionID);
        return handlerInput.getResponseBuilder()
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
        WatsonHandler ws = new WatsonHandlerImp();

        Optional<String> text = ws.assistantMessage(msg, watsonSessionID, setSessionID);
        if (text.isEmpty()) return null;

        return text.get();
    }
}
