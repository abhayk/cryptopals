import util.ConversionUtil;

import java.util.Arrays;

public class Set2
{
    public static String challenge1(String input, int blockSize, byte padByte)
    {
        byte[] inputBytes = ConversionUtil.stringToByteArray(input);

        int resultSize = inputBytes.length < blockSize ? blockSize : (inputBytes.length / blockSize) * blockSize;

        byte[] results = new byte[ resultSize ];

        System.arraycopy( inputBytes, 0, results, 0, inputBytes.length);

        Arrays.fill(results, inputBytes.length, results.length, padByte);

        return new String(results);
    }
}
