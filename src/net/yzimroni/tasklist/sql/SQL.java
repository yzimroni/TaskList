package net.yzimroni.tasklist.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

	private String host;
	private int port;
	private String database;
	private String username;
	private String password;

	private Connection connection = null;

	public SQL(String host, int port, String database, String username, String password) throws Exception {
		if (!checkDriver()) {
			throw new RuntimeException("MySQL Driver not found");
		}
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		open();
	}

	private boolean checkDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Connection open() throws Exception {
		this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
				password);
		return connection;
	}

	public boolean hasConnection() {
		if (this.connection != null) {
			return true;
		}
		return false;
	}

	private boolean checkConncection() {
		try {
			return this.connection != null && !this.connection.isClosed() && this.connection.isValid(2000);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Connection getConnection() {
		if (!checkConncection()) {
			try {
				open();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.connection;
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection = null;
	}

	public PreparedStatement getPrepare(String s) {
		try {
			return getConnection().prepareStatement(s);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getPrepareReturnId(String s) {
		try {
			PreparedStatement p = getPrepareAutoKeys(s);
			p.executeUpdate();
			return getIdFromPrepared(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int getIdFromPrepared(PreparedStatement p) {
		try {
			ResultSet rs = p.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public PreparedStatement getPrepare(String s, int autoGeneratedKeys) {
		try {
			return getConnection().prepareStatement(s, autoGeneratedKeys);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PreparedStatement getPrepareAutoKeys(String s) {
		return getPrepare(s, Statement.RETURN_GENERATED_KEYS);
	}

}