package br.fiap.hackathonpostech.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class CartaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cpf;
    private Integer limite;
    private String numero;
    private String dataValidade;
    private String cvv;
    @ManyToOne
    @JoinColumn(name = "clienteId")
    private ClienteEntity cliente;

    public CartaoEntity(Integer id, String cpf, Integer limite, String numero, String dataValidade, String cvv) {
        this.id = id;
        this.cpf = cpf;
        this.limite = limite;
        this.numero = numero;
        this.dataValidade = dataValidade;
        this.cvv = cvv;
    }
}
