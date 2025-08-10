package br.com.SmartMed.consultas.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoConsultaInputDTO {
    @NotNull
    private Integer consultaID;
    @NotNull
    @NotBlank
    private String motivo;
}
