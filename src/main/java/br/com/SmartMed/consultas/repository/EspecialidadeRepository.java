package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.EspecialidadeModel;
import br.com.SmartMed.consultas.rest.dto.RelatorioDeEspecialidadesOutputDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EspecialidadeRepository extends JpaRepository<EspecialidadeModel, Integer> {

    List<EspecialidadeModel> findByNome(String nome);
    Boolean existsByNome(String nome);

    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.RelatorioDeEspecialidadesOutputDTO(e.nome, COUNT(c.id)) " +
            "FROM ConsultaModel c " +
            "JOIN MedicoModel m ON c.medicoID = m.id " +
            "JOIN EspecialidadeModel e ON e.id = m.especialidadeID " +
            "WHERE c.status = 'REALIZADA' AND CAST(c.dataHoraConsulta AS DATE) BETWEEN :pDataInicio AND :pDataFim " +
            "GROUP BY e.nome " +
            "ORDER BY COUNT(c.id) DESC")
    List<RelatorioDeEspecialidadesOutputDTO> buscarRelatorioEspecialidadesMaisAtendidas(
            @Param("pDataInicio") LocalDate pDataInicio,
            @Param("pDataFim") LocalDate pDataFim);
}
