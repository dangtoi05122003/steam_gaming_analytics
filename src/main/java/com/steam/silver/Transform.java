package com.steam.silver;

import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.col;
import org.apache.spark.sql.types.StructType;

import com.steam.config.EnvConfig;
import com.steam.config.LoaderYaml;
import com.steam.config.SchemaConfig;
import com.steam.config.SparkConfig;

public class Transform {
    private final SparkConfig sparkConfig = new SparkConfig();
    private final SchemaConfig schemaConfig = new SchemaConfig();
    private static Map<String, Object> config = LoaderYaml.loadConfig(EnvConfig.get("SILVER_PATH"));
    public void run() {
        String bronzePath = String.format((String) ((Map<String, Object>) config.get("path")).get("bronze_path"), EnvConfig.get("BUCKET_NAME"));
        String silverPath = String.format((String) ((Map<String, Object>) config.get("path")).get("silver_path"), EnvConfig.get("BUCKET_NAME"));
        Map<String, Object> schema = (Map<String, Object>) config.get("schema");
        StructType sparkSchema  = schemaConfig.buildSchema(schema);
        SparkSession spark = sparkConfig.getSparkSession();
        Dataset<Row> df = spark.read().schema(sparkSchema).parquet(bronzePath);
        List<Map<String, Object>> processes = (List<Map<String, Object>>) config.get("process");
        for (Map<String, Object> process: processes) {
            String column = (String) process.get("column");
            df = df.filter(col(column).isNotNull());
        }
        df.write().mode("overwrite").parquet(silverPath);
    }
    public static void main(String[] args) {
        Transform app = new Transform();
        app.run();
    } 
}
