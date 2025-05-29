package br.com.SmartMed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "covenio")
public class CovenioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    @NotNull(message = "Invalido")
    @NotBlank(message = "Invalido")
    private String nome;

    @Column(name = "cnpj", length = 14, nullable = false)
    @NotNull(message = "Invalido")
    @CNPJ(message = "Invalido")
    private String cnpj;

    @Column(name = "telefone", length = 11)
    @NotBlank(message = "Invalido")
    @Length(min = 11,message = "Invalido")
    private String telefone;

    @Column(name = "email", length = 54)
    @Email(message = "Invalido")
    private String email;

    @Column(name = "ativo")
    private boolean ativo;
}
