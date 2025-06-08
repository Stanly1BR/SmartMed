package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.repository.MedicoRepository;
import br.com.SmartMed.consultas.rest.dto.MedicoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService  {

    private MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public MedicoDTO obterporId(int id){
        MedicoModel medico = medicoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Medico com ID " + id + " não encontrado."));
        return medico.toDTO();
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> obterTodos(){
        List<MedicoModel> medicos = medicoRepository.findAll();
        return medicos.stream().map(medico -> medico.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public MedicoDTO salvar(MedicoModel medico){
        return medicoRepository.save(medico).toDTO();
    }

    @Transactional
    public MedicoDTO atualizar(MedicoModel medico){
        return medicoRepository.save(medico).toDTO();
    }

    @Transactional
    public void deletar(MedicoModel medico){
        medicoRepository.delete(medico);
    }
}
