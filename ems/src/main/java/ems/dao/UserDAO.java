package ems.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ems.model.*;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";

	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES "+ " (?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";



	protected Connection getConnection() throws Exception {
		
		Connection connection = null;
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		return connection;
	}

	public void insertUser(User user) throws Exception {
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL); 
		preparedStatement.setString(1, user.getName());
		preparedStatement.setString(2, user.getEmail());
		preparedStatement.setString(3, user.getCountry());
		System.out.println(preparedStatement);
		preparedStatement.executeUpdate();
	}

	public User selectUser(int id) throws Exception {
		
		User user = null;
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID); 
		preparedStatement.setInt(1, id);
		System.out.println(preparedStatement);			
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			String name = rs.getString("name");
			String email = rs.getString("email");
			String country = rs.getString("country");
			user = new User(id, name, email, country);
		}
		return user;
	}

	public List<User> selectAllUsers() throws Exception {
		
		List<User> users = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS); 
		System.out.println(preparedStatement);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String country = rs.getString("country");
			users.add(new User(id, name, email, country));
		}	
		return users;
	}

	public boolean deleteUser(int id) throws Exception {
		
		boolean rowDeleted;
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);
		statement.setInt(1, id);
		rowDeleted = statement.executeUpdate() > 0;
		return rowDeleted;
	}

	public boolean updateUser(User user) throws Exception {
		
		boolean rowUpdated;
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);
		statement.setString(1, user.getName());
		statement.setString(2, user.getEmail());
		statement.setString(3, user.getCountry());
		statement.setInt(4, user.getId());
		rowUpdated = statement.executeUpdate() > 0;
		return rowUpdated;
	}

	private void printSQLException(SQLException ex) {
		
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

}

