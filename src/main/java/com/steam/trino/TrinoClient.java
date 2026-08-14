package com.steam.trino;

import java.sql.Connection;
import java.sql.DriverManager;

import com.steam.config.EnvConfig;

public class TrinoClient {
    private static final String URL = "jdbc:trino://" + EnvConfig.get("TRINO_HOST") + ":" + EnvConfig.get("TRINO_PORT");
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, EnvConfig.get("TRINO_USER"), null);
    }
}
