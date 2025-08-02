package br.com.SmartMed.consultas.rest.dto;

import lombok.Data;

@Data
public class MedicoDTO {
    private int id;
    private String nome;
    private String crm;
    private String telefone;
    private String email;
    private float valorConsultaReferencia;
    private boolean ativo;
    private int especialidadeID;

}
