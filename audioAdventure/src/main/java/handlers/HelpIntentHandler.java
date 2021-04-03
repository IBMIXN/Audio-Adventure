package handlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import java.util.Optional;

/**
 * Handles help intent.
 */
public class HelpIntentHandler implements RequestHandler {
  private static final String INTENT_NAME = "AMAZON.HelpIntent";

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName(INTENT_NAME).or(requestType(LaunchRequest.class)));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    var reply =
        "Audio Adventure is a thrilling detective game when you have to investigate a"
            + "mysterious murder happened during the opening of Clifton Suspension Bridge. Pay "
            + "attention to the arguments of the suspect and don't let the murderer escape!!";

    return handlerInput
        .getResponseBuilder()
        .withSpeech(reply)
        .withReprompt("go ahead and start your game")
        .build();
  }
}
