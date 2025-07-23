package br.com.SmartMed.consultas.service.caso01;
/*
* Service com regra de negócio:
* Considerar apenas consultas com status = 'REALIZADA'.
* Consultas canceladas ou marcadas como ausente devem ser ignoradas.
* O sistema deve permitir informar uma data inicial e uma data final.
* */

import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.rest.dto.caso01.RelatorioDTO;
import br.com.SmartMed.consultas.rest.dto.caso01.RelatorioFaturamentoPorCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.caso01.RelatorioFaturamentoPorPagamentoDTO;
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
        /*
        * - Converte as datas para incluir o horário
        * - `atStartOfDay()`: Define como 00:00:00
        * - `atTime(LocalTime.MAX)`: Define como 23:59:59.999999999
        * */
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        // Usa as novas queries do repositório, que já retornam os DTOs específicos
        Double totalGeral = consultaRepository.findTotalFaturamento(inicio, fim);
        List<RelatorioFaturamentoPorPagamentoDTO> porFormaPagamento = consultaRepository.findFaturamentoPorFormaPagamento(inicio, fim);
        List<RelatorioFaturamentoPorCovenioDTO> porConvenio = consultaRepository.findFaturamentoPorConvenio(inicio, fim);

        // O totalGeral pode vir nulo se não houver consultas, tratar para 0.0
        if (totalGeral == null) {
            totalGeral = 0.0;
        }

        // Cria o DTO de saída diretamente com os DTOs retornados do repositório
        return new RelatorioDTO(totalGeral, porFormaPagamento, porConvenio);
    }
}
