package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.EspecialidadeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadeRepository extends JpaRepository<EspecialidadeModel, Integer> {

    List<EspecialidadeModel> findByNome(String nome);
    Boolean existsByNome(String nome);
}
