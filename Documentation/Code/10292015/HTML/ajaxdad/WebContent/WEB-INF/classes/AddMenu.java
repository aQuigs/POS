import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddMenu")
public class AddMenu extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public AddMenu()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuName", "adminPassword" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuName = request.getParameter("menuName");
        String password = request.getParameter("adminPassword");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int retCode = sql.ProcedureAddMenu(username, password, menuName);
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
    }
}
