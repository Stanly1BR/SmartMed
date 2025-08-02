package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
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
    List<RelatorioFormaPagamentoDTO> BuscaFaturamentoPorFormaPagamento(@Param(("dataInicio")) LocalDate dataInicio,
                                                                       @Param(("dataFim")) LocalDate dataFim);

    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RelatorioCovenioDTO(co.nome, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN CovenioModel co ON c.covenioID = co.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY co.nome")
    List<RelatorioCovenioDTO> BuscaFaturamentoPorCovenio(@Param(("dataInicio")) LocalDate dataInicio,
                                                         @Param(("dataFim")) LocalDate dataFim);

    @Query("SELECT SUM(c.valor) FROM ConsultaModel c " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim ")
    Double BuscaFaturamentoTotal(@Param("dataInicio") LocalDate dataInicio,
                                 @Param("dataFim") LocalDate dataFim);

    //-- (Caso 02) --




}
