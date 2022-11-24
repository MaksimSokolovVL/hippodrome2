import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

class MainTest {

    @Disabled
    @Timeout(22)
    @Test
    void mainTest() throws Exception {
        Main.main(null);
    }
}