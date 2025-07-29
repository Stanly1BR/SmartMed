package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoConsultaOutputDTO {
    private LocalDateTime dataHora;
    private String medico;
    private String especialidade;
    private double valor;
    private String status;
    private String observacoes;
}
