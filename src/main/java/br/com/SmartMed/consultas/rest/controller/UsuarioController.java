package br.com.SmartMed.consultas.rest.controller;

import br.com.SmartMed.consultas.rest.dto.LoginResponseDTO;
import br.com.SmartMed.consultas.rest.dto.UsuarioInputDTO;
import br.com.SmartMed.consultas.rest.dto.UsuarioLoginInputDTO;
import br.com.SmartMed.consultas.rest.dto.UsuarioOutputDTO;
import br.com.SmartMed.consultas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioOutputDTO> cadastrar(@Valid @RequestBody UsuarioInputDTO request) {
        UsuarioOutputDTO resposta = usuarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UsuarioLoginInputDTO request) {
        LoginResponseDTO resposta = usuarioService.login(request.getEmail(), request.getSenha());
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}