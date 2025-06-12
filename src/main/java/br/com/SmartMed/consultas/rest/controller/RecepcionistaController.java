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

    @GetMapping("/{obterPorCpf}")
    public ResponseEntity<RecepcionistaDTO> obterPorCpf(@PathVariable String Cpf){
        RecepcionistaDTO recepcionistaDTO = recepcionistaService.obterPorCpf(Cpf);
        return ResponseEntity.status(HttpStatus.OK).body(recepcionistaDTO);
    }

    @GetMapping()
    public ResponseEntity<List<RecepcionistaDTO>> obterTodos(){
        List<RecepcionistaDTO> recepcionistaDTO = recepcionistaService.obterTodos();
        return  ResponseEntity.ok(recepcionistaDTO);
    }

    @PutMapping()
    public  ResponseEntity<RecepcionistaDTO> alterar(@Valid @RequestBody RecepcionistaModel recepcionista){
        RecepcionistaDTO recepcionistaDTO = recepcionistaService.salvar(recepcionista);
        return ResponseEntity.status(HttpStatus.OK).body(recepcionistaDTO);
    }

    @PostMapping()
    public void deletar(@Valid @RequestBody RecepcionistaModel recepcionista){
        recepcionistaService.deletar(recepcionista);
    }
}
