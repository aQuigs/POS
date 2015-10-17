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
        { "adminUsername", "oldUsername", "username", "password", "email", "accountType" }))
        {
            writer.append("error");
            return;
        }

        String adminUsername = request.getParameter("adminUsername");
        String oldUsername = request.getParameter("oldUsername");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String type = request.getParameter("accountType");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            String restaurantId = ServletUtilities.getRestaurantFromAdmin(sql, adminUsername);
            if (restaurantId == null)
            {
                writer.append("Invalid admin account");
                return;
            }

            if (oldUsername.equals(adminUsername) && !type.equals("admin"))
            {
                writer.append("admin");
                return;
            }
            
            //if (ServletUtilities.isUsernameEmailTaken(sql, username, email))
            //{
                //writer.append("taken");
                //return;
            //}

            int result = sql.UpdateSQL("UPDATE UserInfo SET username='" + username + "',password='" + password + "',type='" + type + "',email='"
                    + email + "' WHERE restaurantId=" + restaurantId + " AND username='" + oldUsername + "';");

            if (result == 0)
            {
            	//writer.append("success");
                writer.append("invalid");
            }
            else
            {
                writer.append("success");
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace(writer);
            writer.append("error");
        }
        catch (SQLException e)
        {
            e.printStackTrace(writer);
            writer.append("error");
        }
    }
}
