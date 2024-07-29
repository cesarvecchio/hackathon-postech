package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;

import java.util.List;

public interface CartaoGateway {
    void gerarCartao(Cartao cartao, Cliente cliente);

    List<Cartao> buscarCartoesPorCpf(String cpf);

    boolean existeCartaoPorNumero(String numero);
}
