import objects.Candidate;
import util.ConversionUtil;
import util.CryptoUtil;
import util.FileUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeSet;

public class Set1
{
    // Convert hex to base64
    public static String challenge1( String input )
    {
        return ConversionUtil.hexToBase64(input);
    }

    //Fixed XOR
    public static String challenge2(String s1, String s2)
    {
        byte[] bytes1 = ConversionUtil.hexToBytes(s1);
        byte[] bytes2 = ConversionUtil.hexToBytes(s2);

        return ConversionUtil.bytesToHex( CryptoUtil.doRepeatingKeyXOR(bytes1, bytes2 ));
    }

    // Single-byte XOR cipher
    public static String challenge3(String input )
    {
        return CryptoUtil.decryptSingleByteXORMostProbable( input );
    }

    // Detect single-character XOR
    public static String challenge4( String inputFilePath) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
        String line;

        TreeSet<Candidate> finalCandidates = new TreeSet<>();

        while((line = br.readLine()) != null)
        {
            finalCandidates.add( CryptoUtil.decryptSingleByteXOR(line).first() );
        }
        br.close();

        return finalCandidates.first().getResultAsString();
    }

    // Implement repeating-key XOR
    public static String challenge5(String input, String key)
    {
        byte[] inputBytes = ConversionUtil.stringToByteArray(input);
        byte[] keyBytes = ConversionUtil.stringToByteArray(key);

        return ConversionUtil.bytesToHex( CryptoUtil.doRepeatingKeyXOR(inputBytes, keyBytes));
    }

    //Break repeating-key XOR
    public static String challenge6(String inputFilePath ) throws IOException
    {
        byte[] input = FileUtil.getBytesFromBase64File(inputFilePath);

        return CryptoUtil.getProbableKeyForRepeatingKeyXOR(input, 2, 40, 40, 10);
    }

    //AES in ECB mode
    public static String challenge7(String inputFilePath, String key) throws IOException, IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        byte[] input = FileUtil.getBytesFromBase64File(inputFilePath);

        return CryptoUtil.decryptAESInECBMode(input, key);
    }
}
