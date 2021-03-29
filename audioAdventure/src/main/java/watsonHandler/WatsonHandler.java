package watsonHandler;

import java.util.Map;
import java.util.Optional;

public interface WatsonHandler {
    /**
     * Get a response from Watson API server,
     * @param msg user's request
     * @param  sessionAttributes, session attributes that needs to be tracked
     * @return possible Response, empty if an error occurred
     */
    Optional<String> getResponseFromWatson(String msg, Map<String, Object> sessionAttributes);

}
