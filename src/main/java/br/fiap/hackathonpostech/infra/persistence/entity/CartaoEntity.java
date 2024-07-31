package br.fiap.hackathonpostech.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity(name = "TB_CARTAO")
public class CartaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String cpf;
    private Integer limite;
    private String numero;
    private String dataValidade;
    private String cvv;
    @ManyToOne
    @JoinColumn(name = "clienteId")
    private ClienteEntity cliente;
    @OneToMany(mappedBy = "cartao")
    private Set<PagamentoEntity> pagamentos;

    public CartaoEntity(UUID id, String cpf, Integer limite, String numero, String dataValidade, String cvv) {
        this.id = id;
        this.cpf = cpf;
        this.limite = limite;
        this.numero = numero;
        this.dataValidade = dataValidade;
        this.cvv = cvv;
    }
}
