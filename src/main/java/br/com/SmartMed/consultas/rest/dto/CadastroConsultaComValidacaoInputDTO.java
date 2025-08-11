package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroConsultaComValidacaoInputDTO {
    private LocalDateTime dataHora;
    private int duracaoMinutos;
    private int medicoID;
    private int pacienteID;
    private int recepcionistaID;
    private int covenioID;
    private int formaPagamentoID;
}
