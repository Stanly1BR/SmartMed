package br.com.SmartMed.consultas.model;


import br.com.SmartMed.consultas.rest.dto.PacienteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.Database;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Paciente")
public class PacienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    @NotBlank(message = "Invalido")
    @NotNull(message = "Invalido")
    private String nome;

    @Column(name = "cpf", length = 11, nullable = false, unique = false)
    @NotNull(message = "Invalido")
    @CPF
    private String cpf;

    @Column(name = "dataNascimento")
    private Database dataNascimento;

    @Column(name = "telefone", length = 11, nullable = false)
    @NotBlank(message = "Invalido")
    @Length(min = 11,message = "Invalido")
    private String telefone;

    @Column(name = "email", length = 64, nullable = true)
    @NotBlank(message = "Invalido")
    @Email(message = "Invalido")
    private String email;

    public PacienteDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return  m.map(this, PacienteDTO.class);
    }
}
