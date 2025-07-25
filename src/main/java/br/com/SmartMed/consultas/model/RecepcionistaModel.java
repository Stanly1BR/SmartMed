package br.com.SmartMed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @NotNull(message = "O campo nome não pode ser vazio")
    @NotBlank(message = "O campo nome não pode ser branco")
    private String nome;

    @Column(name = "cpf", length = 11, unique = true, nullable = false)
    @NotNull(message = "O campo cpf não pode ser vazio")
    @NotBlank(message = "O campo cpf não pode ser branco")
    @CPF(message = "Invalido")
    private String cpf;

    @Column(name = "dataNascimento", nullable = false)
    @NotNull(message = "O campo dataNAscimento não pode ser vazio")
    private LocalDate dataNascimento;

    @Column(name = "dataAdmissao", nullable = false)
    @NotNull(message = "O campo dataAdimissão não pode ser vazio")
    private LocalDate dataAdmissao;

    @Column(name = "dataDemissao")
    private LocalDate dataDemissao;

    @Column(name = "telefone", length = 11)
    @NotNull(message = "O campo telefone não pode ser vazio")
    @NotBlank(message = "O campo telefone não pode ser branco")
    @Length(min = 11, message = "Invalido")
    private String telefone;

    @Column(name = "email", length = 64)
    @Email(message = "Invalido")
    private String email;

    @Column(name = "ativo")
    private boolean ativo;

    /*public RecepcionistaDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, RecepcionistaDTO.class);
    }*/
}
