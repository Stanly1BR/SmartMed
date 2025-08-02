package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioDTO {
    private Double total;
    List<RelatorioCovenioDTO> relatorioCovenioDTOS;
    List<RelatorioFormaPagamentoDTO> relatorioFormaPagamentoDTOS;
}
