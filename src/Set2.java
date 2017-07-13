import objects.MutableInt;
import util.AESUtil;
import util.ConversionUtil;
import util.CryptoUtil;
import util.FileUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Set2
{
    //Implement PKCS#7 padding
    public static String challenge9(String input, int blockSize, byte padByte)
    {
        return new String( CryptoUtil.pad( input.getBytes(), blockSize, padByte) );
    }

    //Implement CBC mode
    public static String challenge10(String inputFilePath, String key, byte[] iv) throws IOException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException
    {
        byte[] input = FileUtil.getBytesFromBase64File( inputFilePath );

        return AESUtil.decryptAESInCBCModeManual( input, key.getBytes(), iv);
    }

    public static int challenge11(byte[] cipherBytes) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, NoSuchPaddingException
    {
        int keyLength = 16;
        int max = 0;

        Map<String, MutableInt> blockCount = new HashMap<>();

        for(int i=0; i< cipherBytes.length - keyLength; i++)
        {
            String s = ConversionUtil.bytesToHex( Arrays.copyOfRange(cipherBytes, i, i+keyLength));
            int currentCount = blockCount.computeIfAbsent(s, k -> new MutableInt()).incrementAndGet();
            if( currentCount > max)
            {
                max = currentCount;
            }
        }
        return max > 2 ? 0 : 1;
    }

    public static byte[] encryptionOracle(byte[] input, int mode) throws IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        byte[] key = CryptoUtil.getRandomBytes( 16 );

        int padCount = ThreadLocalRandom.current().nextInt(5, 11);

        byte[] padBytes = new byte[padCount];

        Arrays.fill(padBytes, (byte)0);

        byte[] paddedInput = new byte[ input.length + padCount * 2];

        System.arraycopy(padBytes, 0, paddedInput, 0, padCount);

        System.arraycopy(input, 0, paddedInput, padCount, input.length);

        System.arraycopy(padBytes, 0, paddedInput, input.length + padCount, padCount);

        paddedInput = CryptoUtil.pad( paddedInput, key.length, (byte)0);

        //int mode = ThreadLocalRandom.current().nextInt(2);

        byte[] result;

        if( mode == 0)
        {
            result = AESUtil.encryptAESInECBMode(paddedInput, key);
        }
        else
        {
            byte[] iv = CryptoUtil.getRandomBytes( 16 );
            result = AESUtil.encryptAESInCBCModeManual(paddedInput, key, iv);
        }
        return result;
    }
}
