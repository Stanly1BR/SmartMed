package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.rest.dto.RelatorioCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFormaPagamentoDTO;
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
            "WHERE c.status = 'REALIZADA' AND DATE(c.dataHoraConsulta) BETWEEN :dataInicio AND :dataFim ")
    Double BuscaFaturamentoTotal(@Param("dataInicio") LocalDateTime dataInicio,
                                 @Param("dataFim") LocalDateTime dataFim);

    //-- (Caso 02) --
    @Query("SELECT m FROM MedicoModel m WHERE m.ativo = TRUE")
    List<MedicoModel> BuscaMedicosAtivos();

    @Query("SELECT m FROM MedicoModel m JOIN EspecialidadeModel e ON m.especialidadeID = e.id WHERE m.ativo = TRUE ")
    List<MedicoModel> BuscaMedicosAtivosPorEspecialidade(@Param("idEspecialidade") int idEspecialidade);


    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId AND " +
            "CAST(c.dataHoraConsulta AS LocalDate) = :data AND c.status != 'CANCELADA'")
    List<ConsultaModel> buscarConsultasPorMedicoEData(
            @Param("medicoId") Integer medicoId,
            @Param("data") LocalDate data);

    //-- (Caso 03) --

}
