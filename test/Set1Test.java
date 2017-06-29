import objects.Candidate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Set1Test
{
    @Test
    void c1()
    {
        Assertions.assertEquals( "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t",
                Util.hexToBase64("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"));
    }

    @Test
    void c2()
    {
        byte[] bytes1 = Util.toBytes("1c0111001f010100061a024b53535009181c");
        byte[] bytes2 = Util.toBytes("686974207468652062756c6c277320657965");

        Assertions.assertEquals("746865206b696420646f6e277420706c6179",
                Util.toHex( Util.doRepeatingKeyXOR(bytes1, bytes2 )));
    }

    @Test
    void c3()
    {
        Assertions.assertEquals("Cooking MC's like a pound of bacon",
                Decryptor.decryptSingleByteXORMostProbable("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"));
    }

    @Test
    void c4() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("input\\s1c4.txt"));
        String line;

        TreeSet<Candidate> finalCandidates = new TreeSet<>();

        while((line = br.readLine()) != null)
        {
            finalCandidates.add( Decryptor.decryptSingleByteXOR(line).first() );
        }
        br.close();
        Assertions.assertEquals("Now that the party is jumping",
                finalCandidates.first().getResultAsString());
    }

    @Test
    void c5()
    {
        byte[] input = Util.getByteArray("Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal");
        byte[] key = Util.getByteArray("ICE");

        Assertions.assertEquals("0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272" +
                        "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f",
                Util.toHex( Util.doRepeatingKeyXOR(input, key)) );
    }

    @Test
    void c6() throws IOException
    {
        byte[] input = Util.getBytesFromBase64File("input\\s1c6.txt");

        Assertions.assertEquals("Terminator X: Bring the noise",
                Decryptor.getProbableKeyForRepeatableXOR(input, 2, 40, 40, 10));
    }

    @Test
    void c7() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        byte[] input = Util.getBytesFromBase64File("input\\s1c7.txt");

        Assertions.assertEquals(true, Decryptor.decryptAESInECBMode(input, "YELLOW SUBMARINE").startsWith("I'm back and I'm ringin' the bell"));
    }

    @Test
    void c8() throws IOException
    {
        BufferedReader br = new BufferedReader( new FileReader( "input\\s1c8.txt") );
        String line;
        List<String> input = new ArrayList<>();

        while((line = br.readLine()) != null)
        {
            input.add(line);
        }
        br.close();
    }
}
