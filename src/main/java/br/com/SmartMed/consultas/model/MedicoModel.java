package br.com.SmartMed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medico")
public class MedicoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    @NotNull(message = "O campo nome não pode ser vazio")
    @NotBlank(message = "O campo nome não pode ser branco")
    private String nome;

    @Column(name = "crm", length = 8, nullable = false)
    @NotNull(message = "O campo crm não pode ser vazio")
    @NotBlank(message = "O campo crm não pode ser branco")
    private String crm;

    @Column(name = "telefone", length = 11, nullable = false)
    @NotNull(message = "O campo email não pode ser vazio")
    @NotBlank(message = "O campo email não pode ser branco")
    @Length(min = 11)
    private String telefone;

    @Column(name = "email", length = 65, nullable = false)
    @NotNull(message = "O campo email não pode ser vazio")
    @NotBlank(message = "O campo email não pode ser branco")
    @Email(message = "Invalido")
    private String email;

    @Column(name = "valorConsultaReferencia")
    private float valorConsultaReferencia;

    @Column(name = "ativo")
    private boolean ativo;

    @Column(name = "especialidadeID", nullable = false)
    private int especialidadeID;

    @Column(name = "horarioInicioAtendimento", nullable = false)
    private LocalTime horarioInicioAtendimento = LocalTime.of(8, 0);

    @Column(name = "horarioFimAtendimento", nullable = false)
    private LocalTime horarioFimAtendimento = LocalTime.of(18, 0);


    /*public MedicoDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, MedicoDTO.class);
    }*/
}
