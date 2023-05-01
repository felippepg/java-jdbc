package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ContaService {
    private ConnectionFactory connectionFactory;



    public ContaService(){
        this.connectionFactory = new ConnectionFactory();
    };

    private Set<Conta> contas = new HashSet<>();

    public Set<Conta> listarContasAbertas() {
        Connection con = connectionFactory.recuperarConexao();
        ContaDAO contaDAO = new ContaDAO(con);
        return contaDAO.show();
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {
        Connection con = connectionFactory.recuperarConexao();
        ContaDAO contaDAO = new ContaDAO(con);
        contaDAO.add(dadosDaConta);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        Connection connection = connectionFactory.recuperarConexao();
        ContaDAO contaDAO = new ContaDAO(connection);

        var conta = buscarContaPorNumero(numeroDaConta);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        BigDecimal valorASacar = conta.getSaldo().subtract(valor);
        contaDAO.sacar(numeroDaConta, valorASacar);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        Connection con = connectionFactory.recuperarConexao();
        ContaDAO contaDAO = new ContaDAO(con);
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        BigDecimal valorAdepositar = conta.getSaldo().add(valor);
        contaDAO.realizarDeposito(numeroDaConta, valorAdepositar);
    }

    public void inativar(Integer numeroDaConta) {
        ContaDAO contaDAO = new ContaDAO(connectionFactory.recuperarConexao());

        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contaDAO.inativar(numeroDaConta);

    }

    public void excluir(Integer numeroDaConta) {
        ContaDAO contaDAO = new ContaDAO(connectionFactory.recuperarConexao());

        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contaDAO.excluir(numeroDaConta);

    }

    public Conta buscarContaPorNumero(Integer numero) {
        Connection connection = connectionFactory.recuperarConexao();
        ContaDAO contaDAO = new ContaDAO(connection);
        Optional<Conta> conta = Optional.ofNullable(contaDAO.buscarPorNumero(numero));
        if(conta.isEmpty()) {
            throw new  RegraDeNegocioException("Não existe conta cadastrada com esse número!");
        } else {
            return conta.get();
        }
    }

    public Boolean realizarTransferencia(int numeroDaMinhaConta, int numeroContaDestino, BigDecimal valor) {
        try {
            this.realizarSaque(numeroDaMinhaConta, valor);
            this.realizarDeposito(numeroContaDestino, valor);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
