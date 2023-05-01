package br.com.alura.bytebank.domain;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public Connection recuperarConexao() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Bytebank?user=root&password=password"
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
