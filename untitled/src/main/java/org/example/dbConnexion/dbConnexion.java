package org.example.dbConnexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnexion {
    private final static int defaultPort = 5432;
    private final String host = "localhost";
    private final String user = "postgres";
    private final String password = "smellgood";
    private final String database = "restaurant";
    private final String jdbcUrl;

    public dbConnexion() {
        jdbcUrl = "jdbc:postgresql://" + host + ":" + defaultPort + "/" + database;
        System.out.println("JDBC URL: " + jdbcUrl);
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }
}
