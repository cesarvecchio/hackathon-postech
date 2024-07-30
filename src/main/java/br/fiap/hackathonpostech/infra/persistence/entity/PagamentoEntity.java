package br.fiap.hackathonpostech.infra.persistence.entity;

import br.fiap.hackathonpostech.domain.enums.MetodoPagamentoEnum;
import br.fiap.hackathonpostech.domain.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class PagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double valor;
    private String descricao;
    private MetodoPagamentoEnum metodoPagamento;
    private StatusEnum status;
    @ManyToOne
    @JoinColumn(name = "cartaoId")
    private CartaoEntity cartao;

    public PagamentoEntity(UUID id, Double valor, String descricao, MetodoPagamentoEnum metodoPagamento, StatusEnum status) {
        this.id = id;
        this.valor = valor;
        this.descricao = descricao;
        this.metodoPagamento = metodoPagamento;
        this.status = status;
    }
}
