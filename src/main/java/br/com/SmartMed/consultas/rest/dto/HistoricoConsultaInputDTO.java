package br.com.SmartMed.consultas.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoricoConsultaInputDTO {
    @NotNull(message = "O ID do paciente não pode ser nulo.")
    private Integer pacienteID;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer medicoID;
    private String status;
    private Integer especialidadeID;
}
