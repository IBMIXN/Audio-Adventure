package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.request.Predicates;
import watsonHandler.WatsonHandler;
import watsonHandler.WatsonHandlerImp;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.function.Consumer;


public class EverythingIntentHandler implements RequestHandler {
    private static final String CONTEXT = "watsonContext";
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(Predicates.intentName("EverythingIntent"));
    }

    public Optional<Response> handle(HandlerInput handlerInput) {
        var intent = (IntentRequest)handlerInput.getRequestEnvelope().getRequest();
        Slot slot = intent.getIntent().getSlots().get("EveryThingSlot");
        if (slot == null || slot.getValue() == null) return Optional.empty(); //TODO: properly handle the bugs
        String str = slot.getValue();
        String watsonSessionID = (String)handlerInput.getAttributesManager().getSessionAttributes().get(CONTEXT);
        Consumer<String> setSessonID = (id) -> handlerInput.getAttributesManager().getSessionAttributes().put(CONTEXT, id);
        WatsonHandler ws = new WatsonHandlerImp();

        Optional<String> text = ws.assistantMessage(str, watsonSessionID, setSessonID);
        if (text.isEmpty()) return Optional.empty();
        return handlerInput.getResponseBuilder()
                .withSpeech(text.get())
                .build();
    }
}
