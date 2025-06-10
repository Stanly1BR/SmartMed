package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
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
        return especialidadeRepository.save(especialidade).toDTO();
    }

    @Transactional
    public EspecialidadeDTO salvar(EspecialidadeModel especialidade){
        return especialidadeRepository.save(especialidade).toDTO();
    }

    @Transactional
    public void deletar(EspecialidadeModel especialidade){
        especialidadeRepository.delete(especialidade);
    }
}
