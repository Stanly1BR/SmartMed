package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.rest.dto.RelatorioDTO;
import br.com.SmartMed.consultas.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    // Link: http://localhost:8080/api/relatorio/faturamento?dataInicio=2025-05-01T00:00:00&dataFim=2025-07-31T23:59:59
    @GetMapping("/faturamento")
    public ResponseEntity<RelatorioDTO> obterFaturamento(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dataInicio,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dataFim){
        RelatorioDTO relatorio = relatorioService.gerarRelatorioDeFaturamento(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
