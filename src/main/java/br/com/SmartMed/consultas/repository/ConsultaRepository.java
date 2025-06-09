package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.ConsultaModel;
import org.hibernate.dialect.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaModel, Integer> {

    List<ConsultaModel> findByDataHoraConsulta(Database dataConsulta);

    List<ConsultaModel> findByStatus(String statusConsulta);

    List<ConsultaModel> findByValor(float valorConsulta);

    List<ConsultaModel> findByObservacoes(String observacionConsulta);

}
