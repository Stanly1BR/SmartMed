package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.CovenioModel;
import br.com.SmartMed.consultas.repository.CovenioRepository;
import br.com.SmartMed.consultas.rest.dto.CovenioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CovenioService {

    @Autowired
    private CovenioRepository covenioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public CovenioDTO buscarPorId(int id){
        CovenioModel covenio = covenioRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Covenio com o "+id+" Não encontrado"));
        return modelMapper.map(covenio, CovenioDTO.class);

    }

    @Transactional(readOnly = true)
    public List<CovenioDTO> buscarTodos(){
        List<CovenioModel> covenios = covenioRepository.findAll();
        return covenios.stream().map(covenio -> modelMapper.map(covenio, CovenioDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public CovenioDTO alterar(CovenioModel covenio){
        try {

            if(!covenioRepository.existsById(covenio.getId())){
                throw new ConstraintException("Covenio não existe");
            }
            return modelMapper.map(covenioRepository.save(covenio), CovenioDTO.class);

        }catch (DataIntegrityException e){
            throw  new DataIntegrityException("Erro! Não foi possível atualizar a covenio " + covenio.getCnpj() + " !");
        }catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw  new ConstraintException("Erro! Não foi possível atualizar a covenio " + covenio.getCnpj() + ": Restrição de integridade de dados.");
            }
            throw e;
        }catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível atualizar a covenio" + covenio.getCnpj() + "violação da regra de negocio");
        }catch (SQLException e){
            throw new SQLException("Erro! Não foi possível atualizar a covenio " + covenio.getCnpj() + "Falha na conexão com o  banco de dados");
        }catch (ObjectNotFoundException e){
            throw  new ObjectNotFoundException("Erro! Não foi possível atualizar a covenio " + covenio.getCnpj() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public CovenioDTO salvar(CovenioModel covenio){
        try {
            if (covenioRepository.existsByCnpj(covenio.getCnpj())) {
                throw new ConstraintException("Covenio já salvo");
            }
            return modelMapper.map(covenioRepository.save(covenio), CovenioDTO.class);
        }catch (DataIntegrityException e){
            throw  new DataIntegrityException("Erro! Não foi possível salvar a covenio " + covenio.getCnpj() + " !");
        }catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw  new ConstraintException("Erro! Não foi possível salvar a covenio " + covenio.getCnpj() + ": Restrição de integridade de dados.");
            }
            throw e;
        }catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível salvar a covenio" + covenio.getCnpj() + "violação da regra de negocio");
        }catch (SQLException e){
            throw new SQLException("Erro! Não foi possível salvar a covenio " + covenio.getCnpj() + "Falha na conexão com o  banco de dados");
        }catch (ObjectNotFoundException e){
            throw  new ObjectNotFoundException("Erro! Não foi possível salvar a covenio " + covenio.getCnpj() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public void deletar(CovenioModel covenio){
        try {

            if (!covenioRepository.existsById(covenio.getId())){
                throw new ConstraintException("COvenio não existe");
            }
            covenioRepository.delete(covenio);

        }catch (DataIntegrityException e){
            throw  new DataIntegrityException("Erro! Não foi possível deletar a covenio " + covenio.getCnpj() + " !");
        }catch (ConstraintException e){
            if (e.getMessage() == null || e.getMessage().isBlank()){
                throw  new ConstraintException("Erro! Não foi possível deletar a covenio " + covenio.getCnpj() + ": Restrição de integridade de dados.");
            }
            throw e;
        }catch (BusinessRuleException e){
            throw new BusinessRuleException("Erro! Não foi possível deletar a covenio" + covenio.getCnpj() + "violação da regra de negocio");
        }catch (SQLException e){
            throw new SQLException("Erro! Não foi possível deletar a covenio " + covenio.getCnpj() + "Falha na conexão com o  banco de dados");
        }catch (ObjectNotFoundException e){
            throw  new ObjectNotFoundException("Erro! Não foi possível deletar a covenio " + covenio.getCnpj() + "Não encontrado no bando de dados");
        }
    }
}
