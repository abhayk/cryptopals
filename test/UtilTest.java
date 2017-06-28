import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest
{
    @Test
    void testHammingDistance()
    {
        Assertions.assertEquals(37, Util.hammingDistance("this is a test", "wokka wokka!!!"));
    }
}
