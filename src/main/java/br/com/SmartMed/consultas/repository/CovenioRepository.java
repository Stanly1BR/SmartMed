package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.CovenioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.ConnectException;
import java.util.List;
import java.util.Optional;

public interface CovenioRepository extends JpaRepository<CovenioModel, Integer> {

    List<CovenioModel> findByNome(String nome);

    Optional<CovenioModel> findByCnpj(String cnpj);

    List<CovenioModel> findByTelefone(String telefone);

    List<CovenioModel> findByEmail(String email);

    List<ConnectException> findByAtivo(boolean Status);
}
