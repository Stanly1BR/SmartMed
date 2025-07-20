package br.com.SmartMed.consultas.rest.dto.caso02;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoAutomaticoInputDTO {
    private int PacienteID;
    private int EspecialidadeID;
    private LocalDateTime dataHoraInicial;
    private int duracaoConsultaMinutos;
    private int covenioID;
    private int formaPagamentoID;
}
