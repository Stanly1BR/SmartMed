package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPacienteInputDTO {
    private Integer pacienteID;
    private LocalDateTime datInicio;
    private LocalDateTime datFim;
    private Integer medicoID;
    private String status;
}
