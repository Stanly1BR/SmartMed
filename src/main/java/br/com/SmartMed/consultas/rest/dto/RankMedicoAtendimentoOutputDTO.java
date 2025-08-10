package br.com.SmartMed.consultas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankMedicoAtendimentoOutputDTO<T> {
    private List<T> conteudo;
    private int totalPaginas;
    private int paginaAtual;
}
