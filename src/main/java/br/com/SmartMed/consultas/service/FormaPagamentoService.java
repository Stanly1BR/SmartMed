package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.EspecialidadeModel;
import br.com.SmartMed.consultas.model.FormaPagamentoModel;
import br.com.SmartMed.consultas.repository.FormaPagamentoRepository;
import br.com.SmartMed.consultas.rest.dto.EspecialidadeDTO;
import br.com.SmartMed.consultas.rest.dto.FormaPagamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Transactional(readOnly = true)
    public FormaPagamentoDTO buscarPorID(int id){
        FormaPagamentoModel forma = formaPagamentoRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Forma com o "+id+" não encontrado"));
        return forma.toDTO();
    }

    @Transactional(readOnly = true)
    public List<FormaPagamentoDTO> buscarTodos(){
        List<FormaPagamentoModel> forma = formaPagamentoRepository.findAll();
        return forma.stream().map(formaPagamentoModel -> formaPagamentoModel.toDTO()).collect(Collectors.toList());
    }

    @Transactional
    public FormaPagamentoDTO alterar(FormaPagamentoModel forma){
        return formaPagamentoRepository.save(forma).toDTO();
    }

    @Transactional
    public FormaPagamentoDTO salvar(FormaPagamentoModel forma){
        return formaPagamentoRepository.save(forma).toDTO();
    }

    @Transactional
    public void deletar(FormaPagamentoModel forma){
        formaPagamentoRepository.delete(forma);
    }
}
