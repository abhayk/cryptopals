import java.util.*;

public class Util
{
    public static String hexToBase64( String hex )
    {
        return Base64.getEncoder().encodeToString(hexToBytes(hex));
    }

    public static String repeatingKeyXOR(String input, String key)
    {
        byte[] bytes1 = getByteArray(input);
        byte[] keyBytes = getByteArray(key);

        return genericXOR(bytes1, keyBytes);
    }

    public static String fixedXOR(String a, String b)
    {
        byte[] bytes1 = hexToBytes(a);
        byte[] bytes2 = hexToBytes(b);

        return genericXOR(bytes1, bytes2);
    }

    public static Map<Float, String> decryptSingleByteXOR(String input)
    {
        byte[] inputBytes = hexToBytes(input);

        TreeMap<Float, String> candidates = new TreeMap<>();

        for(int i=0; i<256; i++)
        {
            byte[] out = singleByteXOR(inputBytes, (byte)i);
            String s = new String(out);
            candidates.put( getChi2(s), s);
        }
        return candidates;
    }

    public static String decryptSingleByteXORMostProbable(String input)
    {
        return decryptSingleByteXOR(input).entrySet().iterator().next().getValue();
    }

    public static void decryptRepeatingKeyXOR(byte[] input)
    {

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

    private static String bytesToHex(byte[] input)
    {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<input.length; i++)
        {
            sb.append(byteToHex(input[i]));
        }
        return sb.toString();
    }

    private static String byteToHex(byte input)
    {
        String s = Integer.toHexString( input & 0xFF);
        if( s.length() == 1)
            s = "0" + s;
        return s;
    }

    private static byte[] getByteArray(String s)
    {
        byte[] bytes = new byte[s.length()];

        int i=0;
        for(char c : s.toCharArray())
        {
            bytes[i++] = (byte)c;
        }
        return bytes;
    }

    private static String genericXOR(byte[] bytes1, byte[] bytes2)
    {
        byte[] result = new byte[bytes1.length];

        int j = 0;
        for(int i=0; i<bytes1.length; i++)
        {
            if( j == bytes2.length )
                j = 0;
            result[i] = (byte)(bytes1[i] ^ bytes2[j++]);
        }
        return bytesToHex(result);
    }

    private static byte[] singleByteXOR(byte[] input, byte b)
    {
        byte[] result = new byte[input.length];
        for(int i=0; i<input.length; i++)
        {
            result[i] = (byte)(input[i] ^ b);
        }
        return result;
    }

    private static float getChi2(String input)
    {
        float[] default_freq = new float[] {
                0.08167f, 0.01492f, 0.02782f, 0.04253f, 0.12702f, 0.02228f, 0.02015f,
                0.06094f, 0.06966f, 0.00153f, 0.00772f, 0.04025f, 0.02406f, 0.06749f,
                0.07507f, 0.01929f, 0.00095f, 0.05987f, 0.06327f, 0.09056f, 0.02758f,
                0.00978f, 0.02360f, 0.00150f, 0.01974f, 0.00074f };

        int[] letter_freq = new int[default_freq.length];

        char[] inputChars = input.toUpperCase().toCharArray();

        int ignored = 0;

        for (char c : inputChars)
        {
            if (c >= 65 && c <= 90)
                letter_freq[c - 65]++;
            else if (c >= 32 && c <= 126)
                ignored++;
            else if( c == 9 || c == 10 || c == 13 )
                ignored++;
            else
                return Float.MAX_VALUE;
        }

        float chi2 = 0f;
        int valid_len = input.length() - ignored;
        if( ((float)valid_len / input.length()) < 0.7f)
            return Float.MAX_VALUE;

        for( int i=0; i<26; i++)
        {
            int observed = letter_freq[i];
            float expected = valid_len * default_freq[i];

            float diff = observed - expected;

            chi2 += (diff * diff) / expected;
        }
        return chi2;
    }

    public static int hammingDistance(String s1, String s2)
    {
        assert s1.length() == s2.length();

        byte[] b1 = getByteArray(s1);
        byte[] b2 = getByteArray(s2);

        return hammingDistance(b1, b2);
    }

    public static int hammingDistance(byte[] b1, byte[] b2)
    {
        assert b1.length == b2.length;

        int dist = 0;

        for(int i=0; i<b1.length; i++)
        {
            dist += getSetBitCount( b1[i] ^ b2[i] );
        }
        return dist;
    }

    private static int getSetBitCount(int a)
    {
        int count = 0;
        while( a != 0)
        {
            a &= (a-1);
            count++;
        }
        return count;
    }

    private static int getProbableKeySizeForRepeatableXOR(byte[] input)
    {
        TreeSet<Float> distances = new TreeSet<>(Collections.reverseOrder());

        for(int i=2; i<40; i++)
        {
            for(int j=0; j<4; j++)
            {

            }
        }
        return 0;
    }
}
