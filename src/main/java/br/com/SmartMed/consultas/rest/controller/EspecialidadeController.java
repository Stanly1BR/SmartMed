package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.model.EspecialidadeModel;
import br.com.SmartMed.consultas.rest.dto.EspecialidadeDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioDeEspecialidadesInputDTO;
import br.com.SmartMed.consultas.rest.dto.RelatorioDeEspecialidadesOutputDTO;
import br.com.SmartMed.consultas.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidade")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeDTO> buscarPorId(@PathVariable int id){
        EspecialidadeDTO especialidadeDTO = especialidadeService.buscarPorID(id);
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeDTO);
    }

    @GetMapping()
    public ResponseEntity<List<EspecialidadeDTO>> obterTodos(){
        List<EspecialidadeDTO> especialidadeDTOS = especialidadeService.buscarTodos();
        return ResponseEntity.ok(especialidadeDTOS);
    }

    @PutMapping()
    public ResponseEntity<EspecialidadeDTO> alterar(@Valid  @RequestBody EspecialidadeModel especialidade){
        EspecialidadeDTO especialidadeDTO = especialidadeService.alterar(especialidade);
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeDTO);
    }

    @PostMapping()
    public ResponseEntity<EspecialidadeDTO> salvar(@Valid  @RequestBody EspecialidadeModel especialidade){
        EspecialidadeDTO especialidadeDTO = especialidadeService.salvar(especialidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeDTO);
    }

    @DeleteMapping()
    public void deletar(@Valid @RequestBody EspecialidadeModel especialidade){
        especialidadeService.deletar(especialidade);
    }

    @PostMapping("/especialidades-frequentes")
    public ResponseEntity<List<RelatorioDeEspecialidadesOutputDTO>> getRelatorioEspecialidades(
            @Valid @RequestBody RelatorioDeEspecialidadesInputDTO input) {

        List<RelatorioDeEspecialidadesOutputDTO> relatorio = especialidadeService.RelatorioEspecialidade(input);
        return ResponseEntity.status(HttpStatus.OK).body(relatorio);
    }
}
