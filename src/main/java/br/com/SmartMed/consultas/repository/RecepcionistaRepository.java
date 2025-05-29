package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.RecepcionistaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecepcionistaRepository extends JpaRepository<RecepcionistaModel, Integer> {
    List<RecepcionistaModel> findByNome(String nome);

    Optional<RecepcionistaModel> findByCpf(String cpf);

    List<RecepcionistaModel> findByTelefone(String telefone);

    List<RecepcionistaModel> findByEmail(String email);

    Optional<RecepcionistaModel> findByAtivo(boolean Status);
}
