package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest {
    @Test
    public void testCorrectness() {
        var config = new Config("/config_template.xml");
        assertEquals("a", config.ALEXA_SKILL_ID);
        assertEquals("b", config.WATSON_API_KEY);
        assertEquals("c", config.WATSON_SERVICE_URL);
        assertEquals("d", config.WATSON_ASSISTANT_ID);
    }
}