package com.steam.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory(".")
            .load();
    public static String get(String key) {
        return dotenv.get(key);
    }
}
