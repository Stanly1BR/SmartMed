package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioInputDTO {
    private String nome;
    private String email;
    private String perfil;
    private String senha;
}
