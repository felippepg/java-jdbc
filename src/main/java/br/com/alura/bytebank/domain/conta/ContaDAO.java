package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
    private Connection connection;
    public ContaDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(DadosAberturaConta dadosDaConta) {
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente, true);

        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email, esta_ativa)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, dadosDaConta.numero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5, dadosDaConta.dadosCliente().email());
            preparedStatement.setBoolean(6, true);
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Set<Conta> show() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        Set<Conta> contas = new HashSet<>();
        String sql = "SELECT * FROM conta WHERE esta_ativa = true";
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtivo = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);
                contas.add(new Conta(numero, saldo, new Cliente(dadosCadastroCliente), estaAtivo));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contas;
    }

    public void realizarDeposito(int numeroConta, BigDecimal valor) {
        PreparedStatement preparedStatement;
        try {
            String sql = "UPDATE conta SET saldo = ? WHERE numero = ? and esta_ativa = true";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(2, numeroConta);
            preparedStatement.setBigDecimal(1, valor);
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Conta buscarPorNumero(int numeroConta) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Conta conta = null;
        try {
            String sql = "SELECT * FROM conta WHERE numero = ? and esta_ativa = true";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, numeroConta);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtivo = resultSet.getBoolean(6);

                Cliente titular = new Cliente(new DadosCadastroCliente(nome, cpf, email));
                
                conta = new Conta(numero, saldo, titular, estaAtivo);
            }
            
            preparedStatement.close();
            resultSet.close();
            connection.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return conta;
    }

    public void sacar(int numeroDaConta, BigDecimal valor) {
        PreparedStatement preparedStatement;
        try {
            String sql = "UPDATE conta SET saldo = ? WHERE numero = ? and esta_ativa = true";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(2, numeroDaConta);
            preparedStatement.setBigDecimal(1, valor);
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Integer numeroDaConta) {
        PreparedStatement preparedStatement;
        try {
            String sql = "DELETE FROM conta WHERE numero = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, numeroDaConta);
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void inativar(int numeroDaConta) {
        PreparedStatement preparedStatement;
        try {
            String sql = "UPDATE conta SET esta_ativa = false WHERE numero = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, numeroDaConta);
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

