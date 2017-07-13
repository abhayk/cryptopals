package util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class AESUtil
{

    public static String decryptAESInECBMode(byte[] input, byte[] key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        byte[] result = doAESInECBMode(input, key, Cipher.DECRYPT_MODE);
        return new String(result).trim();
    }

    public static byte[] encryptAESInECBMode(byte[] input, byte[] key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        return doAESInECBMode(input, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] doAESInECBMode(byte[] input, byte[] key, int cipherMode) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = getAESCipherInstance(key, "AES/ECB/NoPadding", cipherMode);

        return cipher.doFinal(input);
    }

    public static Cipher getAESCipherInstance(byte[] key, String transformation, int cipherMode) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec spec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance(transformation);

        cipher.init(cipherMode, spec);

        return cipher;
    }

    public static byte[] encryptAESInCBCModeManual(byte[] input, byte[] keyBytes, byte[] iv) throws
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        int blockSize = keyBytes.length;
        byte[] block;
        byte[] xorBytes;
        byte[] cipherBytes = iv;
        int resultSize = input.length < 16 ? 16 : ((input.length / 16) + 1) * 16;
        byte[] resultBytes = new byte[ resultSize ];

        for(int i=0; i<input.length; i += blockSize)
        {
            block = Arrays.copyOfRange(input, i, i + blockSize);

            xorBytes = CryptoUtil.doRepeatingKeyXOR( block, cipherBytes );

            cipherBytes = encryptAESInECBMode( xorBytes, keyBytes );

            System.arraycopy(cipherBytes, 0, resultBytes, i, cipherBytes.length);
        }
        return resultBytes;
    }

    public static String decryptAESInCBCModeManual(byte[] input, byte[] keyBytes, byte[] iv) throws
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        int blockSize = keyBytes.length;
        byte[] block;
        byte[] plainBytes;
        byte[] decryptedBytes;
        byte[] cipherBytes = iv;
        byte[] resultBytes = new byte[ input.length ];

        for(int i=0; i<input.length; i += blockSize )
        {
            block = Arrays.copyOfRange(input, i, i + blockSize);

            decryptedBytes = doAESInECBMode( block, keyBytes, Cipher.DECRYPT_MODE );

            plainBytes = CryptoUtil.doRepeatingKeyXOR(decryptedBytes, cipherBytes);

            cipherBytes = block;

            System.arraycopy(plainBytes, 0, resultBytes, i, plainBytes.length);
        }
        return new String(resultBytes).trim();
    }

}
