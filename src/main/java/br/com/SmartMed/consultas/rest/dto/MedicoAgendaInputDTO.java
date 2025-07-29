package br.com.SmartMed.consultas.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicoAgendaInputDTO {
    @NotNull(message = "O ID do médico não pode ser nulo.")
    private Integer medicoID;
    @NotNull(message = "A data não pode ser nula.")
    private LocalDate data;
}
