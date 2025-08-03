package br.com.SmartMed.consultas.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListagemRecepcionistasOutputDTO<T> {
    private List<T> recepcionistas;
    private int totalPagina;
    private int pagina;
}
