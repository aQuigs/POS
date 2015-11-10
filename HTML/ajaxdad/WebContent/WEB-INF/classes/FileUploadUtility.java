import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadUtility
{
    private static final String filePath = "/home/ubuntu/images/";
    private static final int MAX_FILE_SIZE = 5000 * 1024;
    private static final int MAX_MEM_SIZE = 64 * 1024;

    public static String uploadFile(HttpServletRequest request, String filename) throws FileUploadException
    {
        File file;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart)
        {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(MAX_MEM_SIZE);
            factory.setRepository(new File("/home/ubuntu/images"));

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(MAX_FILE_SIZE);

            List<FileItem> fileItems = upload.parseRequest(request);
            Iterator<FileItem> i = fileItems.iterator();

            while (i.hasNext())
            {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField())
                {
                    long sizeInBytes = fi.getSize();
                    if (sizeInBytes > MAX_FILE_SIZE)
                    {
                        throw new FileSizeLimitExceededException("File exceeds max size of " + MAX_FILE_SIZE + " bytes", sizeInBytes, MAX_FILE_SIZE);
                    }

                    file = new File(filePath + filename);

                    try
                    {
                        fi.write(file);
                    }
                    catch (Exception e)
                    {
                        return null;
                    }
                }
            }
        }

        return null;
    }
}
