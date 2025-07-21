package br.com.SmartMed.consultas.rest.controller.caso02;

import br.com.SmartMed.consultas.rest.dto.caso02.AgendamentoAutomaticoInputDTO;
import br.com.SmartMed.consultas.rest.dto.caso02.AgendamentoAutomaticoOutputDTO;
import br.com.SmartMed.consultas.service.caso02.AgendamentoAutomaticoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos-automatico")
public class AgendamentoAutomaticoController {

    @Autowired
    private AgendamentoAutomaticoService agendamentoAutomaticoService;

    @PostMapping
    public ResponseEntity<AgendamentoAutomaticoOutputDTO> agendarConsulta(
            @Valid @RequestBody AgendamentoAutomaticoInputDTO input){

        AgendamentoAutomaticoOutputDTO resultado = agendamentoAutomaticoService.agendarConsultaAutomaticamente(input);

        return ResponseEntity.ok(resultado);
    }
}
