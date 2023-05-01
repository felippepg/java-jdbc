package br.com.alura.bytebank.domain;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoDB {
    public static void main(String... x) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Bytebank?user=root&password=password"
            );
            System.out.println("Recuperei  a conexão");
            connection.close();
        } catch (Exception e) {
            System.out.println("Erro ao acessar o banco " + e.getMessage() );
        }
    }
}
