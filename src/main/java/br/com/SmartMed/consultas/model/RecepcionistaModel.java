package br.com.SmartMed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recepcionista")
public class RecepcionistaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 255)
    @NotNull(message = "Invalido")
    @NotBlank(message = "Invalido")
    private String nome;

    @Column(name = "cpf", length = 11, unique = false, nullable = false)
    @NotNull(message = "Invalido")
    @CPF(message = "Invalido")
    private String cpf;

    @Column(name = "dataNascimento", nullable = false)
    @NotNull(message = "Invalido")
    private LocalDate dataNascimento;

    @Column(name = "dataAdmissao", nullable = false)
    @NotNull(message = "Invalido")
    private LocalDate dataAdmissao;

    @Column(name = "dataDemissao", nullable = false)
    private LocalDate dataDemissao;

    @Column(name = "telefone", length = 11)
    @Length(min = 11, message = "Invalido")
    private String telefone;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "ativo")
    private boolean ativo;
}
