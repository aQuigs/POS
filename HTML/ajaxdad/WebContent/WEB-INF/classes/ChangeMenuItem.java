import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/ChangeMenuItem")
@MultipartConfig
public class ChangeMenuItem extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ChangeMenuItem()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities
                .checkSingletonInputs(request, new String[] { "adminUsername", "adminPassword", "menuItemId", "menuId", "name", "cost" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String password = request.getParameter("adminPassword");
        String menuItemId = request.getParameter("menuItemId");
        String menuId = request.getParameter("menuId");
        String itemName = request.getParameter("name");
        String cost = request.getParameter("cost");
        String subMenu = request.getParameter("submenu");
        String description = request.getParameter("description");
        String imageUrl = null;

        Part file = request.getPart("file");
        if (file != null)
        {
            String ext = ServletUtilities.getExtension(file);
            if (!ServletUtilities.isImageExtension(ext))
            {
                writer.append("Invalid image extension");
                return;
            }

            imageUrl = "menuItem-" + menuItemId + ext;
            if (!FileUploadUtility.uploadFile(imageUrl, file))
            {
                writer.append("error");
                return;
            }
        }

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            String[] returnString = sql.ProcedureChangeMenuItem(username, password, menuItemId, menuId, itemName, cost, subMenu, description, imageUrl);
            int retCode = Integer.parseInt(returnString[0]);
            if (retCode == 0 && returnString[1] != null)
            {
                FileUploadUtility.deleteFile(returnString[1]);
            }
            writer.append(retCode < 0 ? ServletUtilities.decodeErrorCode(retCode) : "success");
        }
        catch (ClassNotFoundException e)
        {
            writer.append("error");
        }
        catch (SQLException e)
        {
            writer.append("error");
        }
        catch (Exception e)
        {
            writer.append("error");
        }
    }
}
