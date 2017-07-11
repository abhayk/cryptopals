import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Set2Test
{
    @Test
    void challenge9()
    {
        String input = "YELLOW SUBMARINE";

        byte padByte = (byte)4;

        String result = Set2.challenge9(input, 20, padByte);

        Assertions.assertEquals(20, result.length());

        for(int i= input.length(); i< result.length(); i++)
        {
            Assertions.assertEquals(padByte, result.charAt(i));
        }
    }

    @Test
    void challenge10() throws BadPaddingException, NoSuchAlgorithmException, IOException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException
    {
        byte[] iv = new byte[16];
        Arrays.fill( iv, (byte)0);

        Assertions.assertEquals( true,
                Set2.challenge10("test\\input\\s2c2.txt", "YELLOW SUBMARINE", iv).startsWith("I'm back and I'm ringin' the bell"));
    }
}
