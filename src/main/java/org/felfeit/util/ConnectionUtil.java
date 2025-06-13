package org.felfeit.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionUtil {
    private static HikariDataSource dataSource;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/belajar_java_database";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(MYSQL_DRIVER);
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
