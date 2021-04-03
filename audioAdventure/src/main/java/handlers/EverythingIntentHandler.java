package handlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;
import static util.Constants.SLOT_NAME;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watsonhandler.WatsonHandler;

/**
 * EverythingIntentHander handles EverythingIntent.
 *
 * <p>Captures user's utterances and send them to WatsonAssistant Replies with WatsonAssistant's
 * response.
 */
public final class EverythingIntentHandler implements RequestHandler {

  /** Intent name EverythingIntent. */
  public static final String INTENT_NAME = "EverythingIntent";

  private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

  private final WatsonHandler handler;

  public EverythingIntentHandler(final WatsonHandler watsonHandler) {
    this.handler = watsonHandler;
  }

  public boolean canHandle(final HandlerInput handlerInput) {
    return handlerInput.matches(requestType(LaunchRequest.class).or(intentName(INTENT_NAME)));
  }

  /**
   * Handles the input.
   *
   * @param handlerInput user input
   * @return optional response, might be empty in case of an error
   */
  public Optional<Response> handle(final HandlerInput handlerInput) {
    var msg = getUserMessage(handlerInput);
    logger.info("User input " + msg);

    var response = getResponseFromWatson(handlerInput, msg);

    return handlerInput
        .getResponseBuilder()
        .withReprompt("Tell me what do you think")
        .withSpeech(response)
        .build();
  }

  private String getUserMessage(final HandlerInput handlerInput) {
    var request = handlerInput.getRequestEnvelope().getRequest();
    if (request instanceof IntentRequest) {
      var intent = (IntentRequest) request;
      Slot slot = intent.getIntent().getSlots().get(SLOT_NAME);
      if (slot == null || slot.getValue() == null) {
        logger.warn("Empty Slot value");
        return null;
      }
      return slot.getValue();
    } else {
      return "";
    }
  }

  private String getResponseFromWatson(final HandlerInput handlerInput, final String msg) {
    var sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
    Optional<String> text = handler.getResponseFromWatson(msg, sessionAttributes);

    if (text.isEmpty()) {
      logger.warn("Empty watson response");
      return null;
    }
    return text.get();
  }
}
