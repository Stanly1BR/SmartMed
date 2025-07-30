package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoAgendaOutputDTO {
    private String medico;
    private LocalDateTime data;
    private List<String> horariosOcupados;
    private List<String> horariosDisponiveis;
}
