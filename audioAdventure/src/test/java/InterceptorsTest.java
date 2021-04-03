import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.interfaces.system.SystemState;
import org.junit.Before;
import org.junit.Test;
import util.Constants;
import util.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InterceptorsTest {
    private NewSessionInterceptor newSessionInterceptor;
    private SaveSessionInterceptor saveSessionInterceptor;

    @Before
    public void setup() {
        newSessionInterceptor = new NewSessionInterceptor();
        saveSessionInterceptor = new SaveSessionInterceptor();
    }

    @Test
    public void ensureNothingChangesOnNewSession() {
        var input = mockedInputWith(false, "test", State.New, new HashMap<>() {{
            put(Constants.STATE, State.New.toString());
        }});

        newSessionInterceptor.process(input);
        assertFalse(isNewSession(input));
        assertEquals("test", getMessage(input));
        assertEquals(State.New, getStateFromPersistentStorage(input));
        assertEquals(State.New, getStateFromSession(input));
    }

    @Test
    public void onNewSessionAndNewStateEmptyMessage() {
        var input = mockedInputWith(true, "test", State.New, new HashMap<>() {{
            put(Constants.STATE, State.CheckPoint1.toString());
        }});

        newSessionInterceptor.process(input);

        assertTrue(isNewSession(input));
        assertEquals("", getMessage(input));
        assertEquals(State.New, getStateFromPersistentStorage(input));
        assertEquals(State.New, getStateFromSession(input));
    }

    @Test
    public void onNewSessionRestoreOldState() {
        var input = mockedInputWith(true, "test", State.CheckPoint1, new HashMap<>());

        newSessionInterceptor.process(input);

        assertTrue(isNewSession(input));
        assertEquals(State.CheckPoint1.getSecret(), getMessage(input));
        assertEquals(State.CheckPoint1, getStateFromPersistentStorage(input));
        assertEquals(State.CheckPoint1, getStateFromSession(input));
    }

    @Test
    public void onNewSessionRestoreOldStateVariation() {
        var input = mockedInputWith(true, "test", State.CheckPoint2, new HashMap<>());

        newSessionInterceptor.process(input);

        assertTrue(isNewSession(input));
        assertEquals(State.CheckPoint2.getSecret(), getMessage(input));
        assertEquals(State.CheckPoint2, getStateFromPersistentStorage(input));
        assertEquals(State.CheckPoint2, getStateFromSession(input));
    }

    @Test
    public void testOldState() {
        var input = mockedInputWith(true, "test", State.CheckPoint1, new HashMap<>());

        newSessionInterceptor.process(input);

        var s =
                (String) input.getAttributesManager().getSessionAttributes().get(Constants.STATE_OLD);
        assertNotNull(s);
        var oldState = State.valueOf(s);
        assertEquals(State.CheckPoint1, oldState);
    }

    @Test
    public void testOldStateVariation() {
        var input = mockedInputWith(true, "test", State.New, new HashMap<>());

        newSessionInterceptor.process(input);

        var s =
                (String) input.getAttributesManager().getSessionAttributes().get(Constants.STATE_OLD);
        assertNotNull(s);
        var oldState = State.valueOf(s);
        assertEquals(State.New, oldState);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotUpdatePersistentVariables() {
        Map<String, Object> map = mock(HashMap.class);
        when(map.get(Constants.STATE)).thenReturn(State.CheckPoint1.toString());
        when(map.get(Constants.STATE_OLD)).thenReturn(State.CheckPoint1.toString());

        var input = mockedInputWith(false, "test", State.New, map);
        saveSessionInterceptor.process(input, Optional.empty());

        verify(map, never()).put(any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldUpdatePersistentVariables() {
        Map<String, Object> map = mock(HashMap.class);
        when(map.get(Constants.STATE)).thenReturn(State.CheckPoint2.toString());
        when(map.get(Constants.STATE_OLD)).thenReturn(State.CheckPoint1.toString());

        var input = mockedInputWith(false, "test", State.New, map);
        saveSessionInterceptor.process(input, Optional.empty());

        verify(map, times(1)).put(Constants.STATE_OLD, State.CheckPoint2.toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldUpdatePersistentVariablesVariation() {
        Map<String, Object> map = mock(HashMap.class);
        when(map.get(Constants.STATE)).thenReturn(State.CheckPoint2.toString());
        when(map.get(Constants.STATE_OLD)).thenReturn(State.CheckPoint1.toString());

        var input = mockedInputWith(true, "test", State.New, map);
        saveSessionInterceptor.process(input, Optional.empty());

        verify(map, times(1)).put(Constants.STATE_OLD, State.CheckPoint2.toString());
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("unchecked")
    public void shouldThrowException() {
        Map<String, Object> map = mock(HashMap.class);
        when(map.get(Constants.STATE)).thenReturn(State.CheckPoint2.toString());

        var input = mockedInputWith(true, "test", State.New, map);
        saveSessionInterceptor.process(input, Optional.empty());
    }

    private HandlerInput mockedInputWith(boolean newSession, String msg, State persistentState,
                                         Map<String, Object> sessionVariables) {
        PersistenceAdapter persistent = mock(PersistenceAdapter.class);
        Map<String, Object> map = new HashMap<>() {{
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

    private State getStateFromSession(HandlerInput handlerInput) {
        var s =
                (String) handlerInput.getAttributesManager().getSessionAttributes().get(Constants.STATE);

        return s == null ? null : State.valueOf(s);
    }

    private State getStateFromPersistentStorage(HandlerInput handlerInput) {
        var s =
                (String) handlerInput.getAttributesManager().getPersistentAttributes().get(Constants.STATE);
        return s == null ? null : State.valueOf(s);
    }

    private boolean isNewSession(HandlerInput handlerInput) {
        return handlerInput.getRequestEnvelope().getSession().getNew();
    }

    private String getMessage(HandlerInput handlerInput) {
        return ((IntentRequest) handlerInput.getRequestEnvelope().getRequest()).getIntent().getSlots().get(Constants.SLOT_NAME).getValue();
    }

}