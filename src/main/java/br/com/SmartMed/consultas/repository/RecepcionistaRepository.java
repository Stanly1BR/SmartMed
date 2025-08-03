package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.RecepcionistaModel;
import br.com.SmartMed.consultas.rest.dto.HistoricoPacienteOutputDTO;
import br.com.SmartMed.consultas.rest.dto.ListagemRecepcionistasOutputDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecepcionistaRepository extends JpaRepository<RecepcionistaModel, Integer> {
    List<RecepcionistaModel> findByNome(String pNome);
    List<RecepcionistaModel> findByTelefone(String pTelefone);
    List<RecepcionistaModel> findByEmail(String pEmail);
    Optional<RecepcionistaModel> findByAtivo(boolean pStatus);
    Optional<RecepcionistaModel> findByCpf(String pCpf);
    Boolean existsByCpf(String pCpf);

    @Query("SELECT NEW br.com.SmartMed.consultas.rest.dto.ListagemRecepcionistasOutputDTO " +
            "(r.nome, r.cpf, r.email, r.dataAdmissao, r.ativo) " +
            " FROM RecepcionistaModel r" +
            " WHERE r.ativo = :pAtivo " +
            " AND (:pDataInicio IS NULL OR r.dataAdmissao >= :pDataInicio) " +
            " AND (:pDataFim IS NULL OR r.dataAdmissao <= :pDataFim)" +
            " ORDER BY r.dataAdmissao")
    List<ListagemRecepcionistasOutputDTO> ListagemRecepcionistas(@Param("pDataInicio")LocalDate pDataInicio,
                                                                 @Param("pDataFim") LocalDate pDataFim,
                                                                 @Param("pStatus") boolean pStatus,
                                                                 Pageable pageable);

}
