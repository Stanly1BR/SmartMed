package br.com.SmartMed.consultas.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoAutomaticoOutputDTO {
    private int id;
    private LocalDateTime dataHoraConsulta;
    private String nomeMedico;
    private String nomePaciente;
    private double valor;
}
