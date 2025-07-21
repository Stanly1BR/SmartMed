package br.com.SmartMed.consultas.service.caso02;

import br.com.SmartMed.consultas.exception.BusinessRuleException;
import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.model.PacienteModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.repository.MedicoRepository;
import br.com.SmartMed.consultas.repository.PacienteRepository;
import br.com.SmartMed.consultas.rest.dto.caso02.AgendamentoAutomaticoInputDTO;
import br.com.SmartMed.consultas.rest.dto.caso02.AgendamentoAutomaticoOutputDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoAutomaticoService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final LocalTime HORARIO_INICIO_PADRAO = LocalTime.of(8, 0);
    private static final LocalTime HORARIO_FIM_PADRAO = LocalTime.of(18, 0);


    @Transactional
    public AgendamentoAutomaticoOutputDTO agendarConsultaAutomaticamente(AgendamentoAutomaticoInputDTO input) {
        // Buscar paciente
        PacienteModel paciente = pacienteRepository.findById(input.getPacienteID())
                .orElseThrow(() -> new ObjectNotFoundException("Paciente não encontrado"));

        // Buscar médicos disponíveis pela especialidade
        List<MedicoModel> medicosDisponiveis = medicoRepository.findByEspecialidadeIDAndAtivoIsTrue(input.getEspecialidadeID());
        if (medicosDisponiveis.isEmpty()) {
            throw new BusinessRuleException("Nenhum médico encontrado para a especialidade informada");
        }

        // Para cada médico, tentar encontrar o primeiro horário disponível
        LocalDateTime horarioEncontrado = null;
        MedicoModel medicoSelecionado = null;

        for (MedicoModel medico : medicosDisponiveis) {
            // Buscar consultas existentes do médico a partir da data inicial
            List<ConsultaModel> consultasExistentes = consultaRepository
                    .findByMedicoIDAndDataHoraConsultaGreaterThanEqual(
                            medico.getId(),
                            input.getDataHoraInicial());

            // Encontrar primeiro horário livre
            LocalDateTime horarioTentativa = input.getDataHoraInicial();

            if (horarioTentativa.toLocalTime().isBefore(HORARIO_INICIO_PADRAO)) {
                horarioTentativa = LocalDateTime.of(
                        horarioTentativa.toLocalDate(),
                        HORARIO_INICIO_PADRAO
                );
            }

            while (horarioTentativa.toLocalTime().isBefore(HORARIO_FIM_PADRAO)) {
                boolean horarioLivre = true;

                for (ConsultaModel consulta : consultasExistentes) {
                    if (consulta.getDataHoraConsulta().equals(horarioTentativa)) {
                        horarioLivre = false;
                        break;
                    }
                }

                if (horarioLivre) {
                    horarioEncontrado = horarioTentativa;
                    medicoSelecionado = medico;
                    break;
                }
                horarioTentativa = horarioTentativa.plusMinutes(input.getDuracaoConsultaMinutos());
            }

            if (horarioEncontrado != null) break;
        }

        if (horarioEncontrado == null || medicoSelecionado == null) {
            throw new BusinessRuleException("Não foi possível encontrar horário disponível");
        }

        float valorConsulta = medicoSelecionado.getValorConsultaReferencia();
        if (input.getCovenioID() > 0){
            valorConsulta *= 0.5F; // Desconto de 50%
        }

        // Criar e salvar a consulta
        ConsultaModel novaConsulta = new ConsultaModel();
        novaConsulta.setDataHoraConsulta(horarioEncontrado);
        novaConsulta.setPacienteID(paciente.getId());
        novaConsulta.setMedicoID(medicoSelecionado.getId());
        novaConsulta.setValor(valorConsulta);
        novaConsulta.setStatus("AGENDADA");
        novaConsulta.setFormaPagamentoID(input.getFormaPagamentoID());
        novaConsulta.setCovenioID(input.getCovenioID());

        ConsultaModel consultaSalva = consultaRepository.save(novaConsulta);

        // Faz o DTO de saída
        AgendamentoAutomaticoOutputDTO output = modelMapper.map(consultaSalva, AgendamentoAutomaticoOutputDTO.class);
        output.setNomeMedico(medicoSelecionado.getNome());
        output.setNomePaciente(paciente.getNome());

        return output;
    }

}
