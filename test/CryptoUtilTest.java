import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.CryptoUtil;

public class CryptoUtilTest
{
    @Test
    void testHammingDistance()
    {
        Assertions.assertEquals(37, CryptoUtil.hammingDistance("this is a test", "wokka wokka!!!"));
    }
}
