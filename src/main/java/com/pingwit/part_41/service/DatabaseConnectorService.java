package com.pingwit.part_41.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectorService {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/pingwit_maven_video";
    private static final String USER = "postgres";
    private static final String PASSWORD = "docker";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
