import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChangeRestaurantUser")
public class ChangeRestaurantUser extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ChangeRestaurantUser()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[]
        { "adminUsername", "adminPassword", "oldUsername", "username", "password", "email", "accountType" }))
        {
            writer.append("error");
            return;
        }

        String adminUsername = request.getParameter("adminUsername");
        String adminPassword = request.getParameter("adminPassword");
        String oldUsername = request.getParameter("oldUsername");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String type = request.getParameter("accountType");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            String restaurantId = ServletUtilities.getRestaurantFromAdmin(sql, adminUsername, adminPassword);
            if (restaurantId == null)
            {
                writer.append("invalid");
                return;
            }

            if (oldUsername.equals(adminUsername) && !type.equals("admin"))
            {
                writer.append("admin");
                return;
            }

            // TODO
            // if (ServletUtilities.isUsernameEmailTaken(sql, username, email))
            // {
            // writer.append("taken");
            // return;
            // }

            String salt = ServletUtilities.generateSalt();
            String passwordHash = ServletUtilities.generateHash(password, salt);
            int result = sql
                    .UpdateSQL(String
                            .format(
                                    "UPDATE UserInfo SET username='%s',restaurantPassword='%s',password='%s',salt='%s',type='%s',email='%s' WHERE restaurantId=%s AND username='%s';",
                                    username, password, passwordHash, salt, type, email, restaurantId, oldUsername));

            if (result == 0)
            {
                writer.append("invalid");
            }
            else
            {
                writer.append("success");
            }
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
