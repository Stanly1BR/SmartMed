package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.BusinessRuleException;
import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.PacienteModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.repository.PacienteRepository;
import br.com.SmartMed.consultas.rest.dto.HistoricoConsultaInputDTO;
import br.com.SmartMed.consultas.rest.dto.HistoricoConsultaOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class HistoricoConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional(readOnly = true)
    public List<HistoricoConsultaOutputDTO> getHIstoricoConsultas(HistoricoConsultaInputDTO input){
        // 1. Validar se o paciente existe e está ativo
        PacienteModel paciente = pacienteRepository.findById(input.getPacienteID())
                .orElseThrow(()-> new ObjectNotFoundException("Paciente não encontrado com ID: " + input.getPacienteID()));

        if (!paciente.isAtivo()){
            throw  new BusinessRuleException("Não é possível consultar o histórico de um paciente inativo.");
        }

        // 2. Preparar os parâmetros de data para a query
        LocalDateTime dataInicio = null;
        if (input.getDataInicio() != null){
            dataInicio = input.getDataInicio().atStartOfDay();
        }

        LocalDateTime datafim = null;
        if (input.getDataFim() != null){
            datafim = input.getDataFim().atTime(LocalTime.MAX);
        }

        // 3. Chamar o repositório com todos os filtros
        List<HistoricoConsultaOutputDTO> historico = consultaRepository.findHistoricoConsultasByPacienteAndFilters(
                input.getPacienteID(),
                dataInicio,
                datafim,
                input.getMedicoID(),
                input.getStatus(),
                input.getEspecialidadeID()
        );

        return historico;
    }
}
