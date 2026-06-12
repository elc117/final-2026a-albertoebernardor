package com.divideai.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    // Bernardo isso aqui usa o dotenv que botei no pom.xml, usa pra puxar as informações pra conectar com o banco no render de um arquivo .env, se for precisar me manda msg no zap.
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private static final String URL  = getEnv("DB_URL",  "jdbc:postgresql://localhost:5432/divideai");
    private static final String USER = getEnv("DB_USER", "postgres");
    private static final String PASS = getEnv("DB_PASS", "postgres");

    private static String getEnv(String key, String fallback) {
        String val = dotenv.get(key, null);
        return val != null ? val : System.getenv().getOrDefault(key, fallback);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
