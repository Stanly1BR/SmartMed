package br.com.SmartMed.consultas.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoricoConsultaInputDTO {
    @NotNull(message = "O ID do paciente não pode ser nulo.")
    private Integer pacienteID; // Usar Integer para permitir null se for opcional, mas aqui é obrigatório

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer medicoID;
    private String status; // Ex: "REALIZADA", "AGENDADA", "CANCELADA"
    private Integer especialidadeID; // Adicionado filtro por especialidade
}
