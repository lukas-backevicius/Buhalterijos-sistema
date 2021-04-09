import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitTest {
    MainController mc = new MainController();
    @Test
    public void testDatabasePass() { assertEquals(mc.connectDatabase(), "buhalterija");}
    @Test
    public void testDatabaseFail() { assertEquals(mc.connectDatabase(), "duombaze");}
    @Test
    public void testDatabaseError() { assertEquals(mc.connectDatabase(), 3);}
}
