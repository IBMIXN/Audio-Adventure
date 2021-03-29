package handlers;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.interfaces.system.SystemState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import util.Constants;
import util.State;
import watsonHandler.WatsonHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EverythingIntentHandlerTest {
    @Mock
    private WatsonHandler handler;

    @InjectMocks
    private EverythingIntentHandler intentHandler;

    @Test
    @SuppressWarnings("unchecked")
    public void testDialog() {
        var input =  mockedInputWith(true, "test", State.New, new HashMap<>(){{
            put("something", "else");
        }});
        var mapArgumentCaptor =
                ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(handler.getResponseFromWatson(stringArgumentCaptor.capture(),
                mapArgumentCaptor.capture())).thenReturn(Optional.of("Hello world"));

        var optionalResponse = intentHandler.handle(input);
        assertTrue(optionalResponse.isPresent());
        var response = optionalResponse.get().getOutputSpeech().toString();
        assertTrue(!response.isBlank() && !response.isEmpty());
        assertTrue(response.contains("Hello world"));

        verify(handler, times(1)).getResponseFromWatson(any(), any());
        assertEquals("test", stringArgumentCaptor.getValue());
        assertEquals("else", mapArgumentCaptor.getValue().get("something"));
    }

    private HandlerInput mockedInputWith(boolean newSession, String msg, State persistentState,
                                         Map<String, Object> sessionVariables) {
        PersistenceAdapter persistent = mock(PersistenceAdapter.class);
        Map<String, Object> map = new HashMap<>(){{
            put(Constants.STATE, persistentState.toString()); // state saved in the persistent storage
        }};
        var context = Context.builder()
                .withSystem(SystemState.builder()
                        .withUser(User.builder().withUserId("1").build()).build())
                .build();
        var session = Session.builder()
                .withAttributes(sessionVariables)
                .withNew(newSession)    // is it a new session
                .build();
        var requestEnvelope = RequestEnvelope
                .builder()
                .withRequest(IntentRequest
                        .builder()
                        .withIntent(Intent
                                .builder()
                                .putSlotsItem(Constants.SLOT_NAME, Slot
                                        .builder()
                                        .withName(Constants.SLOT_NAME)
                                        .withValue(msg)     // msg
                                        .build())
                                .build())
                        .build())
                .withContext(context)
                .withSession(session)
                .build();
        assertNotNull(session.getAttributes());
        assertNotNull(requestEnvelope.getSession().getAttributes());

        var input =
                HandlerInput.builder().withPersistenceAdapter(persistent)
                        .withRequestEnvelope(requestEnvelope).build();
        when(persistent.getAttributes(any())).thenReturn(java.util.Optional.of(map));

        return input;
    }
}