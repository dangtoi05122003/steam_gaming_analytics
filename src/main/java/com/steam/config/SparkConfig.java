package com.steam.config;
import org.apache.spark.sql.SparkSession;

public class SparkConfig {
    String endpoint = EnvConfig.get("MINIO_ENDPOINT");
    String accessKey = EnvConfig.get("MINIO_ACCESS_KEY");
    String secretKey = EnvConfig.get("MINIO_ROOT_PASSWORD");
    private SparkSession spark;
    public SparkConfig() {
        this.spark = SparkSession.builder()
            .master("local[*]")
            .config("spark.driver.memory", "8g")
            .config("spark.hadoop.fs.s3a.endpoint", endpoint)
            .config("spark.hadoop.fs.s3a.access.key", accessKey)
            .config("spark.hadoop.fs.s3a.secret.key", secretKey)
            .config("spark.hadoop.fs.s3a.path.style.access", "true")
            .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
            .appName("steam").getOrCreate();
    }
    public SparkSession getSparkSession() {
        return this.spark;
    }
}
