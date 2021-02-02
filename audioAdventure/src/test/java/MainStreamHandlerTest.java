import com.sun.tools.javac.Main;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainStreamHandlerTest {
    @Test
    public void ensureEntryPoint() throws ClassNotFoundException {
        Class<?> c = Class.forName("MainStreamHandler");
        var name = c.getPackageName() + '.' + c.getSimpleName();
        assertEquals(".MainStreamHandler", name);
    }
}