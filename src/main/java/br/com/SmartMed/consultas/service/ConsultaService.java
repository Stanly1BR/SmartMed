package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.rest.dto.ConsultaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public ConsultaDTO buscaPorID(int id){
        ConsultaModel consulta = consultaRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Cpnsulta com o "+id+" Não encpntrado"));
        return consulta.toDTO();
    }

    @Transactional(readOnly = true)
    public List<ConsultaDTO> buscarTodos(){
        List<ConsultaModel> consultas = consultaRepository.findAll();
        return consultas.stream().map(consulta -> consulta.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public ConsultaDTO atualizar(ConsultaModel consulta){
        return consultaRepository.save(consulta).toDTO();
    }

    @Transactional
    public ConsultaDTO salvar(ConsultaModel consulta){
        return consultaRepository.save(consulta).toDTO();
    }

    @Transactional
    public void deletar(ConsultaModel consulta){
        consultaRepository.delete(consulta);
    }

}
