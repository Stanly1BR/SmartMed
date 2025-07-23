package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.FormaPagamentoModel;
import br.com.SmartMed.consultas.rest.dto.AgendamentoAutomaticoInputDTO;
import br.com.SmartMed.consultas.rest.dto.AgendamentoAutomaticoOutputDTO;
import br.com.SmartMed.consultas.rest.dto.ConsultaDTO;
import br.com.SmartMed.consultas.rest.dto.FormaPagamentoDTO;
import br.com.SmartMed.consultas.service.AgendamentoAutomaticoService;
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

    @Autowired
    private AgendamentoAutomaticoService agendamentoAutomaticoService;

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

    @PostMapping("/agendamentos-automatico")
    public ResponseEntity<AgendamentoAutomaticoOutputDTO> agendarConsulta(
            @Valid @RequestBody AgendamentoAutomaticoInputDTO input){

        AgendamentoAutomaticoOutputDTO resultado = agendamentoAutomaticoService.agendarConsultaAutomaticamente(input);

        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping
    public void deletar(@Valid @RequestBody ConsultaModel consulta){
        consultaService.deletar(consulta);
    }
}
