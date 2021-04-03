package handlers;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import java.util.Optional;

/**
 * Handles Fallback intent, especially unexpected utterances.
 */
public class FallbackIntentHandler implements RequestHandler {
  private static final String INTENT_NAME = "AMAZON.FallbackIntent";

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName(INTENT_NAME));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    return handlerInput
        .getResponseBuilder()
        .withSpeech("Would you mind repeating that?")
        .withReprompt("Would you mind repeating that?")
        .build();
  }
}
