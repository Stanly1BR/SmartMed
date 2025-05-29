package br.com.SmartMed.consultas.model;

import br.com.SmartMed.consultas.rest.dto.ConsultaDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.Database;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

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
    private Database dataHoraConsulta;

    @Column(name = "status", length = 16)
    private String status;

    @Column(name = "valor")
    private float valor;

    @Column(name = "observacoes", length = 1024)
    private String observacoes;

    @Column(name = "pacienteID")
    private int pacienteID;

    @Column(name = "medicoID")
    private int medicoID;

    @Column(name = "formaPagamentoID")
    private int formaPagamentoID;

    @Column(name = "covenioID")
    private int covenioID;

    @Column(name = "recepcionistaID")
    private int recepcionistaID;

    public ConsultaDTO toDTO(){
        ModelMapper m = new ModelMapper();
        return m.map(this, ConsultaDTO.class);
    }
}
