package br.com.SmartMed.consultas.rest.dto.caso01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFaturamentoPorCovenioDTO {
    private String convenio;
    private double valor;
}
