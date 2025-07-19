package br.com.SmartMed.consultas.rest.controller.caso01;
/*
* Controller com endpoint:
* GET /api/relatorios/faturamento?dataInicio=YYYY-MM-
* DD&dataFim=YYYY-MM-DD
* */

import br.com.SmartMed.consultas.rest.dto.caso01.RelatorioDTO;
import br.com.SmartMed.consultas.service.caso01.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/faturamento")
    public ResponseEntity<RelatorioDTO> gerarRelatorioFaturamento(
            @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataInicio,
            @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataFim){
        RelatorioDTO relatorio = relatorioService.gerarRelatorio(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
