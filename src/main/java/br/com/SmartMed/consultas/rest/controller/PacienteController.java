package br.com.SmartMed.consultas.rest.controller;


import br.com.SmartMed.consultas.model.PacienteModel;
import br.com.SmartMed.consultas.rest.dto.PacienteDTO;
import br.com.SmartMed.consultas.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos pacientes.
 */
@RestController
@RequestMapping("/paciente")
public class PacienteController {

    /**
     * Instância do serviço de pacientes, responsável por encapsular a lógica de negócios
     * e intermediar as operações entre o controlador e o repositório.
     */
    @Autowired
    private PacienteService pacienteService;

    /**
     * Obtém um paciente pelo ID.
     * Link: http://localhost:8080/api/paciente/?
     *
     * @param id ID do paciente.
     * @return pacienteDTO representando o paciente encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obterPorId(@PathVariable int id) {
        PacienteDTO pacienteDTO = pacienteService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(pacienteDTO);
    }

    /**
     * Obtém a lista de todos os pacientes cadastrados.
     * Link: http://localhost:8080/api/paciente
     *
     * @return Lista de pacienteDTO representando os pacientes cadastrados.
     */
    @GetMapping()
    public ResponseEntity<List<PacienteDTO>> obterTodos() {
        List<PacienteDTO> pacienteDTOList = pacienteService.obterTodos();
        return ResponseEntity.ok(pacienteDTOList);
    }

    /**
     * Salva um novo paciente na base de dados.
     * Link: http://localhost:8080/api/paciente
     *
     * @param novoPaciente PacienteModel contendo os dados do novo paciente.
     * @return pacienteDTO representando o paciente salvo.
     */
    @PostMapping()
    public ResponseEntity<PacienteDTO> salvar(@Valid @RequestBody PacienteModel novoPaciente) {
        PacienteDTO novoPacienteDTO = pacienteService.salvar(novoPaciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPacienteDTO);
    }

    /**
     * Atualiza os dados de um paciente existente.
     * Link: http://localhost:8080/api/paciente
     *
     * @param pacienteExistente pacienteModel contendo os dados atualizados do paciente.
     * @return pacienteDTO representando o paciente atualizado.
     * @link http://localhost:8080/api/paciente
     */
    @PutMapping
    public ResponseEntity<PacienteDTO> atualizar(@Valid @RequestBody PacienteModel pacienteExistente) {
        PacienteDTO pacienteExistenteDTO = pacienteService.atualizar(pacienteExistente);
        return ResponseEntity.status(HttpStatus.OK).body(pacienteExistenteDTO);
    }

    /**
     * Deleta um paciente da base de dados.
     * Link: http://localhost:8080/api/paciente
     *
     * @param pacienteExistente pacienteModel contendo os dados do paciente a ser deletado.
     * @link http://localhost:8080/api/paciente
     */
    @DeleteMapping
    public void deletar(@Valid @RequestBody PacienteModel pacienteExistente) {

        pacienteService.deletar(pacienteExistente);
    }
}
