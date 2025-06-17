package br.com.SmartMed.consultas.model;

import br.com.SmartMed.consultas.rest.dto.EspecialidadeDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "especialidade")
public class EspecialidadeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 64, nullable = false)
    @NotNull(message = "O campo nome não pode ser vazio")
    @NotBlank(message = "O campo nome não pode ser branco")
    private String nome;

    @Column(name = "descricao", length = 255, nullable = false)
    @NotBlank(message = "O campo  descrição não pode ser branco")
    @NotNull(message = "O campo descrição não pode ser vazio")
    private String descricao;

    public EspecialidadeDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, EspecialidadeDTO.class);
    }
}
