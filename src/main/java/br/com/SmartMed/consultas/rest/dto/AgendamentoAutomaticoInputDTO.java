package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoAutomaticoInputDTO {
    private Integer pacienteID;
    private Integer especialidadeID;
    private LocalDateTime dataHoraInicial;
    private Integer covenioID;
    private Integer formaPagamentoID;
}
