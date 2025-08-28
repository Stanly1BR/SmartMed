package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListagemRecepcionistasInputDTO {

    private String status;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int pagina = 0;
    private int tamanhoPagina = 10;
    private String ordenarPordataAdmissao;
    private String direcao;

}
