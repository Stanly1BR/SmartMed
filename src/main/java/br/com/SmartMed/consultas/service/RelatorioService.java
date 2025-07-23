package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.rest.dto.RelatorioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorPagamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class RelatorioService {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public RelatorioDTO gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        Double totalGeral = consultaRepository.findTotalFaturamento(inicio, fim);
        List<RelatorioFaturamentoPorPagamentoDTO> porFormaPagamento = consultaRepository.findFaturamentoPorFormaPagamento(inicio, fim);
        List<RelatorioFaturamentoPorCovenioDTO> porConvenio = consultaRepository.findFaturamentoPorConvenio(inicio, fim);

        if (totalGeral == null) {
            totalGeral = 0.0;
        }

        return new RelatorioDTO(totalGeral, porFormaPagamento, porConvenio);
    }
}
