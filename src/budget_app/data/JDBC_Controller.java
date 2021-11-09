package budget_app.data;

import java.sql.*;

public class JDBC_Controller {

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    String password = "Providence@001";

    public JDBC_Controller() {

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            String connectionString = "jdbc:mysql://localhost/budgeting_app?"
                    + "user=root&password=" + password
                    + "&useSSL=false&allowPublicKeyRetrieval=true";

            connection = DriverManager.getConnection(connectionString);

        } catch (SQLException exc) {
            System.out.println("Connecting to DB Exception occurred");
            //exc.printStackTrace();
            try {
                // close connections
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("connection did not close");
            }
            System.out.println("Exiting Budget Application");
            System.exit(1);

        } catch (ClassNotFoundException e) {
            System.out.println("Exception occured - driver not found on classpath");
            e.printStackTrace();
        }
    }

    //runs a DB Query
    public void executeSQL(String query) throws SQLException{

            // Statements allow us to issue SQL queries to the database
            statement = connection.createStatement();
            // Execute the query on the Statement, set ResultSet variable
            resultSet = statement.executeQuery(query);
    }

    //runs a DB insert
    public int updateSQL(String query) throws SQLException{

        // Statements allow us to issue SQL queries to the database
        statement = connection.createStatement();
        // Execute the query on the Statement, set ResultSet variable
        return statement.executeUpdate(query);
    }

    //returns current resultSet
    public ResultSet getResultSet() {
        return resultSet;
    }

    public void closeConnections() throws SQLException{

        if (resultSet != null)
            resultSet.close();
        if (statement != null)
            statement.close();
        if (connection != null)
            connection.close();
    }
}
