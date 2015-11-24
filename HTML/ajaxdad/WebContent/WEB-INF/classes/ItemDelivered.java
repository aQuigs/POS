import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ItemDelivered")
public class ItemDelivered extends DetailStatusChanged
{
    private static final long serialVersionUID = 1L;

    public ItemDelivered()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        super.doChange(request, response, ServletUtilities.OrderStatus.COOKED, ServletUtilities.OrderStatus.DELIVERED, "waitstaff");
    }
}
