package br.com.SmartMed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "formaPagamento")
public class FormaPagamentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descricao", length = 64)
    @NotBlank(message = "O campo descrição não pode ser branco")
    private String descricao;

    /*public FormaPagamentoDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, FormaPagamentoDTO.class);
    }*/
}
