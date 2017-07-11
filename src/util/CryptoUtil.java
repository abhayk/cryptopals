package util;

import objects.Candidate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CryptoUtil
{
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
                return 10000f;
        }

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

        byte[] b1 = ConversionUtil.stringToByteArray(s1);
        byte[] b2 = ConversionUtil.stringToByteArray(s2);

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

    public static TreeSet<Candidate> decryptSingleByteXOR(String input)
    {
        return decryptSingleByteXOR(ConversionUtil.hexToBytes(input));
    }

    public static TreeSet<Candidate> decryptSingleByteXOR(byte[] inputBytes)
    {
        TreeSet<Candidate> candidates = new TreeSet<>();

        for(int i=0; i<128; i++)
        {
            byte[] out = doXOR(inputBytes, (byte)i);

            Candidate c = new Candidate();
            c.setResult(out);
            c.setScore(getChi2(out));
            c.setKey((byte)i);

            candidates.add(c);
        }
        return candidates;
    }

    public static String decryptSingleByteXORMostProbable(String input)
    {
        return decryptSingleByteXOR(input).first().getResultAsString();
    }

    public static String getProbableKeyForRepeatingKeyXOR(byte[] input, int keySizeFrom, int keySizeTo, int blockCount, int probableKeyTryCount)
    {
        Map<Float, List<Integer>> normalizedDistances = new TreeMap<>();
        for(int i=keySizeFrom; i<keySizeTo; i++)
        {
            float normalizedDistanceSum = 0f;
            for(int j=0; j<blockCount; j += i*2)
            {
                byte[] block1 = Arrays.copyOfRange(input, j, i + j);
                byte[] block2 = Arrays.copyOfRange(input, i + j, i*2 + j);

                int hammingDistance = hammingDistance(block1, block2);
                normalizedDistanceSum += (float)hammingDistance / i;
            }
            normalizedDistances.computeIfAbsent(normalizedDistanceSum / blockCount, k -> new ArrayList<>()).add(i);
        }

        Iterator<Map.Entry<Float, List<Integer>>> probableKeySizeIterator = normalizedDistances.entrySet().iterator();

        List<String> probableKeys = new ArrayList<>();

        for(int m = 0; m < probableKeyTryCount; m++)
        {
            List<Integer> probableKeySizes = probableKeySizeIterator.next().getValue();

            for(Integer probableKeySize : probableKeySizes)
            {
                int blockSize = input.length / probableKeySize;

                byte[][] blocks = new byte[probableKeySize][blockSize];

                for(int i=0; i<probableKeySize; i++)
                {
                    int k=0;
                    for(int j=i; j < blockSize * probableKeySize; j += probableKeySize)
                    {
                        blocks[i][k++] = input[j];
                    }
                }

                StringBuilder sb = new StringBuilder();

                for(int i=0; i<probableKeySize; i++)
                {
                    sb.append((char) decryptSingleByteXOR(blocks[i]).first().getKey());
                }
                probableKeys.add(sb.toString());
            }
        }

        TreeMap<Float, String> probableKeysSorted = new TreeMap<>();

        for(String probableKey : probableKeys)
        {
            Float keyScore = getChi2( probableKey.getBytes() );
            probableKeysSorted.put(keyScore, probableKey);
        }
        return probableKeysSorted.firstEntry().getValue();
    }
}
