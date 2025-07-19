package br.com.SmartMed.consultas.service.caso01;
/*
* Service com regra de negócio:
* Considerar apenas consultas com status = 'REALIZADA'.
* Consultas canceladas ou marcadas como ausente devem ser ignoradas.
* O sistema deve permitir informar uma data inicial e uma data final.
* */

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.repository.CovenioRepository;
import br.com.SmartMed.consultas.repository.FormaPagamentoRepository;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CovenioRepository covenioRepository;

    @Transactional(readOnly = true)
    public RelatorioDTO gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        List<ConsultaModel> consultas = consultaRepository.buscarConsultasPorPeriodo(inicio, fim);

        double totalGeral = consultas.stream()
                .mapToDouble(ConsultaModel::getValor)
                .sum();

        Map<Integer, Double> totalPorFormaPagamento = consultas.stream()
                .collect(Collectors.groupingBy(
                        ConsultaModel::getFormaPagamentoID,
                        Collectors.summingDouble(ConsultaModel::getValor)
                ));

        Map<Integer, Double> totalPorConvenio = consultas.stream()
                .collect(Collectors.groupingBy(
                        ConsultaModel::getCovenioID,
                        Collectors.summingDouble(ConsultaModel::getValor)
                ));

        List<RelatorioFaturamentoPorPagamentoDTO> porFormaPagamento = totalPorFormaPagamento.entrySet().stream()
                .map(entry -> {
                    String descricao = formaPagamentoRepository.findById(entry.getKey())
                            .map(fp -> fp.getDescricao())
                            .orElse("Não encontrado");
                    return new RelatorioFaturamentoPorPagamentoDTO(descricao, entry.getValue());
                })
                .collect(Collectors.toList());

        List<RelatorioFaturamentoPorCovenioDTO> porConvenio = totalPorConvenio.entrySet().stream()
                .map(entry -> {
                    String nome = covenioRepository.findById(entry.getKey())
                            .map(c -> c.getNome())
                            .orElse("Não encontrado");
                    return new RelatorioFaturamentoPorCovenioDTO(nome, entry.getValue());
                })
                .collect(Collectors.toList());

        // Criando um objeto com os parametros
        return new RelatorioDTO(totalGeral, porFormaPagamento, porConvenio);


    }
}
