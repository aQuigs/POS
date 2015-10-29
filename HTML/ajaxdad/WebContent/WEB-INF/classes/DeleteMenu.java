import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteMenu")
public class DeleteMenu extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public DeleteMenu()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "adminPassword", "menuId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String password = request.getParameter("adminPassword");
        String menuId = request.getParameter("menuId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int result = sql.ProcedureDeleteMenu(username, password, menuId);
            write.append(result);
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
