package br.com.SmartMed.consultas.rest.dto;

import lombok.Data;

@Data
public class CovenioDTO {

    private int id;

    private String nome;

    private String cnpj;

    private String telefone;

    private String email;

    private boolean ativo;

}
