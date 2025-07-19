package br.com.SmartMed.consultas.rest.dto.caso01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioDTO {
    private double totalGeral;
    private List<RelatorioFaturamentoPorPagamentoDTO> porFormaPagamento;
    private List<RelatorioFaturamentoPorCovenioDTO> porCovenio;
}
