package br.com.SmartMed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Medico")
public class MedicoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "crm", length = 8, nullable = false)
    private String crm;

    @Column(name = "telefone", length = 11,unique = false)
    @Length(min = 11)
    @NotBlank(message = "Invalido")
    private String telefone;

    @Column(name = "email", length = 65)
    @NotNull(message = "Invalido")
    @Email(message = "Invalido")
    private String email;

    @Column(name = "valorConsultaReferencia")
    @NotBlank(message = "Invalido")
    private float valorConsultaReferencia;

    @Column(name = "ativo")
    @NotBlank(message = "Invalido")
    private boolean ativo;

    @Column(name = "especialidadeID", nullable = false)
    @NotNull(message = "Invalido")
    @NotBlank(message = "Invalido")
    private int especialidadeID;
}
