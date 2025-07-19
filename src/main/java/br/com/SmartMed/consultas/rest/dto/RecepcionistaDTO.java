package br.com.SmartMed.consultas.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecepcionistaDTO {

    private Integer id;

    private String nome;

    private String cpf;

    private LocalDate dataNascimento;

    private LocalDate dataAdmissao;

    private LocalDate dataDemissao;

    private String telefone;

    private String email;

    private boolean ativo;

}
