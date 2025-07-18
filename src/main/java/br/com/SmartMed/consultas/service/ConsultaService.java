package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.rest.dto.ConsultaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ConsultaDTO buscaPorID(int id) {
        ConsultaModel consulta = consultaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Consulta com o " + id + " Não encpntrado"));
        return modelMapper.map(consulta, ConsultaDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ConsultaDTO> buscarTodos() {
        List<ConsultaModel> consultas = consultaRepository.findAll();
        return consultas.stream().map(consulta -> modelMapper.map(consulta, ConsultaDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public ConsultaDTO atualizar(ConsultaModel consulta) {
        try {

            if (!consultaRepository.existsById(consulta.getId())) {
                throw new ObjectNotFoundException("Consulta  Não encontrado");
            }
            return modelMapper.map(consultaRepository.save(consulta), ConsultaDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar a consulta " + consulta.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar a consulta " + consulta.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao atualizar a consulta " + consulta.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar a consulta " + consulta.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao atualizar a consulta " + consulta.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public ConsultaDTO salvar(ConsultaModel consulta) {
        try {
            if (consultaRepository.existsById(consulta.getId())) {
                throw new ObjectNotFoundException("Consulta já salvar");
            }
            return modelMapper.map(consultaRepository.save(consulta), ConsultaDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a consulta " + consulta.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar a consulta " + consulta.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao salvar a consulta " + consulta.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar a consulta " + consulta.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao salvar a consulta " + consulta.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public void deletar(ConsultaModel consulta) {
        try {
            if (!consultaRepository.existsById(consulta.getId())) {
                throw new ConstraintException("Consulta não existente");
            }
            consultaRepository.delete(consulta);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a consulta " + consulta.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a consulta " + consulta.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao deletar a consulta " + consulta.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar a consulta " + consulta.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao deletar a consulta " + consulta.getId() + "Não encontrado no bando de dados");
        }
    }

}
