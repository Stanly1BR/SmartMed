package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.EspecialidadeModel;
import br.com.SmartMed.consultas.repository.EspecialidadeRepository;
import br.com.SmartMed.consultas.rest.dto.EspecialidadeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Transactional(readOnly = true)
    public EspecialidadeDTO buscarPorID(int id){
        EspecialidadeModel especialidade = especialidadeRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Especialidade com o "+id+" não encontrado"));
        return especialidade.toDTO();
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeDTO> buscarTodos(){
        List<EspecialidadeModel> especialidade = especialidadeRepository.findAll();
        return especialidade.stream().map(especialidadeModel -> especialidadeModel.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public EspecialidadeDTO alterar(EspecialidadeModel especialidade){
        try {

            if (!especialidadeRepository.existsById(especialidade.getId())) {
                throw new ObjectNotFoundException("especialidade Não encontrado");
            }
            return especialidadeRepository.save(especialidade).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível alterar a especialidade " + especialidade.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao alterar a especialidade " + especialidade.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao alterar a especialidade " + especialidade.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao alterar a especialidade " + especialidade.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao alterar a especialidade " + especialidade.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public EspecialidadeDTO salvar(EspecialidadeModel especialidade){
        try {

            if (especialidadeRepository.existsByNome(especialidade.getNome())) {
                throw new ObjectNotFoundException("especialidade já cadastrada");
            }
            return especialidadeRepository.save(especialidade).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a especialidade " + especialidade.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar a especialidade " + especialidade.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao salvar a especialidade " + especialidade.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar a especialidade " + especialidade.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao salvar a especialidade " + especialidade.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public void deletar(EspecialidadeModel especialidade){
        try {

            if (!especialidadeRepository.existsById(especialidade.getId())) {
                throw new ObjectNotFoundException("especialidade não encontrada");
            }
            especialidadeRepository.delete(especialidade);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível delete a especialidade " + especialidade.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao delete a especialidade " + especialidade.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao delete a especialidade " + especialidade.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao delete a especialidade " + especialidade.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao delete a especialidade " + especialidade.getId() + "Não encontrado no bando de dados");
        }
    }
}
