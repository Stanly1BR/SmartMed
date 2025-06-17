package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.model.FormaPagamentoModel;
import br.com.SmartMed.consultas.rest.dto.FormaPagamentoDTO;
import br.com.SmartMed.consultas.service.FormaPagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formaPagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoDTO> obterPorId(@PathVariable int id){
        FormaPagamentoDTO formaPagamento = formaPagamentoService.buscarPorID(id);
        return ResponseEntity.status(HttpStatus.OK).body(formaPagamento);
    }

    @GetMapping
    public ResponseEntity<List<FormaPagamentoDTO>> obterTodos(){
        List<FormaPagamentoDTO> formaPagamentoDTOList = formaPagamentoService.buscarTodos();
        return ResponseEntity.ok(formaPagamentoDTOList);
    }

    @PutMapping
    public ResponseEntity<FormaPagamentoDTO> alterar(@Valid @RequestBody FormaPagamentoModel forma){
        FormaPagamentoDTO formaDTO = formaPagamentoService.alterar(forma);
        return ResponseEntity.status(HttpStatus.OK).body(formaDTO);
    }

    @PostMapping
    public ResponseEntity<FormaPagamentoDTO> salvar(@Valid @RequestBody FormaPagamentoModel forma){
        FormaPagamentoDTO formaDTO = formaPagamentoService.salvar(forma);
        return ResponseEntity.status(HttpStatus.CREATED).body(formaDTO);
    }

    @DeleteMapping
    public void deletar(@Valid @RequestBody FormaPagamentoModel forma){
        formaPagamentoService.deletar(forma);
    }
}
