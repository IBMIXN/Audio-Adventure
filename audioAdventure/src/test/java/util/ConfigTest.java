package util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigTest {
    @Test
    public void testCorrectness() {
        var config = new Config("/config_template.xml");
        assertEquals("a", config.alexaSkillId);
        assertEquals("b", config.watsonApiKey);
        assertEquals("c", config.watsonServiceUrl);
        assertEquals("d", config.watsonAssistantId);
    }

    @Test
    public void loadTestNode() {
        var config = new Config("/config_test_endpoints.xml");
        assertNotNull(config);
    }

}