import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * MySQL Utilites is the basic Select, Update, Insert and Delete
 * 
 * @author Mel Chi and Anthony Quigley
 */
public class MySQLUtilities
{

    private static String jdbcDriver = "com.mysql.jdbc.Driver";
    /**
     * Url is the url of where the server is
     */
    public static String Url = "jdbc:mysql://ccl5zwj6cgtotm.c6af5bvxbhrs.us-east-1.rds.amazonaws.com:3306/CSE480";
    /**
     * Username is the username
     */
    public static String Username = "cse480username";
    /**
     * Password, password will be stored here when the object is created
     */
    public static String Password = "cse480password";

    /**
     * MySQL connection, global so every function can use it
     */
    public  static Connection theConnection = null;

    /**
     * Public constructor
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    private StringUtilities theStringUtilities = new StringUtilities();

    public MySQLUtilities() throws SQLException, ClassNotFoundException
    {
        if (theConnection == null || theConnection.isClosed())
        {
            Class.forName(jdbcDriver);
            theConnection = DriverManager.getConnection(Url, Username, Password);
        }
    }

    /**
     * @param Url
     * @param Username
     * @param Password
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public MySQLUtilities(String Url, String Username, String Password) throws SQLException, ClassNotFoundException
    {
        this.Url = Url;
        this.Username = Username;
        this.Password = Password;
        Class.forName(jdbcDriver);
        theConnection = DriverManager.getConnection(Url, Username, Password);
    }

    /**
     * @param InsertSQLStatement
     *            Insert SQL Statement
     * @return returns 1 or 0 if it is successful or not
     * @throws SQLException
     */
    public int InsertSQL(String InsertSQLStatement) throws SQLException
    {
        PreparedStatement thePreparedStatement = theConnection.prepareStatement(InsertSQLStatement, Statement.RETURN_GENERATED_KEYS);
        thePreparedStatement.executeUpdate();
        ResultSet theResultSet = thePreparedStatement.getGeneratedKeys();
        /*
         * if(theResultSet.next()) { return theResultSet.getInt(1); } else { return 0; }
         */
        return 1;
    }

    /**
     * Inserts into the TableName given and giving a hashmap of <String, String> Column Name and
     * Value
     * 
     * @param TableName
     * @param TableColumnNameAndValue
     *            HashMap < String , String> which is <Column Name, Value>
     * @return returns 1 if success, 0 if failure
     * @throws SQLException
     */
    public int InsertSQL(String TableName, HashMap<String, String> TableColumnNameAndValue) throws SQLException
    {
        // sql.InsertSQL("INSERT INTO Mel (melcolumn1, melcolumn2) values ('String1', 'String2')");
        String insideTheColumnInsert = "";
        String insideTheValueInsert = "";

        for (Map.Entry<String, String> theMap : TableColumnNameAndValue.entrySet())
        {
            String columnName = theMap.getKey();
            String value = theMap.getValue();

            if (insideTheColumnInsert.length() > 0)
            {
                insideTheColumnInsert = " , " + insideTheColumnInsert;
                insideTheValueInsert = " , " + insideTheValueInsert;
            }

            insideTheColumnInsert = columnName + insideTheColumnInsert;
            insideTheValueInsert = SQLFormat(value) + insideTheValueInsert;
        }

        String insertSQL = "INSERT INTO " + TableName + " (" + insideTheColumnInsert + ") values (" + insideTheValueInsert + ")";

        return InsertSQL(insertSQL);

    }

    /**
     * @param SelectSQLQuery
     *            The Select SQL Statement will go here
     * @return Returns a resultSet that way you can get the data
     * @throws SQLException
     */
    public ResultSet SelectSQL(String SelectSQLQuery) throws SQLException
    {
        PreparedStatement thePreparedStatement = theConnection.prepareStatement(SelectSQLQuery);
        return thePreparedStatement.executeQuery();
    }

    public ResultSet ExecuteSQL(String ExecuteSQLQuery) throws SQLException
    {
        PreparedStatement thePreparedStatement = theConnection.prepareStatement(ExecuteSQLQuery);
        return thePreparedStatement.executeQuery();
    }

