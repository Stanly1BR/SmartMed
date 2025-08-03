package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.rest.dto.HistoricoPacienteOutputDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFormaPagamentoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY fp.descricao")
    List<RelatorioFormaPagamentoDTO> BuscaFaturamentoPorFormaPagamento(@Param(("dataInicio")) LocalDateTime dataInicio,
                                                                       @Param(("dataFim")) LocalDateTime dataFim);

    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RelatorioCovenioDTO(co.nome, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN CovenioModel co ON c.covenioID = co.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY co.nome")
    List<RelatorioCovenioDTO> BuscaFaturamentoPorCovenio(@Param(("dataInicio")) LocalDateTime dataInicio,
                                                         @Param(("dataFim")) LocalDateTime dataFim);

    @Query("SELECT SUM(c.valor) FROM ConsultaModel c " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim ")
    Double BuscaFaturamentoTotal(@Param("dataInicio") LocalDateTime dataInicio,
                                 @Param("dataFim") LocalDateTime dataFim);

    //-- (Caso 02) --
    @Query("SELECT m FROM MedicoModel m WHERE m.ativo = TRUE")
    List<MedicoModel> BuscaMedicosAtivos();

    @Query("SELECT m FROM MedicoModel m JOIN EspecialidadeModel e ON m.especialidadeID = e.id WHERE m.ativo = TRUE ")
    List<MedicoModel> BuscaMedicosAtivosPorEspecialidade(@Param("idEspecialidade") int idEspecialidade);

    @Query("SELECT m FROM MedicoModel m WHERE m.id = :medicoId AND m.ativo = TRUE")
    Optional<MedicoModel> buscarMedico(@Param("medicoId") Integer medicoId);

    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId AND " +
            "c.dataHoraConsulta = :data AND c.status != 'CANCELADA'")
    List<ConsultaModel> buscarConsultasPorMedicoEData(
            @Param("medicoId") Integer medicoId,
            @Param("data") LocalDateTime data);

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
            "AND c.pacienteID = :pacienteID " +
            "AND (:datInicio IS NULL OR c.dataHoraConsulta >= :datInicio) " +
            "AND (:datFim IS NULL OR c.dataHoraConsulta <= :datFim) " +
            "AND (:medicoID IS NULL OR c.medicoID = :medicoID) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "ORDER BY c.dataHoraConsulta DESC")
    List<HistoricoPacienteOutputDTO> ConsultasPaciente(
            @Param("pacienteID") Integer pacienteID,
            @Param("datInicio") LocalDateTime datInicio,
            @Param("datFim") LocalDateTime datFim,
            @Param("medicoID") Integer medicoID,
            @Param("status") String status);

    //-- (Caso 04) --

    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId AND " +
            "CAST(c.dataHoraConsulta AS DATE) = CAST(:data AS DATE) AND c.status != 'CANCELADA'")
    List<ConsultaModel> buscarConsultasPorMedicoEData02(
            @Param("medicoId") Integer medicoId,
            @Param("data") LocalDateTime data);



}
