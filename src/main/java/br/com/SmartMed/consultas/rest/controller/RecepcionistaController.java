package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.model.RecepcionistaModel;
import br.com.SmartMed.consultas.rest.dto.PacienteDTO;
import br.com.SmartMed.consultas.rest.dto.RecepcionistaDTO;
import br.com.SmartMed.consultas.service.RecepcionistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recepcionista")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @GetMapping("/{id}")
    public ResponseEntity<RecepcionistaDTO> obterPorId(@PathVariable int id){
        RecepcionistaDTO recepcionistaDTO = recepcionistaService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(recepcionistaDTO);
    }

    @GetMapping()
    public ResponseEntity<List<RecepcionistaDTO>> obterTodos(){
        List<RecepcionistaDTO> recepcionistaDTO = recepcionistaService.obterTodos();
        return  ResponseEntity.ok(recepcionistaDTO);
    }

    @PutMapping()
    public  ResponseEntity<RecepcionistaDTO> alterar(@Valid @RequestBody RecepcionistaModel recepcionista){
        RecepcionistaDTO recepcionistaDTO = recepcionistaService.atualizar(recepcionista);
        return ResponseEntity.status(HttpStatus.OK).body(recepcionistaDTO);
    }

    @PostMapping()
    public  ResponseEntity<RecepcionistaDTO> salvar(@Valid @RequestBody RecepcionistaModel recepcionista){
        RecepcionistaDTO recepcionistaDTO = recepcionistaService.salvar(recepcionista);
        return ResponseEntity.status(HttpStatus.CREATED).body(recepcionistaDTO);
    }

    @DeleteMapping()
    public void deletar(@Valid @RequestBody RecepcionistaModel recepcionista){
        recepcionistaService.deletar(recepcionista);
    }
}
