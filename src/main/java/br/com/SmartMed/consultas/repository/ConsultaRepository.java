package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.rest.dto.HistoricoConsultaOutputDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorPagamentoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaModel, Integer> {

    List<ConsultaModel> findByDataHoraConsulta(LocalDateTime dataConsulta);
    List<ConsultaModel> findByStatus(String statusConsulta);
    List<ConsultaModel> findByValor(float valorConsulta);
    List<ConsultaModel> findByObservacoes(String observacionConsulta);

    boolean existsByDataHoraConsulta(LocalDateTime pDataHoraConsulta);

    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorPagamentoDTO(fp.descricao, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN FormaPagamentoModel fp ON c.formaPagamentoID = fp.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY fp.descricao")
    List<RelatorioFaturamentoPorPagamentoDTO> findFaturamentoPorFormaPagamento(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorCovenioDTO(conv.nome, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN CovenioModel conv ON c.covenioID = conv.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY conv.nome")
    List<RelatorioFaturamentoPorCovenioDTO> findFaturamentoPorConvenio(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT SUM(c.valor) FROM ConsultaModel c " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim")
    Double findTotalFaturamento(@Param("dataInicio") LocalDateTime dataInicio,
                                @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId " +
            "AND c.dataHoraConsulta BETWEEN :inicioPeriodo AND :fimPeriodo " +
            "ORDER BY c.dataHoraConsulta ASC")
    List<ConsultaModel> findConsultasByMedicoAndPeriodo(
            @Param("medicoId") Integer medicoId,
            @Param("inicioPeriodo") LocalDateTime inicioPeriodo,
            @Param("fimPeriodo") LocalDateTime fimPeriodo);

    // --- Novo Método para Histórico de Consultas (Caso 03) ---
    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.HistoricoConsultaOutputDTO(" +
            "c.dataHoraConsulta, m.nome, e.nome, c.valor, c.status, c.observacoes) " +
            "FROM ConsultaModel c " +
            "LEFT JOIN MedicoModel m ON c.medicoID = m.id " +
            "LEFT JOIN EspecialidadeModel e ON m.especialidadeID = e.id " + // Join com especialidade do médico
            "WHERE c.pacienteID = :pacienteId " +
            "AND (:dataInicio IS NULL OR c.dataHoraConsulta >= :dataInicio) " +
            "AND (:dataFim IS NULL OR c.dataHoraConsulta <= :dataFim) " +
            "AND (:medicoId IS NULL OR c.medicoID = :medicoId) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:especialidadeId IS NULL OR m.especialidadeID = :especialidadeId) " + // Filtro por especialidade
            "ORDER BY c.dataHoraConsulta DESC") // Mais recente para mais antiga
    List<HistoricoConsultaOutputDTO> findHistoricoConsultasByPacienteAndFilters(
            @Param("pacienteId") Integer pacienteId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("medicoId") Integer medicoId,
            @Param("status") String status,
            @Param("especialidadeId") Integer especialidadeId);
}
