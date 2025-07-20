package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
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

    //caso01
    @Query("SELECT c FROM ConsultaModel c WHERE c.status = 'REALIZADO'" +
            "AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim")
    List<ConsultaModel> buscarConsultasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio,
                                                  @Param("dataFim") LocalDateTime dataFim);

    //caso02
    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId " +
            "AND c.dataHoraConsulta BETWEEN :inicio AND :fim")
    List<ConsultaModel> findConsultasByMedicoIdAndPeriodo(@Param("medicoId") int medicoId,
                                                         @Param("inicio") LocalDateTime inicio,
                                                         @Param("fim") LocalDateTime fim);

    @Query("SELECT m FROM MedicoModel m WHERE m.especialidadeID = :especialidadeId " +
            "AND m.ativo = true")
    List<MedicoModel> findMedicosByEspecialidadeId(@Param("especialidadeId") int especialidadeId);
}
