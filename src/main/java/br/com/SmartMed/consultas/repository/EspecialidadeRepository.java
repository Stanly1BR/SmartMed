package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.EspecialidadeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspecialidadeRepository extends JpaRepository<EspecialidadeModel, Integer> {

    List<EspecialidadeModel> findByNome(String nome);
}
