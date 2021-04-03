import org.junit.Test;
import org.mockito.Mockito;
import util.Config;

import static org.junit.Assert.assertEquals;

public class MainStreamHandlerTest {
    @Test
    public void ensureEntryPoint() throws ClassNotFoundException {
        try (var mock = Mockito.mockStatic(MainStreamHandler.class)) {
            mock.when(MainStreamHandler::getConfig).thenReturn(new Config("/config.xml"));
            Class<?> c = Class.forName("MainStreamHandler");
            var name = c.getPackageName() + '.' + c.getSimpleName();
            assertEquals(".MainStreamHandler", name);
        }
    }
}