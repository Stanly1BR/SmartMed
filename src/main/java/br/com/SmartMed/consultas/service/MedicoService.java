package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.repository.MedicoRepository;
import br.com.SmartMed.consultas.rest.dto.MedicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService  {

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public MedicoDTO obterporId(int id){
        MedicoModel medico = medicoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Medico com ID " + id+ " não encontrado."));
        return medico.toDTO();
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> obterTodos(){
        List<MedicoModel> medicos = medicoRepository.findAll();
        return medicos.stream().map(medico -> medico.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public MedicoDTO salvar(MedicoModel medico){
        try {

            if (medicoRepository.existsByCrm(medico.getCrm())) {
                throw new ObjectNotFoundException("medico Não encpntrado");
            }
            return medicoRepository.save(medico).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a medico " + medico.getCrm() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar a medico " + medico.getCrm() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao salvar a medico " + medico.getCrm() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar a medico " + medico.getCrm() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao salvar a medico " + medico.getCrm() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public MedicoDTO atualizar(MedicoModel medico){
        try {

            if (!medicoRepository.existsById(medico.getId())) {
                throw new ObjectNotFoundException("medico Não encpntrado");
            }
            return medicoRepository.save(medico).toDTO();

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar a medico " + medico.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar a medico " + medico.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao atualizar a medico " + medico.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar a medico " + medico.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao atualizar a medico " + medico.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public void deletar(MedicoModel medico){
        try {
            if (!medicoRepository.existsById(medico.getId())) {
                throw new ObjectNotFoundException("medico Não encpntrado");
            }
            medicoRepository.delete(medico);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a medico " + medico.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a medico " + medico.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao deletar a medico " + medico.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar a medico " + medico.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao deletar a medico " + medico.getId() + "Não encontrado no bando de dados");
        }
    }
}
