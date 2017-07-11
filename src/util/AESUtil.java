package util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AESUtil
{

    public static String decryptAESInECBMode(byte[] input, String key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        byte[] result = doAESInECBMode(input, key, Cipher.DECRYPT_MODE);
        return new String(result).trim();
    }

    public static byte[] encryptAESInECBMode(byte[] input, String key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        return doAESInECBMode(input, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] doAESInECBMode(byte[] input, String key, int cipherMode) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = getAESCipherInstance(key, "AES/ECB/NoPadding", cipherMode);

        return cipher.doFinal(input);
    }

    public static Cipher getAESCipherInstance(String key, String transformation, int cipherMode) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance(transformation);

        cipher.init(cipherMode, spec);

        return cipher;
    }

    public static byte[] encryptAESInCBCModeManual(byte[] input, String key, byte[] iv) throws
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        byte[] keyBytes = key.getBytes();
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

            cipherBytes = encryptAESInECBMode( xorBytes, key );

            System.arraycopy(cipherBytes, 0, resultBytes, i, cipherBytes.length);
        }
        return resultBytes;
    }

    public static String decryptAESInCBCModeManual(byte[] input, String key, byte[] iv) throws
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] keyBytes = key.getBytes();
        int blockSize = keyBytes.length;
        byte[] block;
        byte[] plainBytes;
        byte[] decryptedBytes;
        byte[] cipherBytes = iv;
        byte[] resultBytes = new byte[ input.length ];

        for(int i=0; i<input.length; i += blockSize )
        {
            block = Arrays.copyOfRange(input, i, i + blockSize);

            decryptedBytes = doAESInECBMode( block, key, Cipher.DECRYPT_MODE );

            plainBytes = CryptoUtil.doRepeatingKeyXOR(decryptedBytes, cipherBytes);

            cipherBytes = block;

            System.arraycopy(plainBytes, 0, resultBytes, i, plainBytes.length);
        }
        return new String(resultBytes).trim();
    }
}
