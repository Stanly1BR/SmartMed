package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.FormaPagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamentoModel, Integer> {
}
