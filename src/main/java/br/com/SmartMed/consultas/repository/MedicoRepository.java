package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.MedicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<MedicoModel, Integer> {

    //List<MedicoModel> findById(int pId);

    List<MedicoModel> findByNome(String nome);

    Optional<MedicoModel> findByCrm(String crm);
    Boolean existsByCrm(String crm);

    List<MedicoModel> findByEmail(String email);

    List<MedicoModel> findByTelefone(String telefone);

    List<MedicoModel> findByValorConsultaReferencia(float valorConsulta);

    List<MedicoModel> findByAtivo(boolean Status);

}
