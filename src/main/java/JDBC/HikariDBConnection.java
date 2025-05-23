package JDBC;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

    public class HikariDBConnection {
        private static HikariDataSource dataSource;

        static {
            try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties props = new Properties();
                props.load(input);

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(props.getProperty("db.url"));
                config.setUsername(props.getProperty("db.username"));
                config.setPassword(props.getProperty("db.password"));

                // Connection pool settings
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(5);
                config.setIdleTimeout(300000); // 5 minutes
                config.setConnectionTimeout(20000); // 20 seconds
                config.setPoolName("FeedbackAppPool");

                // Optional performance settings
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                Class.forName("com.mysql.cj.jdbc.Driver");

                dataSource = new HikariDataSource(config);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error initializing connection pool: " + e.getMessage());
            }
        }

        public static Connection getConnection() throws SQLException {
            if (dataSource == null) {
                throw new SQLException("Data source is not initialized");
            }
            return dataSource.getConnection();
        }

        // Add this method to properly close the pool when application stops
        public static void closePool() {
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
            }
        }
    }