    /**
     * @param UpdateSQLStatement
     *            update sql statement will go here
     * @return returns 1 or 0, whether or not it happened
     * @throws SQLException
     */
    public int UpdateSQL(String UpdateSQLStatement) throws SQLException
    {
        PreparedStatement thePreparedStatement = theConnection.prepareStatement(UpdateSQLStatement);
        return thePreparedStatement.executeUpdate();
    }

    /**
     * @param DeleteSQLStatement
     *            The delete statement
     * @return returns 1 or 0
     * @throws SQLException
     */
    public int DeleteSQL(String DeleteSQLStatement) throws SQLException
    {
        PreparedStatement thePreparedStatement = theConnection.prepareStatement(DeleteSQLStatement);
        return thePreparedStatement.executeUpdate();
    }

    /**
     * Formatting value with ' and '
     * 
     * @param Value
     * @return
     */
    public String SQLFormat(String text)
    {
        if (text == null || text.trim() == "")
        {
            return "NULL";
        }
        else
        {
            return "'" + text.replace("'", "''") + "'";
        }
    }

    /**
     * Generic Table without null or ascending declarations
     * 
     * @param TableName
     * @param TableColumnNameAndType
     * @return 0 if can not do, 1 if successful
     * @throws SQLException
     */
    public int CreateTableNoRestrictions(String TableName, HashMap<String, Object> TableColumnNameAndType) throws SQLException
    {
        if (TableName.length() <= 0 || TableColumnNameAndType.size() == 0)
        {
            return 0;
        }
        String createTableSQL = "CREATE TABLE " + TableName + " (";
        String insideTheInsert = "";

        for (Map.Entry<String, Object> theMap : TableColumnNameAndType.entrySet())
        {
            String preparedAddition = "";
            String columnName = theMap.getKey();
            Object columnType = theMap.getValue();

            preparedAddition = preparedAddition + columnName + " ";

            if (columnType == String.class)
            {
                preparedAddition = preparedAddition + " VARCHAR(255)";

            }
            else if (columnType == int.class)
            {
                preparedAddition = preparedAddition + " INT(5)";
            }
            else if (columnType == double.class)
            {
                preparedAddition = preparedAddition + " DOUBLE(5, 2)";
            }
            else if (columnType == Date.class)
            {
                preparedAddition = preparedAddition + " DATETIME()";
            }
            else
            {
                preparedAddition = preparedAddition + " TEXT";
            }

            if (insideTheInsert.length() > 0)
            {
                preparedAddition = preparedAddition + " , ";
            }

            insideTheInsert = preparedAddition + insideTheInsert;

        }

        createTableSQL = createTableSQL + insideTheInsert + ")";
        PreparedStatement thePreparedStatement = theConnection.prepareStatement(createTableSQL);
        thePreparedStatement.executeUpdate();

        return 1;
    }

    /**
     * Private dropTable
     * 
     * @param TableName
     * @return
     * @throws SQLException
     */
    private int dropTable(String TableName) throws SQLException
    {
        PreparedStatement thePreparedStatement = theConnection.prepareStatement("DROP TABLE " + TableName);
        thePreparedStatement.executeUpdate();
        return 1;
    }

    /**
     * @param TableName
     *            TableName
     * @param Password
     *            Password to get the drop table
     * @return
     * @throws SQLException
     */
    public int DropTable(String TableName, String Password) throws SQLException
    {
        if (Password == "POS")
        {
            return dropTable(TableName);
        }
        else
        {
            return 0;
        }
    }

    public String GetColumnType(String TableName, String ColumnName) throws SQLException
    {
        ResultSet rs = SelectSQL("Select * FROM " + TableName.replace("'", "") + " WHERE Col_Name = " + SQLFormat(ColumnName));
        ResultSetMetaData rsMetaData = rs.getMetaData();
        return rsMetaData.getColumnTypeName(1);
    }

    public boolean AlterTableColumnChangeName(String TableName, String ColumnName, String NewColumnName) throws SQLException
    {
        String ColumnType = GetColumnType(TableName, ColumnName);
        return AlterTableChange(TableName, ColumnName, NewColumnName, ColumnType);
    }

