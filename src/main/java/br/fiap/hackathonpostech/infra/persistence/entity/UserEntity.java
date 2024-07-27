package br.fiap.hackathonpostech.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {
    @Id
    private Integer id;
    private String usuario;
    private String senha;

    public UserEntity(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }
}
