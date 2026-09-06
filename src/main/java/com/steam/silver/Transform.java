package com.steam.silver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.col;
import org.apache.spark.sql.types.StructField;
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
        SparkSession spark = sparkConfig.getSparkSession();
        Dataset<Row> df = spark.read().parquet(bronzePath);
        df = process(df);
        df.write().mode("overwrite").parquet(silverPath);
    }
    private Dataset<Row> process(Dataset<Row> df) {
        Map<String, Object> schema = (Map<String, Object>) config.get("schema");
        StructType sparkSchema  = schemaConfig.buildSchema(schema);
        List<Column> selectExprs = new ArrayList<>();
        for (StructField field : sparkSchema.fields()) {
            selectExprs.add(col(field.name()).cast(field.dataType()).as(field.name()));
        }
        df = df.select(selectExprs.toArray(new Column[0]));
        Map<String, Object> processes = (Map<String, Object>) config.get("process");
        Map<String, Object> filters = (Map<String, Object>) processes.get("filters");
        String filterColumn = (String) filters.get("name");
        df = df.filter(col(filterColumn).isNotNull());
        for (Map.Entry<String, Object> entry : processes.entrySet()) {
            if(entry.getKey().equals("filters")) {
                continue;
            }
            Map<String, Object> rule = (Map<String, Object>) entry.getValue();
            String condition = (String) rule.get("condition");
            df = df.filter(condition);
        }
        return df;
    }
    public static void main(String[] args) {
        Transform app = new Transform();
        app.run();
    } 
}
