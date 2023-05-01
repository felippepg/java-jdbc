package br.com.alura.bytebank.domain;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public Connection recuperarConexao() {
        try {
            return createDataSource().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/Bytebank?user=root&password=password");
        config.setUsername("root");
        config.setPassword("password");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

}
