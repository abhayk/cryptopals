package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

public class FileUtil
{
    public static byte[] getBytesFromBase64File(String filePath) throws IOException
    {
        BufferedReader br = new BufferedReader( new FileReader(filePath));
        String line;
        StringBuilder sb = new StringBuilder();

        while((line = br.readLine()) != null)
        {
            sb.append( line );
        }
        br.close();
        return Base64.getDecoder().decode( sb.toString() );
    }
}
