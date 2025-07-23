package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.rest.dto.caso01.RelatorioFaturamentoPorCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.caso01.RelatorioFaturamentoPorPagamentoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaModel, Integer> {

    List<ConsultaModel> findByDataHoraConsulta(LocalDateTime dataConsulta);
    List<ConsultaModel> findByStatus(String statusConsulta);
    List<ConsultaModel> findByValor(float valorConsulta);
    List<ConsultaModel> findByObservacoes(String observacionConsulta);

    // Método para verificar existência por data/hora - Útil para agendamento
    boolean existsByDataHoraConsulta(LocalDateTime pDataHoraConsulta); // Nova query method do seu amigo


    // @Query para Faturamento por Forma de Pagamento (JPQL com NEW e seu DTO específico)
    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.caso01.RelatorioFaturamentoPorPagamentoDTO(fp.descricao, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN FormaPagamentoModel fp ON c.formaPagamentoID = fp.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY fp.descricao")
    List<RelatorioFaturamentoPorPagamentoDTO> findFaturamentoPorFormaPagamento(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    // @Query para Faturamento por Convênio (JPQL com NEW e seu DTO específico)
    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.caso01.RelatorioFaturamentoPorCovenioDTO(conv.nome, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN CovenioModel conv ON c.covenioID = conv.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY conv.nome")
    List<RelatorioFaturamentoPorCovenioDTO> findFaturamentoPorConvenio(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    // @Query para Faturamento Total (JPQL)
    @Query("SELECT SUM(c.valor) FROM ConsultaModel c " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim")
    Double findTotalFaturamento(@Param("dataInicio") LocalDateTime dataInicio,
                                @Param("dataFim") LocalDateTime dataFim);

    // @Query para buscar consultas por médico e período (para agendamento inteligente)
    // Manter a versão mais robusta se você tem o campo duracaoMinutos em ConsultaModel.
    // Lembre-se de verificar a compatibilidade de FUNCTION('TIMESTAMPADD', ...) com seu dialeto.
    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId " +
            "AND c.dataHoraConsulta >= :inicioPeriodo AND c.dataHoraConsulta <= :fimPeriodo " +
            "OR (FUNCTION('TIMESTAMPADD', MINUTE, c.duracaoMinutos, c.dataHoraConsulta) BETWEEN :inicioPeriodo AND :fimPeriodo) " +
            "ORDER BY c.dataHoraConsulta ASC")
    List<ConsultaModel> findConsultasByMedicoAndPeriodo(
            @Param("medicoId") Integer medicoId,
            @Param("inicioPeriodo") LocalDateTime inicioPeriodo,
            @Param("fimPeriodo") LocalDateTime fimPeriodo);
}
