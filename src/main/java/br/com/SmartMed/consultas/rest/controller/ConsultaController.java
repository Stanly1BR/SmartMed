package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.rest.dto.*;
import br.com.SmartMed.consultas.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> obterPorId(@PathVariable int id){
        ConsultaDTO consultaDTO = consultaService.buscaPorID(id);
        return ResponseEntity.status(HttpStatus.OK).body(consultaDTO);
    }

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> obterTodos(){
        List<ConsultaDTO> consultaDTO = consultaService.buscarTodos();
        return ResponseEntity.ok(consultaDTO);
    }

    @PutMapping
    public ResponseEntity<ConsultaDTO> alterar(@Valid @RequestBody ConsultaModel forma){
        ConsultaDTO consultaDTO = consultaService.atualizar(forma);
        return ResponseEntity.status(HttpStatus.OK).body(consultaDTO);
    }

    @PostMapping
    public ResponseEntity<ConsultaDTO> salvar(@Valid @RequestBody ConsultaModel consulta){
        ConsultaDTO consultaDTO = consultaService.salvar(consulta);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaDTO);
    }

    @DeleteMapping
    public void deletar(@Valid @RequestBody ConsultaModel consulta){
        consultaService.deletar(consulta);
    }
}
