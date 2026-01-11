package com.onriderentals.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String PORT;

    static {
        try {
            Properties properties = new Properties();
            InputStream input = Database.class.getClassLoader().getResourceAsStream("database.properties");
            if (input == null) {
                throw new RuntimeException("database.properties file not found");
            }
            properties.load(input);

            String host = properties.getProperty("MASTER_DB_HOST");
            String port = properties.getProperty("MASTER_DB_PORT", "3306");
            String database = properties.getProperty("MASTER_DB_NAME");
            USER = properties.getProperty("MASTER_DB_USER");
            PASSWORD = properties.getProperty("MASTER_DB_PASSWORD");

            URL = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=true&serverTimezone=UTC";

            input.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
