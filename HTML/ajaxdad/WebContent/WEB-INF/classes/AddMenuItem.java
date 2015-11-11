import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/AddMenuItem")
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
        String imageUrl = request.getParameter("imageUrl");
        Part file = request.getPart("file");
        StringBuilder sb = new StringBuilder();
        for (String s : file.getHeader("content-dispotion").split(";"))
        {
            sb.append(s + "\n");
        }
        
        
        writer.append(sb.toString());
        return;

//        try
//        {
//            MySQLUtilities sql = new MySQLUtilities();
//            int retCode = sql.ProcedureAddMenuItem(username, password, menuId, itemName, cost, submenu, description, imageUrl);
//            writer.append(retCode < 0 ? ServletUtilities.decodeErrorCode(retCode) : "" + retCode);
//        }
//        catch (ClassNotFoundException e)
//        {
//            writer.append("error");
//        }
//        catch (SQLException e)
//        {
//            writer.append("error");
//        }
//        catch (NumberFormatException e)
//        {
//            writer.append("invalid");
//        }
    }
    
    private String getFilename(Part file)
    {
        if (file != null)
        {
            for (String cd : file.getHeader("content-disposition").split(";")) {
                if (cd.trim().startsWith("filename")) {
                    return cd;
//                    String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
//                    return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
                }
            }
        }
        return null;
    }
}
