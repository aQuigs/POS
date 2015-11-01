import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ItemStarted")
public class ItemStarted extends DetailStatusChanged
{
    private static final long serialVersionUID = 1L;

    public ItemStarted()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        super.doChange(request, response, ServletUtilities.OrderStatus.PLACED, ServletUtilities.OrderStatus.STARTED);
    }
}
