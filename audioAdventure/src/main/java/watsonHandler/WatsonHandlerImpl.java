package watsonHandler;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import util.Config;


public class WatsonHandlerImpl implements WatsonHandler{
    private final Config config;

    public WatsonHandlerImpl(Config config) {
        this.config = config;
    }

    @Override
    public Optional<String> assistantMessage(String input, String sessionID, Consumer<String> updateSessionID) {
        IamAuthenticator  authenticator = new IamAuthenticator (config.WATSON_API_KEY);
        Assistant assistant = new Assistant("2020-09-24", authenticator);
        assistant.setServiceUrl(config.WATSON_SERVICE_URL);

        if (sessionID == null || sessionID.isEmpty()) {
            CreateSessionOptions options = new CreateSessionOptions.Builder(config.WATSON_ASSISTANT_ID).build();
            SessionResponse sessionResponse = assistant.createSession(options).execute().getResult();
            sessionID = sessionResponse.getSessionId();
            System.out.println("Generated new session with id:" + sessionID);
        }

        try {
            // Invoke a Watson Assistant method
            MessageInputOptions inputOptions = new MessageInputOptions.Builder()
                    .returnContext(true)
                    .build();
            MessageInput inputAssistant = new MessageInput.Builder()
                    .messageType("text")
                    .text(input)
                    .options(inputOptions)
                    .build();

            MessageOptions options = new MessageOptions.Builder(config.WATSON_ASSISTANT_ID, sessionID)
                    .input(inputAssistant)
                    .build();

            MessageResponse messageResponse = assistant.message(options).execute().getResult();
            List<RuntimeResponseGeneric> responseList = messageResponse.getOutput().getGeneric();
            StringBuilder responseString = new StringBuilder();
            for (RuntimeResponseGeneric str: responseList) {
                responseString.append(str.text());
            }

            String newSessionID = messageResponse.getContext().global().sessionId();
            System.out.println("New Session id: " + newSessionID);
            updateSessionID.accept(newSessionID);
            return Optional.of(responseString.toString());
        } catch (NotFoundException e) {
            // Handle Not Found (404) exception
            System.out.println("Handle Not Found (404) exception" + e.getMessage());
            return Optional.empty();
        } catch (RequestTooLargeException e) {
            // Handle Request Too Large (413) exception
            System.out.println("Handle Request Too Large (413) exception" + e.getMessage());
            return Optional.empty();
        } catch (ServiceResponseException e) {
            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned status code "
                    + e.getStatusCode() + ": " + e.getMessage());
            return Optional.empty();
        }

    }


}
