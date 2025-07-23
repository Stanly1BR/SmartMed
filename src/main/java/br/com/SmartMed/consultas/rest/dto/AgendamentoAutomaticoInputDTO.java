package br.com.SmartMed.consultas.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoAutomaticoInputDTO {
    private int pacienteID;
    private int especialidadeID;
    private LocalDateTime dataHoraInicial;
    private int duracaoConsultaMinutos;
    private int covenioID;
    private int formaPagamentoID;
}
