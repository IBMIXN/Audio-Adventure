package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class HelpIntentHandler implements RequestHandler {
    private static final String INTENT_NAME = "AMAZON.HelpIntent";
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(INTENT_NAME).or(requestType(LaunchRequest.class)));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech("test - hi you can play an audio adventure game here")
                .withReprompt("go ahead and start your game")
                .build();
    }
}
