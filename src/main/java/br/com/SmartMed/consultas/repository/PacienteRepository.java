package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.PacienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<PacienteModel, Integer> {

    List<PacienteModel> findByNome(String Nome);

    Optional<PacienteModel> findByCpf(String cpf);

    List<PacienteModel> findByTelefone(String telefone);

    List<PacienteModel> findByEmail(String email);

}
