package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.EspecialidadeModel;
import br.com.SmartMed.consultas.model.FormaPagamentoModel;
import br.com.SmartMed.consultas.repository.FormaPagamentoRepository;
import br.com.SmartMed.consultas.rest.dto.EspecialidadeDTO;
import br.com.SmartMed.consultas.rest.dto.FormaPagamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Transactional(readOnly = true)
    public FormaPagamentoDTO buscarPorID(int id){
        FormaPagamentoModel forma = formaPagamentoRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Forma com o "+id+" não encontrado"));
        return forma.toDTO();
    }

    @Transactional(readOnly = true)
    public List<FormaPagamentoDTO> buscarTodos(){
        List<FormaPagamentoModel> forma = formaPagamentoRepository.findAll();
        return forma.stream().map(formaPagamentoModel -> formaPagamentoModel.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public FormaPagamentoDTO alterar(FormaPagamentoModel forma){
        try {

            if (!formaPagamentoRepository.existsById(forma.getId())) {
                throw new ObjectNotFoundException("Forma de pagamento Não encpntrado");
            }
            return formaPagamentoRepository.save(forma).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível alterar a Forma de pagamento " + forma.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao alterar a Forma de pagamento " + forma.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao alterar a Forma de pagamento " + forma.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao alterar a Forma de pagamento " + forma.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao alterar a Forma de pagamento " + forma.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public FormaPagamentoDTO salvar(FormaPagamentoModel forma){
        try {

            if (formaPagamentoRepository.existsById(forma.getId())) {
                throw new ObjectNotFoundException("Forma de pagamento já salva");
            }
            return formaPagamentoRepository.save(forma).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a Forma de pagamento " + forma.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar a Forma de pagamento " + forma.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao salvar a Forma de pagamento " + forma.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar a Forma de pagamento " + forma.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao salvar a Forma de pagamento " + forma.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public void deletar(FormaPagamentoModel forma){
        try {

            if (!formaPagamentoRepository.existsById(forma.getId())) {
                throw new ObjectNotFoundException("Forma de pagamento Não encpntrado");
            }
            formaPagamentoRepository.delete(forma);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a Forma de pagamento " + forma.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a Forma de pagamento " + forma.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao deletar a Forma de pagamento " + forma.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar a Forma de pagamento " + forma.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao deletar a Forma de pagamento " + forma.getId() + "Não encontrado no bando de dados");
        }
    }
}
