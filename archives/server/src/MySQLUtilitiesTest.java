import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Mel Chi and Tim Parzynski
 *
 */
public class MySQLUtilitiesTest  {


	String firstColumn = "MelABCD";
	String secondColumn = "Mel is the best";
	MySQLUtilities sql = null;
	
	@Before
	public void setUp()
	{
		try {
			this.sql = new MySQLUtilities();
			//this.sql = new MySQLUtilities("jdbc:mysql://ccl5zwj6cgtotm.c6af5bvxbhrs.us-east-1.rds.amazonaws.com:3306/CSE480", "cse480username", "poscse480");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
    		fail("Failed");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testInsertSQL()
	{
    	System.out.println("---------testInsertSQL--------------");
		try {
			HashMap<String, String> theMap2 = new HashMap<String,String>();
	    	theMap2.put("melcolumn1", firstColumn);
	    	theMap2.put("melcolumn2", secondColumn);
	    	sql.InsertSQL("Mel", theMap2);
	    	
	    	ResultSet rs1 = sql.SelectSQL("SELECT * From Mel WHERE melcolumn1 = " + sql.SQLFormat(firstColumn));
	    	while (rs1.next())
	        {
	    		assertEquals(rs1.getString(1), firstColumn);
	    		System.out.println(rs1.getString(1) + " " + firstColumn);
	    		assertEquals(rs1.getString(2), secondColumn);
	    		System.out.println(rs1.getString(2) + " " + secondColumn);

	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Failed");
			e.printStackTrace();
		}

	}
	
	@Test
	public void testSelectSQL()
	{
    	System.out.println("--------testSelectSQL--------");
		try {
			HashMap<String, String> theMap2 = new HashMap<String,String>();
	    	theMap2.put("melcolumn1", firstColumn);
	    	theMap2.put("melcolumn2", secondColumn);
	    	sql.InsertSQL("Mel", theMap2);
 			ResultSet rs1 = sql.SelectSQL("SELECT * From Mel WHERE melcolumn1 = 'MelABCD'");
	    	
 			int counter = 0;
 			while (rs1.next())
	        {
	    		assertEquals(rs1.getString(1), firstColumn);
	    		System.out.println(rs1.getString(1) + " " + firstColumn);
	    		assertEquals(rs1.getString(2), secondColumn);
	    		System.out.println(rs1.getString(2) + " " + secondColumn);
	    		counter++;
	        }
 			assertEquals(counter, 1);
	    	sql.DeleteSQL("DELETE FROM Mel WHERE melcolumn1 = " + sql.SQLFormat(firstColumn));
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
	}
	
	
	@Test
	public void testDeleteSQL()
	{
    	System.out.println("---------testDelteSQL--------------");
		try {
/*			HashMap<String, String> theMap2 = new HashMap<String,String>();
	    	theMap2.put("melcolumn1", firstColumn + "MEL");
	    	theMap2.put("melcolumn2", secondColumn + "MEL");
	    	
	    	sql.InsertSQL("Mel", theMap2);*/
	    	sql.DeleteSQL("DELETE FROM Mel WHERE melcolumn1 = " + sql.SQLFormat(firstColumn));
	    	ResultSet rs1 = sql.SelectSQL("SELECT * From Mel WHERE melcolumn1 = " + sql.SQLFormat(firstColumn + "MEL"));
	    	int counter = 0;
	    	while (rs1.next())
	    	{
	    		System.out.println(rs1.getString(1) + " " + firstColumn);
	    		System.out.println(rs1.getString(2) + " " + secondColumn);

	    		counter++;
	    	}
	    	assertEquals(counter, 0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Failed");
			e.printStackTrace();
		}

		
	}
	
	@Test
	public void testCreateTableNoRestrictions()
	{
		try {
	    	HashMap<String, Object> theMap = new HashMap<String,Object>();
	    	theMap.put("melcolumn1", String.class);
	    	theMap.put("melcolumn2", String.class);
	    	
	    	sql.CreateTableNoRestrictions("Melvin", theMap);
	    	
			HashMap<String, String> theMap2 = new HashMap<String,String>();
	    	theMap2.put("melcolumn1", firstColumn);
	    	theMap2.put("melcolumn2", secondColumn);
	    	sql.InsertSQL("Melvin", theMap2);
	    	
	    	ResultSet rs1 = sql.SelectSQL("SELECT * From Mel WHERE melcolumn1 = " + sql.SQLFormat(firstColumn));
	    	//assertEquals(rs1.getFetchSize(), 2);
	    	while (rs1.next())
	        {
	    		assertEquals(rs1.getString(1), firstColumn);
	    		System.out.println(rs1.getString(1) + " " + firstColumn);
	    		assertEquals(rs1.getString(2), secondColumn);
	    		System.out.println(rs1.getString(2) + " " + secondColumn);

	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Failed");
			e.printStackTrace();
		}
	}

	@Test
	public void testDropTable()
	{
		try {
			sql.DropTable("Melvin", "POS");
	    	ResultSet rs1 = sql.SelectSQL("SELECT * From Mel WHERE melcolumn1 = " + sql.SQLFormat(firstColumn));
 			int counter = 0;
 			while (rs1.next())
	        {
 				counter++;
	        }
	    	
	    	assertEquals(counter, 0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Failed");
			e.printStackTrace();
		}
	}

}
