package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.model.RecepcionistaModel;
import br.com.SmartMed.consultas.rest.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaModel, Integer> {

    List<ConsultaModel> findByDataHoraConsulta(LocalDateTime dataConsulta);
    List<ConsultaModel> findByStatus(String statusConsulta);
    List<ConsultaModel> findByValor(float valorConsulta);
    List<ConsultaModel> findByObservacoes(String observacionConsulta);

    //-- (Caso 01) --
    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RelatorioFormaPagamentoDTO(fp.descricao, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN FormaPagamentoModel fp ON c.formaPagamentoID = fp.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :pDataInicio AND :pDataFim " +
            "GROUP BY fp.descricao")
    List<RelatorioFormaPagamentoDTO> BuscaFaturamentoPorFormaPagamento(@Param(("pDataInicio")) LocalDateTime pDataInicio,
                                                                       @Param(("pDataFim")) LocalDateTime pDataFim);

    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RelatorioCovenioDTO(co.nome, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN CovenioModel co ON c.covenioID = co.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :pDataInicio AND :pDataFim " +
            "GROUP BY co.nome")
    List<RelatorioCovenioDTO> BuscaFaturamentoPorCovenio(@Param(("pDataInicio")) LocalDateTime pDataInicio,
                                                         @Param(("pDataFim")) LocalDateTime pDataFim);

    @Query("SELECT SUM(c.valor) FROM ConsultaModel c " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :pDataInicio AND :pDataFim ")
    Double BuscaFaturamentoTotal(@Param("dataInicio") LocalDateTime pDataInicio,
                                 @Param("dataFim") LocalDateTime pDataFim);

    //-- (Caso 02) --
    @Query("SELECT m FROM MedicoModel m WHERE m.ativo = TRUE")
    List<MedicoModel> BuscaMedicosAtivos();

    @Query("SELECT m FROM MedicoModel m JOIN EspecialidadeModel e ON m.especialidadeID = e.id " +
            "WHERE m.ativo = TRUE AND m.especialidadeID = :pIdEspecialidade ")
    List<MedicoModel> BuscaMedicosAtivosPorEspecialidade(@Param("pIdEspecialidade") int pIdEspecialidade);

    @Query("SELECT m FROM MedicoModel m WHERE m.id = :pMedicoId AND m.ativo = TRUE")
    Optional<MedicoModel> buscarMedico(@Param("pMedicoId") Integer pMedicoId);

    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :pMedicoId AND " +
            "c.dataHoraConsulta = :pData AND c.status != 'CANCELADA'")
    List<ConsultaModel> buscarConsultasPorMedicoEData(
            @Param("pMedicoId") Integer medicoId,
            @Param("pData") LocalDateTime pData);

    //-- (Caso 03) --
    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.HistoricoPacienteOutputDTO(" +
            "c.dataHoraConsulta, " +
            "m.nome, " +
            "e.nome, " +
            "c.valor, " +
            "c.status, " +
            "c.observacoes) " +
            "FROM ConsultaModel c " +
            "JOIN PacienteModel p ON p.id = c.pacienteID " +
            "JOIN MedicoModel m ON m.id = c.medicoID " +
            "JOIN EspecialidadeModel e ON e.id = m.especialidadeID " +
            "WHERE p.ativo = true " +
            "AND c.pacienteID = :pPacienteID " +
            "AND (:pDatInicio IS NULL OR c.dataHoraConsulta >= :pDatInicio) " +
            "AND (:pDatFim IS NULL OR c.dataHoraConsulta <= :pDatFim) " +
            "AND (:pMedicoID IS NULL OR c.medicoID = :pMedicoID) " +
            "AND (:pStatus IS NULL OR c.status = :pStatus) " +
            "ORDER BY c.dataHoraConsulta DESC")
    List<HistoricoPacienteOutputDTO> ConsultasPaciente(
            @Param("pPacienteID") Integer pPacienteID,
            @Param("pDatInicio") LocalDateTime pDatInicio,
            @Param("pDatFim") LocalDateTime pDatFim,
            @Param("pMedicoID") Integer pMedicoID,
            @Param("pStatus") String pStatus);

    //-- (Caso 04) --

    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :pMedicoId AND " +
            "CAST(c.dataHoraConsulta AS DATE) = CAST(:pData AS DATE) AND c.status != 'CANCELADA'")
    List<ConsultaModel> buscarConsultasPorMedicoEData02(
            @Param("pMedicoId") Integer pMedicoId,
            @Param("pData") LocalDateTime pData);

    // -- (Caso 06) --

    @Query("SELECT c FROM ConsultaModel c WHERE c.id = :pId AND c.status = 'AGENDADA'")
    Optional<ConsultaModel> buscarConsultasAgendadas(@Param("pId") Integer pId);

    // -- (caso 08) --
    @Query("SELECT c FROM ConsultaModel  c WHERE c.id = :pId AND c.status = 'AGENDADA'")
    Optional<ConsultaModel> buscarConsultaAgendada(@Param("pId") Integer pId);

    // -- (Caso 09) --
    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RankMedicoAtendimentoOutputDTO(m.nome, COUNT(c.id)) " +
            "FROM ConsultaModel c JOIN MedicoModel m ON c.medicoID = m.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :pDataInicio AND :pDataFim " +
            "GROUP BY m.nome " +
            "ORDER BY COUNT(c.id) DESC")
    Page<RankMedicoAtendimentoOutputDTO> buscarRankMedicosPorAtendimentosComDatas(
            @Param("pDataInicio") LocalDateTime pDataInicio,
            @Param("pDataFim") LocalDateTime pDataFim,
            Pageable pageable);

    // -- (Caso 10) --
    @Query("SELECT r FROM RecepcionistaModel r WHERE r.ativo = TRUE AND r.id = :pId")
    Optional<RecepcionistaModel> buscarRecepcionista(@Param("pId") Integer pId);

    /*teste @Query(value = "SELECT NEW br.com.smartmed.consultas.rest.dto.RankingMedicosAtendimentosResponseDTO(m.nome, COUNT(c.id)) " +
            "FROM ConsultaModel c " +
            "JOIN MedicoModel m ON c.medicoID = m.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY m.nome " +
            "ORDER BY COUNT(c.id) DESC",
            countQuery = "SELECT COUNT(DISTINCT m.nome) FROM ConsultaModel c JOIN MedicoModel m ON c.medicoID = m.id WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim")
    Page<RankingMedicosAtendimentosResponseDTO> RankMedicos(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable);*/

}