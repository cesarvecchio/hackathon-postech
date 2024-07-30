package br.fiap.hackathonpostech.domain.entity;

import br.fiap.hackathonpostech.domain.enums.MetodoPagamentoEnum;
import br.fiap.hackathonpostech.domain.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Pagamento {
    private UUID id;
    private Double valor;
    private String descricao;
    private MetodoPagamentoEnum metodoPagamento;
    private StatusEnum status;
    private String cpf;
    private String numero;
    private String dataValidade;
    private String cvv;
}
