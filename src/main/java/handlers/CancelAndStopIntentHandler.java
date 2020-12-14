package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CancelAndStopIntentHandler implements RequestHandler {
    private static final String INTENT_NAME1 = "AMAZON.CancelIntent";
    private static final String INTENT_NAME2 = "AMAZON.StopIntent";


    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(INTENT_NAME1).or(intentName(INTENT_NAME2)));
    }
    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech("test - not implemented")
                .withReprompt("test - not implemented")
                .build();
    }
}
