package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.rest.dto.MedicoAgendaInputDTO;
import br.com.SmartMed.consultas.rest.dto.MedicoAgendaOutputDTO;
import br.com.SmartMed.consultas.rest.dto.MedicoDTO;
import br.com.SmartMed.consultas.service.MedicoAgendaService;
import br.com.SmartMed.consultas.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private MedicoAgendaService medicoAgendaService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> ObterPorCrm(@PathVariable int id){
        MedicoDTO medico = medicoService.obterporId(id);
        return ResponseEntity.status(HttpStatus.OK).body(medico);
    }

    @GetMapping()
    public ResponseEntity<List<MedicoDTO>> ObterTodos(){
        List<MedicoDTO> medico = medicoService.obterTodos();
        return ResponseEntity.ok(medico);
    }

    @PutMapping
    public ResponseEntity<MedicoDTO> alterar(@Valid @RequestBody MedicoModel medico){
        MedicoDTO medicoDTO = medicoService.atualizar(medico);
        return ResponseEntity.status(HttpStatus.OK).body(medicoDTO);
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> salvar(@Valid @RequestBody MedicoModel medico){
        MedicoDTO medicoDTO = medicoService.salvar(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoDTO);
    }

    @PostMapping("/agenda")
    public ResponseEntity<MedicoAgendaOutputDTO> getAgendaMedico(
            @Valid @RequestBody MedicoAgendaInputDTO input) {
        MedicoAgendaOutputDTO agenda = medicoAgendaService.getAgendaMedico(input);
        return ResponseEntity.ok(agenda);
    }

    @DeleteMapping
    public void deletar(@Valid @RequestBody MedicoModel medico){
        medicoService.deletar(medico);
    }
}
