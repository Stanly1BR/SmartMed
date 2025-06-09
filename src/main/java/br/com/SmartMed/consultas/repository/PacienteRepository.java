package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.PacienteModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteModel, Integer> {

    List<PacienteModel> findByNome(String Nome);

    Optional<PacienteModel> findByCpf(String cpf);

    Boolean existsByCpf(String cpf);

    List<PacienteModel> findByTelefone(String telefone);

    List<PacienteModel> findByEmail(String email);


}