    public boolean AlterTableColumnChangeNameAndType(String TableName, String ColumnName, String NewColumnName, String NewColumnType)
            throws SQLException
    {
        return AlterTableChange(TableName, ColumnName, NewColumnName, NewColumnType);
    }

    public boolean AlterTableColumnChangeType(String TableName, String ColumnName, String NewColumnType) throws SQLException
    {
        return AlterTableChange(TableName, ColumnName, ColumnName, NewColumnType);
    }

    private boolean AlterTableChange(String TableName, String ColumnName, String NewColumnName, String ColumnType) throws SQLException
    {
        ExecuteSQL("ALTER TABLE " + TableName + " CHANGE " + ColumnName + " " + ColumnName + " " + ColumnType);
        return true;
    }

    public boolean AddColumnToTable(String TableName, String NewColumnName, String ColumnType) throws SQLException
    {
        ExecuteSQL("ALTER TABLE " + TableName + " ADD " + NewColumnName + " " + ColumnType);
        return true;
    }

    //
    // public boolean DropColumnToTable(String TableName, String ColumnName) throws SQLException
    // {
    // if (theStringUtilities.DoesItContainCertainWord("droptable",ColumnName))
    // {
    // return false;
    // }
    // ExecuteSQL("ALTER TABLE " + TableName + " DROP " + ColumnName);
    // return true;
    // }
    //

    public int ProcedureAddMenu(String username, String password, String menuName) throws SQLException
    {
        String query = "{CALL AddMenu(?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setString("IMenuName", menuName);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        // boolean hadResults = cs.execute();
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }

    public int ProcedureAddMenuItem(String username, String password, String menuId, String itemName, String cost, String subMenu, String description, String imageUrl) throws SQLException
    {
        String query = "{CALL AddMenuItem(?,?,?,?,?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setInt("IMenuId", StringUtilities.parseMenuId(menuId));
        cs.setString("IItemName", itemName);
        cs.setDouble("ICost", StringUtilities.parseMenuItemCost(cost));
        cs.setString("ISubMenu", StringUtilities.makeEmptyStringNull(subMenu));
        cs.setString("IDescription", StringUtilities.makeEmptyStringNull(description));
        cs.setString("IImageUrl", imageUrl);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }

