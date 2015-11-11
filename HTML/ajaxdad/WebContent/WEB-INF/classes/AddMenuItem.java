import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/AddMenuItem")
@MultipartConfig
public class AddMenuItem extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public AddMenuItem()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuId", "itemName", "cost", "adminPassword" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuId = request.getParameter("menuId");
        String itemName = request.getParameter("itemName");
        String cost = request.getParameter("cost");
        String submenu = request.getParameter("submenu");
        String description = request.getParameter("description");
        String password = request.getParameter("adminPassword");
        String imageUrl = null;
        String ext = null;
        try
        {
            Part file = request.getPart("file");
            if (file != null)
            {
                ext = ServletUtilities.getExtension(file);
                if (!ServletUtilities.isImageExtension(ext))
                {
                    writer.append("Invalid image extension");
                    return;
                }

                imageUrl = "tempImage-" + System.currentTimeMillis() + ext;
                if (!FileUploadUtility.uploadFile(imageUrl, file))
                {
                    writer.append("error");
                    return;
                }
            }

            try
            {
                MySQLUtilities sql = new MySQLUtilities();
                int retCode = sql.ProcedureAddMenuItem(username, password, menuId, itemName, cost,
                        submenu, description, null);

                if (retCode > 0 && imageUrl != null)
                {
                    String newName = "menuItem-" + retCode + ext;
                    boolean success = FileUploadUtility.renameFile(imageUrl, newName);
                    sql.UpdateSQL(String.format("UPDATE MenuDetails SET imageUrl='%s' WHERE menuItemId=%d;", success ? newName : imageUrl, retCode));
                }

                writer.append(retCode < 0 ? ServletUtilities.decodeErrorCode(retCode) : "" + retCode);
            }
            catch (ClassNotFoundException e)
            {
                writer.append("error");
            }
            catch (SQLException e)
            {
                writer.append("error");
            }
            catch (NumberFormatException e)
            {
                writer.append("invalid");
            }
        }
        catch (Exception e)
        {
            writer.append("error");
        }
    }
}
