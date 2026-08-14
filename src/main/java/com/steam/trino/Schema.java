package com.steam.trino;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;

import com.steam.config.EnvConfig;
import com.steam.config.LoaderYaml;

public class Schema {
    private static Map<String, Object> config = LoaderYaml.loadConfig(EnvConfig.get("SCHEMA_PATH"));
    private static final String catalog = EnvConfig.get("TRINO_CATALOG");
    private static final String schema = EnvConfig.get("TRINO_SCHEMA");
    public static void createSchema() throws Exception {
        String sql = "create schema if not exists " + catalog + "." + schema;
        try (Connection conn = TrinoClient.getConnection();
            Statement stmt = conn.createStatement();
        ) {
            stmt.execute(sql);
        }
    }
    public static void createTable(String columns, String path) throws Exception{
        String sql = String.format(
        "create table if not exists %s.%s.%s (%s) " +
        "with (format = 'parquet', external_location='%s')",
        catalog, schema, EnvConfig.get("TRINO_TABLE"), columns, path);
        try (
                Connection conn = TrinoClient.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
        }
    }
    public static void main(String[] args) {
        try {
            createSchema();
            Map<String, Object> columnConfig = (Map<String, Object>) config.get("columns");
            String columns = columnConfig.entrySet().stream()
                            .map(entry-> entry.getKey() + " " + entry.getValue())
                            .collect(Collectors.joining(", "));
            String path = String.format((String) config.get("path"), EnvConfig.get("BUCKET_NAME"));
            createTable(columns, path);
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
