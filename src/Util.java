import java.util.Base64;
import java.util.Locale;
import java.util.TreeSet;

public class Util
{
    public static String hexToBase64( String hex )
    {
        return Base64.getEncoder().encodeToString(toBytes(hex));
    }

    public static byte[] toBytes(String hex)
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

    public static String toHex(byte[] input)
    {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<input.length; i++)
        {
            sb.append(toHex(input[i]));
        }
        return sb.toString();
    }

    public static String toHex(byte input)
    {
        String s = Integer.toHexString( input & 0xFF);
        if( s.length() == 1)
            s = "0" + s;
        return s;
    }

    public static byte[] getByteArray(String s)
    {
        byte[] bytes = new byte[s.length()];

        int i=0;
        for(char c : s.toCharArray())
        {
            bytes[i++] = (byte)c;
        }
        return bytes;
    }

    public static byte[] doRepeatingKeyXOR(byte[] bytes1, byte[] bytes2)
    {
        byte[] result = new byte[bytes1.length];

        int j = 0;
        for(int i=0; i<bytes1.length; i++)
        {
            if( j == bytes2.length )
                j = 0;
            result[i] = (byte)(bytes1[i] ^ bytes2[j++]);
        }
        return result;
    }

    public static byte[] doXOR(byte[] input, byte b)
    {
        byte[] result = new byte[input.length];
        for(int i=0; i<input.length; i++)
        {
            result[i] = (byte)(input[i] ^ b);
        }
        return result;
    }

    public static float getChi2(byte[] input)
    {
        float[] default_freq = new float[] {
                0.0804f, 0.0148f, 0.0334f, 0.0382f, 0.1249f, 0.0240f, 0.0187f, 0.0505f, 0.0757f,
                0.0016f, 0.0054f, 0.0407f, 0.0251f, 0.0723f, 0.0764f, 0.0214f, 0.0012f, 0.0628f,
                0.0651f, 0.0928f, 0.0273f, 0.0105f, 0.0168f, 0.0023f, 0.0166f, 0.0009f };

        int[] letter_freq = new int[default_freq.length];

        int ignored = 0;

        for (byte b : input)
        {
            if (b >= 65 && b <= 90)
                letter_freq[b - 65]++;
            else if (b >= 97 && b <= 122)
                letter_freq[b - 97]++;
            else if (b >= 32 && b <= 126)
                ignored++;
            else if( b == 9 || b == 10 || b == 13 )
                ignored++;
            else
            {
                //exitChars.add(b);
                //System.out.println("Exit char - " + b);
                return 10000f;
            }
        }

        //System.out.println("Reached out of exit");

        //System.out.println("Exit chars - " + exitChars.toString());
        float chi2 = 0f;
        int valid_len = input.length - ignored;
        if( ((float)valid_len / input.length) < 0.6f)
            return 20000f;

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

    public static int getSetBitCount(int a)
    {
        int count = 0;
        while( a != 0)
        {
            a &= (a-1);
            count++;
        }
        return count;
    }
}
