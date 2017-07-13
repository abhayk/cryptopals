package util;

import java.util.Base64;

public class ConversionUtil
{
    public static String hexToBase64( String hex )
    {
        return Base64.getEncoder().encodeToString(hexToBytes(hex));
    }

    public static byte[] hexToBytes(String hex)
    {
        byte[] bytes = new byte[hex.length() / 2];

        char[] inputBytes = hex.toCharArray();

        int j=0;
        for(int i=0; i<inputBytes.length; i+=2)
        {
            bytes[j++] = (byte)(Character.digit(inputBytes[i], 16) * 16 +
                    Character.digit(inputBytes[i+1], 16));
        }
        return bytes;
    }

    public static String bytesToHex(byte[] input)
    {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<input.length; i++)
        {
            sb.append(byteToHex(input[i]));
        }
        return sb.toString();
    }

    public static String byteToHex(byte input)
    {
        String s = Integer.toHexString( input & 0xFF);
        if( s.length() == 1)
            s = "0" + s;
        return s;
    }
}
