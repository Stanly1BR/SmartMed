package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.RecepcionistaModel;
import br.com.SmartMed.consultas.repository.RecepcionistaRepository;
import br.com.SmartMed.consultas.rest.dto.RecepcionistaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecepcionistaService {

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Transactional(readOnly = true)
    public RecepcionistaDTO obterPorCpf(String cpf){
        RecepcionistaModel recepcionista = recepcionistaRepository.findByCpf(cpf).orElseThrow(()-> new ObjectNotFoundException("Recepcionista com o "+cpf+" não encontrado"));
        return recepcionista.toDTO();
    }

    @Transactional(readOnly = true)
    public List<RecepcionistaDTO> obterTodos(){
        List<RecepcionistaModel> recepcionistas = recepcionistaRepository.findAll();
        return recepcionistas.stream().map(recepcionista -> recepcionista.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public RecepcionistaDTO salvar(RecepcionistaModel recepcionista){
        try {

            if (recepcionistaRepository.existsByCpf(recepcionista.getCpf())) {
                throw new ConstraintException("O recepcionista com esse CPF " + recepcionista.getCpf() + " não existe na base de dados!");
            }

            return recepcionistaRepository.save(recepcionista).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar o recepcionista " + recepcionista.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar o recepcionista " + recepcionista.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar o recepcionista " + recepcionista.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar o recepcionista " + recepcionista.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível salvar o recepcionista" + recepcionista.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public RecepcionistaDTO atualizar(RecepcionistaModel recepcionista){
        try {

            if (!recepcionistaRepository.existsByCpf(recepcionista.getCpf())) {
                throw new ConstraintException("O recepcionista com esse CPF " + recepcionista.getCpf() + " não existe na base de dados!");
            }

            return recepcionistaRepository.save(recepcionista).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar o recepcionista " + recepcionista.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o recepcionista " + recepcionista.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar o recepcionista " + recepcionista.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o recepcionista " + recepcionista.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar o recepcionista" + recepcionista.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public void deletar(RecepcionistaModel recepcionista){
        try {

            if (!recepcionistaRepository.existsByCpf(recepcionista.getCpf())) {
                throw new ConstraintException("O recepcionista com esse CPF " + recepcionista.getCpf() + " não existe na base de dados!");
            }

            recepcionistaRepository.delete(recepcionista);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar o recepcionista " + recepcionista.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar o recepcionista " + recepcionista.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar o recepcionista " + recepcionista.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível deletar o recepcionista " + recepcionista.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o recepcionista" + recepcionista.getNome() + ". Não encontrado no banco de dados!");
        }
    }
}
