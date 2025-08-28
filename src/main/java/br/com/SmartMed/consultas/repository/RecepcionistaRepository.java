package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.RecepcionistaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Query("SELECT r FROM RecepcionistaModel r " +
            "WHERE r.ativo = :pStatus " +
            "AND (:pDataInicio IS NULL OR r.dataAdmissao >= :pDataInicio) " +
            "AND (:pDataFim IS NULL OR r.dataAdmissao <= :pDataFim)")
    Page<RecepcionistaModel> listagemRecepcionistas(@Param("pDataInicio") LocalDate pDataInicio,
                                                    @Param("pDataFim") LocalDate pDataFim,
                                                    @Param("pStatus") boolean pStatus,
                                                    Pageable pageable);


}