    public int ProcedureAddRestaurantUser(String username, String password, String newUsername, String restaurantPassword, String newPassword, String salt, String newEmail, String newType)
            throws SQLException
    {
        String query = "{CALL AddRestaurantUser(?,?,?,?,?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setString("INewUsername", newUsername);
        cs.setString("INewRestaurantPassword", restaurantPassword);
        cs.setString("INewPassword", newPassword);
        cs.setString("ISalt", salt);
        cs.setString("INewEmail", newEmail);
        cs.setString("INewType", newType);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public int ProcedureChangeMenu(String username, String password, String newMenuName, String menuId) throws SQLException
    {
        String query = "{CALL ChangeMenu(?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setString("INewMenuName", newMenuName);
        cs.setInt("IMenuId", StringUtilities.parseMenuId(menuId));
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public String[] ProcedureChangeMenuItem(String username, String password, String menuItemId, String menuId, String itemName, String cost, String subMenu, String description, String imageUrl)throws SQLException{
        String query = "{CALL ChangeMenuItem(?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setInt("IMenuItemId", StringUtilities.parseMenuId(menuItemId));
        cs.setInt("IMenuId", StringUtilities.parseMenuId(menuId));
        cs.setString("IItemName", itemName);
        cs.setDouble("ICost", StringUtilities.parseMenuItemCost(cost));
        cs.setString("ISubMenu", StringUtilities.makeEmptyStringNull(subMenu));
        cs.setString("IDescription", StringUtilities.makeEmptyStringNull(description));
        cs.setString("IImageUrl", imageUrl);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.registerOutParameter("OReturnImageDeleted", Types.VARCHAR);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        String outputValueString = String.valueOf(outputValue);
        String outputImage = cs.getString("OReturnImageDeleted");
        return new String[]{outputValueString, outputImage};
    }
    
    public int ProcedureChangeRestaurantUser(String username, String password, String oldUsername, String newUsername, String restaurantPassword, String newPassword, String salt, String newEmail, String newType)
            throws SQLException
    {
        String query = "{CALL ChangeRestaurantUser(?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setString("IOldUsername", oldUsername);
        cs.setString("INewUsername", newUsername);
        cs.setString("INewRestaurantPassword", restaurantPassword);
        cs.setString("INewPassword", newPassword);
        cs.setString("ISalt", salt);
        cs.setString("INewEmail", newEmail);
        cs.setString("INewType", newType);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public int ProcedureDeleteMenu(String username, String password, String menuId)throws SQLException{
        String query = "{CALL DeleteMenu(?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setInt("IMenuId", StringUtilities.parseMenuId(menuId));
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public String[] ProcedureDeleteMenuItem(String username, String password, String menuItemId)throws SQLException{
        String query = "{CALL DeleteMenuItem(?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setInt("IMenuItemId", StringUtilities.parseMenuId(menuItemId));
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.registerOutParameter("OReturnImageDeleted", Types.VARCHAR);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        String outputValueString = String.valueOf(outputValue);
        String outputImage = cs.getString("OReturnImageDeleted");
        return new String[]{outputValueString, outputImage};
    }
    
    public int ProcedureDeleteRestaurantUser(String username, String password, String usernameToDelete)throws SQLException{
        String query = "{CALL DeleteRestaurantUser(?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setString("IUsernameToDelete", usernameToDelete);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public int ProcedureRegistration(String username, String passwordHash, String salt, String email, String unverifiedHash)throws SQLException{
        String query = "{CALL Registration(?,?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("INewUsername", username);
        cs.setString("INewPasswordHash", passwordHash);
        cs.setString("ISalt", salt);
        cs.setString("INewEmail", email);
        cs.setString("IUnverifiedHash", unverifiedHash);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public int ProcedureResetPassword(String email, String newGeneratedPassword, String newSalt, String newHashedPassword)throws SQLException{
        String query = "{CALL ResetPassword(?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IEmail", email);
        cs.setString("INewGeneratedPassword", newGeneratedPassword);
        cs.setString("INewGeneratedSalt", newSalt);
        cs.setString("INewGeneratedHashedPassword", newHashedPassword);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
  
    public int ProcedureValidateEmail(String unverifiedHash)throws SQLException{
        String query = "{CALL ValidateEmail(?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUnverifiedHash ", unverifiedHash);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public int ProcedureOrderStatusChanged(String username, String password, String orderId, String newStatusName, String previousStatusName)throws SQLException
    {
        String query = "{CALL OrderStatusChanged(?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.setInt("IOrderId", StringUtilities.parseMenuId(orderId));
        cs.setString("INewStatusName", newStatusName);
        cs.setString("ICurrentStatusName", previousStatusName);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);
        cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
    
    public ReturnCodeResultSet GetTableLayout(String username, String password)throws SQLException
    {
        String query = "{CALL GetTableLayout(?,?,?,?,?)}";
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString("IUsername", username);
        cs.setString("IPassword", password);
        cs.registerOutParameter("OReturnCode", Types.INTEGER);cs.registerOutParameter("OGridWidth", Types.INTEGER);
        cs.registerOutParameter("OGridHeight", Types.INTEGER); 
        MySQLUtilities.ReturnCodeResultSet rcrs = new MySQLUtilities.ReturnCodeResultSet();
        rcrs.resultSet = cs.executeQuery();
        rcrs.returnCode = cs.getInt("OReturnCode");
        rcrs.gridWidth = cs.getInt("OGridWidth");
        rcrs.gridHeight = cs.getInt("OGridHeight");
        return rcrs;
    }

    /**
     * Closes the connection
     */
    public void close()
    {
        try
        {
            theConnection.close();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            theConnection = null;
        }
    }

    /**
     * Overrides the finalize object which calls the close function
     */
    protected void finalize()
    {
        close();
    }
    
    public class ReturnCodeResultSet
    {
        public int returnCode;
        public int gridWidth;
        public int gridHeight;
        public ResultSet resultSet;
    }

}

