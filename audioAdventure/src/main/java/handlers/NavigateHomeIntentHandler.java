package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class NavigateHomeIntentHandler implements RequestHandler {
    private static final String INTENT_NAME = "EverythingIntent";
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(INTENT_NAME));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        var reply  = "The case of Henry Deadman remains unsolved and there're alleged " +
                "witness of his ghost wandering on the top of the Clifton Suspension Bridge till " +
                "today.";
        return handlerInput.getResponseBuilder()
                .withShouldEndSession(true)
                .withSpeech(reply)
                .withReprompt("Better luck next time")
                .build();
    }
}
