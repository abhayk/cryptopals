import objects.Candidate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Decryptor
{
    public static String getProbableKeyForRepeatableXOR(byte[] input, int keySizeFrom, int keySizeTo, int blockCount, int probableKeyTryCount)
    {
        Map<Float, List<Integer>> normalizedDistances = new TreeMap<>();
        for(int i=keySizeFrom; i<keySizeTo; i++)
        {
            float normalizedDistanceSum = 0f;
            for(int j=0; j<blockCount; j += i*2)
            {
                byte[] block1 = Arrays.copyOfRange(input, j, i + j);
                byte[] block2 = Arrays.copyOfRange(input, i + j, i*2 + j);

                int hammingDistance = Util.hammingDistance(block1, block2);
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
                    sb.append((char)decryptSingleByteXOR(blocks[i]).first().getKey());
                }
                probableKeys.add(sb.toString());
            }
        }

        TreeMap<Float, String> probableKeysSorted = new TreeMap<>();

        for(String probableKey : probableKeys)
        {
            Float keyScore = Util.getChi2( probableKey.getBytes() );
            probableKeysSorted.put(keyScore, probableKey);
        }
        return probableKeysSorted.firstEntry().getValue();
    }

    public static TreeSet<Candidate> decryptSingleByteXOR(String input)
    {
        return decryptSingleByteXOR(Util.toBytes(input));
    }

    public static TreeSet<Candidate> decryptSingleByteXOR(byte[] inputBytes)
    {
        TreeSet<Candidate> candidates = new TreeSet<>();

        for(int i=0; i<128; i++)
        {
            byte[] out = Util.doXOR(inputBytes, (byte)i);

            Candidate c = new Candidate();
            c.setResult(out);
            c.setScore(Util.getChi2(out));
            c.setKey((byte)i);

            candidates.add(c);
        }
        return candidates;
    }

    public static String decryptSingleByteXORMostProbable(String input)
    {
        return decryptSingleByteXOR(input).first().getResultAsString();
    }

    public static String decryptAESInECBMode(byte[] input, String key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, spec);

        byte[] result = cipher.doFinal(input);

        return new String(result).trim();
    }
}
