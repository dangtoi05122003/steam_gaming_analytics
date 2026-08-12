package com.steam.bronze;

import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.steam.config.EnvConfig;
import com.steam.config.LoaderYaml;
import com.steam.config.SparkConfig;

public class Ingestion {
    private final SparkConfig sparkconfig = new SparkConfig();
    private static Map<String, Object> config = LoaderYaml.loadConfig(EnvConfig.get("BRONZE_PATH"));
    public void run() {
        String localPath = (String) config.get("local_path");
        String bronzePath = String.format((String) config.get("bronze_path"),EnvConfig.get("BUCKET_NAME"));
        SparkSession spark = sparkconfig.getSparkSession();
        Dataset<Row> df = spark.read().option("header", "true").csv(localPath);
        df.write().mode("overwrite").parquet(bronzePath);
    } 
    public static void main(String[] args) {
        Ingestion app = new Ingestion();
        app.run();
    }    
}