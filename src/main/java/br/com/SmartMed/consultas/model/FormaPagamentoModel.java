package br.com.SmartMed.consultas.model;

import br.com.SmartMed.consultas.rest.dto.FormaPagamentoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FormaPagamento")
public class FormaPagamentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descricao", length = 64)
    @NotBlank
    private String descricao;

    public FormaPagamentoDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, FormaPagamentoDTO.class);
    }
}
