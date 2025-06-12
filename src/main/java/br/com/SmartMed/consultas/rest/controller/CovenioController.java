package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.CovenioModel;
import br.com.SmartMed.consultas.model.FormaPagamentoModel;
import br.com.SmartMed.consultas.rest.dto.CovenioDTO;
import br.com.SmartMed.consultas.rest.dto.FormaPagamentoDTO;
import br.com.SmartMed.consultas.service.CovenioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/covenio")
public class CovenioController {

    @Autowired
    private CovenioService covenioService;

    @GetMapping("/{cnpj}")
    public ResponseEntity<CovenioDTO> obterPorId(@PathVariable String cnpj){
        CovenioDTO covenio = covenioService.buscarPorCnpj(cnpj);
        return ResponseEntity.status(HttpStatus.OK).body(covenio);
    }

    @GetMapping
    public ResponseEntity<List<CovenioDTO>> obterTodos(){
        List<CovenioDTO> covenio = covenioService.buscarTodos();
        return ResponseEntity.ok(covenio);
    }

    @PutMapping
    public ResponseEntity<CovenioDTO> alterar(@Valid @RequestBody CovenioModel covenio){
        CovenioDTO covenioDTO = covenioService.alterar(covenio);
        return ResponseEntity.status(HttpStatus.OK).body(covenioDTO);
    }

    @PostMapping
    public ResponseEntity<CovenioDTO> salvar(@Valid @RequestBody CovenioModel covenio){
        CovenioDTO covenioDTO = covenioService.alterar(covenio);
        return ResponseEntity.status(HttpStatus.CREATED).body(covenioDTO);
    }

    @DeleteMapping
    public void deletar(@Valid @RequestBody CovenioModel covenio){
        covenioService.deletar(covenio);
    }
}
