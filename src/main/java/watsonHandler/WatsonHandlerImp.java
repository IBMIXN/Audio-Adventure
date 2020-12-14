package watsonHandler;

import java.util.Optional;
import java.util.function.Consumer;

public class WatsonHandlerImp implements WatsonHandler{

    @Override
    public Optional<String> assistantMessage(String input, String sessionID, Consumer<String> updateSessionID) {
        return Optional.of("not implemented yet");
    }
}
