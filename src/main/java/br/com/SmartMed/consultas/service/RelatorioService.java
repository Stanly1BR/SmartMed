package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.rest.dto.RelatorioCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFormaPagamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public RelatorioDTO gerarRelatorioDeFaturamento(LocalDate dataInicio, LocalDate dataFim){
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
}
