package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class CancelAndStopIntentHandler implements RequestHandler {
    private static final String INTENT_NAME1 = "AMAZON.CancelIntent";
    private static final String INTENT_NAME2 = "AMAZON.StopIntent";

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(
                    intentName(INTENT_NAME1)
                .or(intentName(INTENT_NAME2)
                .or(requestType(SessionEndedRequest.class))));
    }
    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        var reply = "The case of Henry Deadman remains unsolved and there're alleged " +
                "witness of his ghost wandering on the top of the Clifton Suspension Bridge till " +
                "today.";
        return handlerInput.getResponseBuilder()
                .withSpeech(reply)
                .withShouldEndSession(true)
                .build();
    }
}
