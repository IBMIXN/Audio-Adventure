package watsonHandler;

import org.junit.Before;
import org.junit.Test;
import util.Config;
import util.Constants;
import util.State;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class WatsonHandlerImplTest {
    private Config config;
    private WatsonHandlerImpl watsonHandler;

    @Before
    public void setup() {
        config = new Config("/config_test_endpoints.xml");
        watsonHandler = new WatsonHandlerImpl(config);
    }

    @Test
    public void testConfigNotNull() {
        assertNotNull(config);
        assertNotNull(config.WATSON_ASSISTANT_ID);
        assertNotNull(config.ALEXA_SKILL_ID);
        assertNotNull(config.WATSON_API_KEY);
        assertNotNull(config.WATSON_SERVICE_URL);
        System.out.println(config.toString());
    }

    @Test
    public void testAssistant() {
        var watsonHandler = new WatsonHandlerImpl(config);
        assertNotNull(watsonHandler);
        assertNotNull(watsonHandler.getAssistant());
    }

    @Test
    public void testNewSession() {
        var sessionID = watsonHandler.createSessionIfNecessary(new HashMap<>(), "");
        assertTrue(sessionID != null && !sessionID.equals(""));
    }

    @Test
    public void testSampleDialog() {
        var res = watsonHandler.getResponseFromWatson("", new HashMap<>());

        assertTrue(res.isPresent());
        assertFalse(res.get().isEmpty());
    }

    @Test
    public void ensureContextVariablesNotNull() {
        Map<String, Object> map = new HashMap<>();
        watsonHandler.getResponseFromWatson("", map);

        assertNotNull(map);
        var stateStr = map.get(Constants.STATE);
        assertNotNull(stateStr);
        assertEquals(stateStr, State.New.toString());
    }

}