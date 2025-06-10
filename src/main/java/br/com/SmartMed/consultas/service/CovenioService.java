package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.CovenioModel;
import br.com.SmartMed.consultas.repository.CovenioRepository;
import br.com.SmartMed.consultas.rest.dto.CovenioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CovenioService {

    @Autowired
    private CovenioRepository covenioRepository;

    @Transactional(readOnly = true)
    public CovenioDTO buscarPorID(int id){
        CovenioModel covenio = covenioRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Covenio com o "+id+" Não encontrado"));
        return covenio.toDTO();

    }

    @Transactional(readOnly = true)
    public List<CovenioDTO> buscarTodos(){
        List<CovenioModel> covenios = covenioRepository.findAll();
        return covenios.stream().map(covenio -> covenio.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public CovenioDTO alterar(CovenioModel covenio){
        return covenioRepository.save(covenio).toDTO();
    }

    @Transactional
    public CovenioDTO salvar(CovenioModel covenio){
        return covenioRepository.save(covenio).toDTO();
    }

    @Transactional
    public void deletar(CovenioModel covenio){
        covenioRepository.delete(covenio);
    }
}
