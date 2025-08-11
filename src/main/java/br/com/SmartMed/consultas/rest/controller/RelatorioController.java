package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.rest.dto.RankMedicoAtendimentoDetalhesDTO;
import br.com.SmartMed.consultas.rest.dto.RankMedicoAtendimentoInputDTO;
import br.com.SmartMed.consultas.rest.dto.RankMedicoAtendimentoOutputDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioDTO;
import br.com.SmartMed.consultas.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private ConsultaRepository consultaRepository;

    // Link: http://localhost:8080/api/relatorio/faturamento?dataInicio=2025-05-01T00:00:00&dataFim=2025-07-31T23:59:59
    @GetMapping("/faturamento")
    public ResponseEntity<RelatorioDTO> obterFaturamento(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dataInicio,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dataFim){
        RelatorioDTO relatorio = relatorioService.gerarRelatorioDeFaturamento(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }

    // Link: http://localhost:8080/api/relatorio/medicos-mais-ativos
    @PostMapping("/medicos-mais-ativos")
    public ResponseEntity<RankMedicoAtendimentoOutputDTO<RankMedicoAtendimentoDetalhesDTO>> obterMedicosMaisAtivos(
            @RequestBody RankMedicoAtendimentoInputDTO input) {
        RankMedicoAtendimentoOutputDTO<RankMedicoAtendimentoDetalhesDTO> rank = relatorioService.gerarRankMedicoPorAtendimentos(input);
        return ResponseEntity.ok(rank);
    }
}
