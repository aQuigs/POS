import java.util.*;
import java.sql.*;

public class Version {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

   	
    	MySQLUtilities sql = new MySQLUtilities("jdbc:mysql://ccl5zwj6cgtotm.c6af5bvxbhrs.us-east-1.rds.amazonaws.com:3306/CSE480", "cse480username", "fuckyoubitch");;
    	ResultSet rs = sql.SelectSQL("SELECT * From Persons");

    	while (rs.next())
        {
    		System.out.println(rs.getString(1));
            System.out.println(rs.getString(2));
        }
    	
    	HashMap<String, Object> theMap = new HashMap<String,Object>();
    	theMap.put("melcolumn1", String.class);
    	theMap.put("melcolumn2", String.class);
    	
    	//sql.CreateTableNoRestrictions("Mel", theMap);
    	
    	//sql.InsertSQL("INSERT INTO Mel (melcolumn1, melcolumn2) values ('StringX2', 'StringX2')");
    	HashMap<String, String> theMap2 = new HashMap<String,String>();
    	theMap2.put("melcolumn1", "Mel3");
    	theMap2.put("melcolumn2", "Mel4");
    	sql.InsertSQL("Mel", theMap2);
    	
    	ResultSet rs2 = sql.SelectSQL("SELECT * From Mel");

    	while (rs2.next())
        {
    		System.out.println(rs2.getString(1));
            System.out.println(rs2.getString(2));
        }

    	sql.close();
    }
}
