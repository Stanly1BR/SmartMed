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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    /*
    * http://localhost:8080/api/relatorios/faturamento?dataInicio=2025-01-01&dataFim=2025-07-19
    * */
    @GetMapping("/faturamento")
    public ResponseEntity<RelatorioDTO> gerarRelatorioFaturamento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataFim){
        RelatorioDTO relatorio = relatorioService.gerarRelatorio(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
