import java.sql.*;

public class SQL {

	private Connection connection;
	private Statement statement;

	public SQL() {
		try {
			// Load the MySQL JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to load MySQL JDBC driver");
			e.printStackTrace();
		}

		String url = "jdbc:mysql://localhost:3306/shopping_cart_db";
		String username = "root";
		String password = "pranjal@214022";

		try {
			// Create a connection to the MySQL database
			connection = DriverManager.getConnection(url, username, password);
			statement = connection.createStatement();
			System.out.println("Connected to the MySQL database");
		} catch (SQLException e) {
			System.out.println("Failed to connect to the MySQL database");
			e.printStackTrace();
		}
	}

	public void executeUpdate(String sql) {
		try {
			statement.executeUpdate(sql);
			System.out.println("Executed SQL statement: " + sql);
		} catch (SQLException e) {
			System.out.println("Failed to execute SQL statement: " + sql);
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String sql) {
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sql);
			System.out.println("Executed SQL query: " + sql);
		} catch (SQLException e) {
			System.out.println("Failed to execute SQL query: " + sql);
			e.printStackTrace();
		}
		return resultSet;
	}

	public void close() {
		try {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
				System.out.println("Disconnected from the MySQL database");
			}
		} catch (SQLException e) {
			System.out.println("Failed to close the connection");
			e.printStackTrace();
		}
	}
}
