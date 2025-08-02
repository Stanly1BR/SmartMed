package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPacienteOutputDTO {
    private LocalDateTime dataHoraConsulta;
    private String nomeMedico;
    private String nomeEspecialidade;
    private float valor;
    private String status;
    private String observacoes;
}
