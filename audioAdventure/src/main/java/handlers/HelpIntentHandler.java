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
        var reply = "Here is a number <w role=\"amazon:VBD\">read</w>" +
                "    as a cardinal number:" +
                "    <say-as interpret-as=\"cardinal\">12345</say-as>." +
                "    Here is a word spelled out:" +
                "    <say-as interpret-as=\"spell-out\">hello</say-as>. ";

        return handlerInput.getResponseBuilder()
                .withSpeech(reply)
                .withReprompt("go ahead and start your game")
                .build();
    }
}
