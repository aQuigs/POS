
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Version {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

    	MySQLUtilities sql = new MySQLUtilities();
    	ResultSet rs = sql.SelectSQL("SELECT * From Persons");

        while (rs.next())
        {
            System.out.println(rs.getString(2));
        }
    }
}
