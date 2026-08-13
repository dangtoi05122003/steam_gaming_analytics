package com.steam.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class SchemaConfig {
    private final Map<String, DataType> typeMap = new HashMap<>();
    public SchemaConfig() {
        typeMap.put("string", DataTypes.StringType);
        typeMap.put("long", DataTypes.LongType);
        typeMap.put("int", DataTypes.IntegerType);
        typeMap.put("double", DataTypes.DoubleType);
        typeMap.put("boolean", DataTypes.BooleanType);
        typeMap.put("timestamp", DataTypes.TimestampType);
    }
    public StructType buildSchema(Map<String, Object> schema) {
        List<StructField> fields = new ArrayList<>();
        for (Map.Entry<String, Object> entry: schema.entrySet()) {
            String name = entry.getKey();
            String type = entry.getValue().toString();
            DataType dataType = typeMap.get(type);
            fields.add(DataTypes.createStructField(name, dataType, true));
        }
        return DataTypes.createStructType(fields);
    }
}
