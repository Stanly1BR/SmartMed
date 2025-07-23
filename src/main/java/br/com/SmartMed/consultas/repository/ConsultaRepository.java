package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorCovenioDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioFaturamentoPorPagamentoDTO;
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

    // --- Métodos Adicionais para Agendamento Inteligente (Caso 02) ---

    // Este método é mais genérico e verifica se já existe uma consulta começando em um dado horário
    boolean existsByDataHoraConsulta(LocalDateTime pDataHoraConsulta);

    // Busca todas as consultas de um médico de um período, considerando a duração para sobreposição
    // Verifique o log do Hibernate ao iniciar para ver se o H2 a traduz corretamente.
    // Se tiver problemas, pode ser necessário simplificar a query e fazer mais checagens em Java.
    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId " +
            "AND c.dataHoraConsulta BETWEEN :inicioPeriodo AND :fimPeriodo " +
            "ORDER BY c.dataHoraConsulta ASC")
    List<ConsultaModel> findConsultasByMedicoAndPeriodo(
            @Param("medicoId") Integer medicoId,
            @Param("inicioPeriodo") LocalDateTime inicioPeriodo,
            @Param("fimPeriodo") LocalDateTime fimPeriodo);

}
