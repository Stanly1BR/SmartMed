package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoAutomaticoOutputDTO {
    private Integer id;
    private LocalDateTime dataHoraInicial;
    private String medico;
    private String paciente;
    private double valor;

}
