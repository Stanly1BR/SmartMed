package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioOutputDTO {
    private String mensagem;
    private int usuarioID;
}
