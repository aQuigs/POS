import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddRestaurantUser")
public class AddRestaurantUser extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public AddRestaurantUser()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[]
        { "adminUsername", "adminPassword", "username", "password", "email", "accountType" }))
        {
            writer.append("error");
            return;
        }

        String adminUsername = request.getParameter("adminUsername");
        String adminPassword = request.getParameter("adminPassword");
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
                writer.append("Invalid admin account");
                return;
            }

            if (ServletUtilities.isUsernameEmailTaken(sql, username, email))
            {
                writer.append("taken");
                return;
            }
            int result = sql.InsertSQL(String.format(
                    "INSERT INTO UserInfo (username,password,type,email,restaurantId) VALUES ('%s','%s','%s','%s',%s);", username, password, type,
                    email, restaurantId));

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
