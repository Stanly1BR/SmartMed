package br.com.SmartMed.consultas.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultaDTO {

    private int id;

    private LocalDateTime dataHoraConsulta;

    private String status;

    private float valor;

    private String observacoes;

    private int pacienteID;

    private int medicoID;

    private int formaPagamentoID;

    private int covenioID;

    private int recepcionistaID;

}
