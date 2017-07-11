import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.AESUtil;
import util.CryptoUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CryptoUtilTest
{
    @Test
    void testHammingDistance()
    {
        Assertions.assertEquals(37, CryptoUtil.hammingDistance("this is a test", "wokka wokka!!!"));
    }

    @Test
    void testAESInCBCModeManual() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String input = "A really long winded input which we are supposed to encrypt";
        String key = "YELLOW SUBMARINE";
        byte[] iv = new byte[16];
        Arrays.fill( iv, (byte)0);

        byte[] result = AESUtil.encryptAESInCBCModeManual( input.getBytes(), key, iv);

        Assertions.assertEquals(input, AESUtil.decryptAESInCBCModeManual(result, key, iv));
    }
}
