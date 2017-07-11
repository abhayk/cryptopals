import util.AESUtil;
import util.ConversionUtil;
import util.FileUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Set2
{
    //Implement PKCS#7 padding
    public static String challenge9(String input, int blockSize, byte padByte)
    {
        byte[] inputBytes = ConversionUtil.stringToByteArray(input);

        int resultSize = inputBytes.length < blockSize ? blockSize : (inputBytes.length / blockSize) * blockSize;

        byte[] results = new byte[ resultSize ];

        System.arraycopy( inputBytes, 0, results, 0, inputBytes.length);

        Arrays.fill(results, inputBytes.length, results.length, padByte);

        return new String(results);
    }

    //Implement CBC mode
    public static String challenge10(String inputFilePath, String key, byte[] iv) throws IOException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException
    {
        byte[] input = FileUtil.getBytesFromBase64File( inputFilePath );

        return AESUtil.decryptAESInCBCModeManual( input, key, iv);
    }
}
