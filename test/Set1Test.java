import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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
        Assertions.assertEquals("746865206b696420646f6e277420706c6179",
                Util.fixedXOR("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965"));
    }

    @Test
    void c3()
    {
        Assertions.assertEquals("Cooking MC's like a pound of bacon",
                Util.decryptSingleByteXORMostProbable("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"));
    }

    @Test
    void c4() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("input\\s1c4.txt"));
        String line;

        TreeMap<Float, String> finalCandidates = new TreeMap<>();

        while((line = br.readLine()) != null)
        {
            Map<Float, String> candidates = Util.decryptSingleByteXOR(line);
            Map.Entry<Float, String> entry = candidates.entrySet().iterator().next();
            finalCandidates.put(entry.getKey(), entry.getValue());
        }
        br.close();
        Assertions.assertEquals("Now that the party is jumping",
                finalCandidates.entrySet().iterator().next().getValue().trim());
    }

    @Test
    void c5()
    {
        Assertions.assertEquals("0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272" +
                "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f",
                Util.repeatingKeyXOR("Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal", "ICE"));
    }
}
