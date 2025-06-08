package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
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
    public RecepcionistaDTO obterPorId(int id){
        RecepcionistaModel recepcionista = recepcionistaRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Recepcionista com o "+id+" não encontrado"));
        return recepcionista.toDTO();
    }

    @Transactional(readOnly = true)
    public List<RecepcionistaDTO> obterTodos(){
        List<RecepcionistaModel> recepcionistas = recepcionistaRepository.findAll();
        return recepcionistas.stream().map(recepcionista -> recepcionista.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public RecepcionistaDTO salvar(RecepcionistaModel recepcionista){
        return recepcionistaRepository.save(recepcionista).toDTO();
    }

    @Transactional
    public RecepcionistaDTO atualizar(RecepcionistaModel recepcionista){
        return recepcionistaRepository.save(recepcionista).toDTO();
    }

    @Transactional
    public void deletar(RecepcionistaModel recepcionista){
        recepcionistaRepository.delete(recepcionista);
    }
}
