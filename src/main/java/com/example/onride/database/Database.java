package com.example.onride.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find database.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static final String HOST = properties.getProperty("MASTER_DB_HOST");
    private static final String PORT = properties.getProperty("MASTER_DB_PORT");
    private static final String DBNAME = properties.getProperty("MASTER_DB_NAME");
    private static final String USER = properties.getProperty("MASTER_DB_USER");
    private static final String PASSWORD = properties.getProperty("MASTER_DB_PASSWORD");

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DBNAME + "?useSSL=false";

    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to database: " + URL);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}
