import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteMenuItem")
public class DeleteMenuItem extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public DeleteMenuItem()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "adminPassword", "menuItemId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String password = request.getParameter("adminPassword");
        String menuItemId = request.getParameter("menuItemId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            String[] returnString = sql.ProcedureDeleteMenuItem(username, password, menuItemId);
            int retCode = Integer.parseInt(returnString[0]);
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
    }
}
