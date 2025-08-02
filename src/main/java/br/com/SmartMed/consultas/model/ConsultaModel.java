package br.com.SmartMed.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consulta")
public class ConsultaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dataHoraConsulta", nullable = false)
    @NotNull(message = "O campo dataHoraConsulta não pode ser vazio")
    private LocalDate dataHoraConsulta;

    @Column(name = "status", length = 16)
    @NotBlank(message = "O campo status não pode ser branco")
    private String status;

    @Column(name = "valor")
    private float valor;

    @Column(name = "observacoes", length = 1024)
    @NotBlank(message = "O campo observações não pode ser branco")
    private String observacoes;

    @Column(name = "pacienteID")
    private int pacienteID;

    @Column(name = "medicoID")
    private Integer medicoID;

    @Column(name = "formaPagamentoID")
    private int formaPagamentoID;

    @Column(name = "covenioID")
    private int covenioID;

    @Column(name = "recepcionistaID")
    private int recepcionistaID;

    @Column(name = "duracaoMinutos", nullable = false)
    private int duracaoMinutos;

    /*public ConsultaDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, ConsultaDTO.class);
    }*/
}
