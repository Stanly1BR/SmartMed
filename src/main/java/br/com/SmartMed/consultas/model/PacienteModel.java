package br.com.SmartMed.consultas.model;

import br.com.SmartMed.consultas.rest.dto.PacienteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paciente")
public class PacienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    @NotNull(message = "O campo nome não pode ser vazio")
    @NotBlank(message = "O campo nome não pode ser branco")
    private String nome;

    @Column(name = "cpf", length = 11, nullable = false, unique = false)
    @NotNull(message = "O campo cpf não pode ser vazio")
    @NotBlank(message = "O campo cpf não pode ser branco")
    @CPF
    private String cpf;

    @Column(name = "dataNascimento")
    @NotNull(message = "O campo dataNascimento não pode ser vazio")
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 11, nullable = false)
    @NotNull(message = "O campo telefone não pode ser vazio")
    @NotBlank(message = "O campo telefone não pode ser branco")
    @Length(min = 11,message = "Invalido")
    private String telefone;

    @Column(name = "email", length = 64, nullable = true)
    @NotNull(message = "O campo email não pode ser vazio")
    @NotBlank(message = "O campo email não pode ser branco")
    @Email(message = "Invalido")
    private String email;

    /*public PacienteDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return  m.map(this, PacienteDTO.class);
    }*/
}
