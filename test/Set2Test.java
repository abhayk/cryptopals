import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Set2Test
{
    @Test
    void challenge1()
    {
        String input = "YELLOW SUBMARINE";

        byte padByte = (byte)4;

        String result = Set2.challenge1(input, 20, padByte);

        Assertions.assertEquals(20, result.length());

        for(int i= input.length(); i< result.length(); i++)
        {
            Assertions.assertEquals(padByte, result.charAt(i));
        }
    }
}
