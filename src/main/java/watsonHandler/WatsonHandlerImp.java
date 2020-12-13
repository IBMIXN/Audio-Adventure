package watsonHandler;

import java.util.Optional;
import java.util.function.Consumer;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import com.ibm.watson.assistant.v2.model.MessageResponse;



public class WatsonHandlerImp implements WatsonHandler{

    @Override
    public Optional<String> assistantMessage(String input, String sessionID, Consumer<String> updateSessionID) {
        IamAuthenticator  authenticator = new IamAuthenticator ("{iam_api_key}");
        Assistant assistant = new Assistant("2020-09-24", authenticator);
        assistant.setServiceUrl("<url>");

        if (sessionID.isEmpty()){
            //request new session

        }

        try {
            // Invoke a Watson Assistant method
            MessageInput inputAssistant = new MessageInput.Builder()
                    .messageType("text")
                    .text(input)
                    .build();
            MessageOptions options = new MessageOptions.Builder("{assistant_id}", "{session_id}")
                    .input(inputAssistant)
                    .build();
            MessageResponse response = assistant.message(options).execute().getResult();
            String responseString = response.getOutput().getGeneric().get(0).text();
            updateSessionID.accept(sessionID);
            return Optional.of(responseString);
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
