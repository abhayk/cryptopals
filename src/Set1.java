import java.util.Base64;

public class Set1
{
    public static void main(String[] args)
    {
        assert hexToBase64("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d").equals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t");
    }


    private static String hexToBase64( String hex )
    {
        return Base64.getEncoder().encodeToString(hexToBytes(hex));
    }

    private static byte[] hexToBytes(String hex)
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

    private static String fixedXOR(String a, String b)
    {
        byte[] bytes1 = hexToBytes(a);
        byte[] bytes2 = hexToBytes(b);

        byte[] result = new byte[bytes1.length];

        for(int i=0; i<bytes1.length; i++)
        {
            result[i] = (byte)(bytes1[i] ^ bytes2[i]);
        }
        return null;
    }
}
