package watsonHandler;

import java.util.Optional;
import java.util.function.Consumer;

public interface WatsonHandler {
    /**
     * Get a response from Watson API server,
     * note: you should always update sessionID if the server successfully returns a response
     * @param input user's request
     * @param sessionID can be null if it's a new session, you need to require a new sessionID and update it
     * @param updateSessionID update sessionID, call {@link java.util.function.Consumer#accept} with the new ID
     * @return possible Response, empty if an error occurred
     */
    public Optional<String> assistantMessage(String input, String sessionID, Consumer<String> updateSessionID);
}
