import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitTest {
    @Test
    public void testFileCorrect() { assertEquals("True", MainController.checkFile("sampleData.txt"));}
    @Test
    public void testFileIncorrect() { assertEquals("True", MainController.checkFile("random.txt"));}
    @Test
    public void testFileError() { assertEquals(1, MainController.checkFile("random.txt"));}

}
