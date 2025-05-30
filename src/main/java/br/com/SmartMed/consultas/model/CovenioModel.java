package br.com.SmartMed.consultas.model;

import br.com.SmartMed.consultas.rest.dto.CovenioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.modelmapper.ModelMapper;

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
    @NotNull(message = "O campo nome não pode ser vazio")
    @NotBlank(message = "O campo nome não pode ser branco")
    private String nome;

    @Column(name = "cnpj", length = 14, nullable = false)
    @NotNull(message = "O campo cnpj não pode ser vazio")
    @NotBlank(message = "O campo cnpj não pode ser branco")
    @CNPJ(message = "Invalido")
    private String cnpj;

    @Column(name = "telefone", length = 11, nullable = false)
    @NotNull(message = "O campo telefone não pode ser vazio")
    @NotBlank(message = "O campo telefone não pode ser branco")
    @Length(min = 11,message = "Invalido")
    private String telefone;

    @Column(name = "email", length = 54, nullable = false)
    @NotNull(message = "O campo email não pode ser vazio")
    @NotBlank(message = "O campo email não pode ser branco")
    @Email(message = "Invalido")
    private String email;

    @Column(name = "ativo")
    private boolean ativo;

    public CovenioDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, CovenioDTO.class);
    }
}
