package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public RelatorioDTO gerarRelatorioDeFaturamento(LocalDateTime dataInicio, LocalDateTime dataFim){
        try{
            List<RelatorioFormaPagamentoDTO> FaturamentoFP =
            consultaRepository.BuscaFaturamentoPorFormaPagamento(dataInicio, dataFim);

            List<RelatorioCovenioDTO> FaturamentoC =
            consultaRepository.BuscaFaturamentoPorCovenio(dataInicio, dataFim);

            Double FaturamentoTotal =
            consultaRepository.BuscaFaturamentoTotal(dataInicio, dataFim);

            return new RelatorioDTO(
                    FaturamentoTotal,
                    FaturamentoC,
                    FaturamentoFP
            );

        }catch(Exception e){
            throw new RuntimeException("Erro ao gerar relatório de faturamento: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public RankMedicoAtendimentoOutputDTO<RankMedicoAtendimentoDetalhesDTO> gerarRankMedicoPorAtendimentos(RankMedicoAtendimentoInputDTO input){

        if (input.getPagina() < 0 || input.getTamanhoPagina() < 0) {
            throw new IllegalArgumentException("O número da página não pode ser negativo.");
        }

        YearMonth yearMonth = YearMonth.of(input.getAno(), input.getMes());

        LocalDateTime dataInicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        Pageable pageable = PageRequest.of(input.getPagina(), input.getTamanhoPagina());

        Page<RankMedicoAtendimentoDetalhesDTO> rank = consultaRepository.buscarRankMedicosPorAtendimentos(
                input.getMes(),
                input.getAno(),
                pageable);


        RankMedicoAtendimentoOutputDTO<RankMedicoAtendimentoDetalhesDTO> outputDTO = new RankMedicoAtendimentoOutputDTO<>();
        outputDTO.setConteudo(rank.getContent());
        outputDTO.setPaginaAtual(rank.getNumber() + 1);
        outputDTO.setTotalPaginas(rank.getTotalPages());


        return outputDTO;
    }
}
