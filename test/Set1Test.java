import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Set1Test
{
    @Test
    void challenge1()
    {
        Assertions.assertEquals( "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t",
                Set1.challenge1("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"));
    }

    @Test
    void challenge2()
    {
        Assertions.assertEquals("746865206b696420646f6e277420706c6179",
                Set1.challenge2("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965"));
    }

    @Test
    void challenge3()
    {
        Assertions.assertEquals("Cooking MC's like a pound of bacon",
                Set1.challenge3("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"));
    }

    @Test
    void challenge4() throws IOException
    {
        Assertions.assertEquals("Now that the party is jumping",
                Set1.challenge4("test\\input\\s1c4.txt"));
    }

    @Test
    void challenge5()
    {
        Assertions.assertEquals("0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272" +
                        "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f",
                Set1.challenge5( "Burning 'em, if you ain't quick and nimble\n" +
                        "I go crazy when I hear a cymbal", "ICE" ));
    }

    @Test
    void challenge6() throws IOException
    {
        Assertions.assertEquals("Terminator X: Bring the noise",
                Set1.challenge6("test\\input\\s1c6.txt"));
    }

    @Test
    void challenge7() throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException,
            BadPaddingException, NoSuchPaddingException, InvalidKeyException
    {
        Assertions.assertEquals(true,
                Set1.challenge7("test\\input\\s1c7.txt", "YELLOW SUBMARINE").startsWith("I'm back and I'm ringin' the bell"));
    }

    @Test
    void challenge8() throws IOException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        Assertions.assertEquals( true,
                Set1.challenge8("test\\input\\s1c8.txt").startsWith("d8806197"));
    }
}
