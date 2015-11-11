import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

public class FileUploadUtility
{
    private static final String filePath = "/home/ubuntu/images/";

    public static boolean uploadFile(String filename, Part filePart) throws IOException
    {
        FileOutputStream out = null;
        InputStream filecontent = null;
        try
        {
            out = new FileOutputStream(filePath + filename);
            filecontent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            return true;
        }
        catch (FileNotFoundException fne)
        {
            return false;
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
            if (filecontent != null)
            {
                filecontent.close();
            }
        }
    }

    public static boolean renameFile(String oldname, String newname)
    {
        return new File(filePath + oldname).renameTo(new File(filePath + newname));
    }

    public static void deleteFile(String filename)
    {
        new File(filePath + filename).delete();
    }
}
