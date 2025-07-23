package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.BusinessRuleException;
import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.model.PacienteModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.repository.MedicoRepository;
import br.com.SmartMed.consultas.repository.PacienteRepository;
import br.com.SmartMed.consultas.rest.dto.AgendamentoAutomaticoInputDTO;
import br.com.SmartMed.consultas.rest.dto.AgendamentoAutomaticoOutputDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
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

    @Transactional
    public AgendamentoAutomaticoOutputDTO agendarConsultaAutomaticamente(AgendamentoAutomaticoInputDTO input) {
        PacienteModel paciente = pacienteRepository.findById(input.getPacienteID())
                .orElseThrow(() -> new ObjectNotFoundException("Paciente não encontrado com ID: " + input.getPacienteID()));

        List<MedicoModel> medicosDisponiveis = medicoRepository.findByEspecialidadeIDAndAtivoIsTrue(input.getEspecialidadeID());
        if (medicosDisponiveis.isEmpty()) {
            throw new BusinessRuleException("Nenhum médico ativo encontrado para a especialidade informada.");
        }

        LocalDateTime horarioEncontrado = null;
        MedicoModel medicoSelecionado = null;

        // Limite de busca para evitar loops infinitos (ex: buscar por no máximo 60 dias)
        LocalDateTime limiteBusca = input.getDataHoraInicial().plusDays(60);

        for (MedicoModel medico : medicosDisponiveis) {
            // Buscamos todas as consultas do médico a partir do inicioPeriodo e até o limiteBusca.
            // A lógica de sobreposição será feita em Java.
            List<ConsultaModel> consultasExistentes = consultaRepository
                    .findConsultasByMedicoAndPeriodo(
                            medico.getId(),
                            input.getDataHoraInicial().toLocalDate().atStartOfDay().minusDays(1), // Busca desde o início do dia anterior
                            limiteBusca.toLocalDate().atTime(LocalTime.MAX) // Busca até o fim do dia do limite de busca
                    );
            consultasExistentes.sort(Comparator.comparing(ConsultaModel::getDataHoraConsulta)); // Ordena para facilitar a checagem

            LocalDateTime horarioTentativa = input.getDataHoraInicial();

            while (horarioTentativa.isBefore(limiteBusca)) {
                // Pular fins de semana (sábado e domingo)
                if (horarioTentativa.getDayOfWeek() == DayOfWeek.SATURDAY || horarioTentativa.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    horarioTentativa = horarioTentativa.plusDays(1).with(medico.getHorarioInicioAtendimento());
                    // Garante que o horário comece no início do expediente se pulou o dia
                    if (horarioTentativa.toLocalDate().isAfter(horarioTentativa.toLocalDate().minusDays(1))) {
                        horarioTentativa = LocalDateTime.of(horarioTentativa.toLocalDate(), medico.getHorarioInicioAtendimento());
                    }
                    continue; // Pula para a próxima iteração do loop principal
                }

                // Ajustar para o horário de início do médico se a tentativa for antes do horário de expediente
                if (horarioTentativa.toLocalTime().isBefore(medico.getHorarioInicioAtendimento())) {
                    horarioTentativa = LocalDateTime.of(horarioTentativa.toLocalDate(), medico.getHorarioInicioAtendimento());
                }

                boolean horarioLivreParaAgendamento = true;
                LocalDateTime horarioFimProposto = horarioTentativa.plusMinutes(input.getDuracaoConsultaMinutos());

                // Verificar se o horário de término proposto excede o horário de atendimento do médico para o dia
                if (horarioFimProposto.toLocalTime().isAfter(medico.getHorarioFimAtendimento()) && !horarioTentativa.toLocalTime().equals(medico.getHorarioFimAtendimento())) {
                    horarioTentativa = horarioTentativa.plusDays(1).with(medico.getHorarioInicioAtendimento());
                    continue; // Pula para a próxima iteração do loop principal
                }

                // Verificar conflitos com consultas existentes
                for (ConsultaModel consultaExistente : consultasExistentes) {
                    LocalDateTime inicioExistente = consultaExistente.getDataHoraConsulta();
                    LocalDateTime fimExistente = inicioExistente.plusMinutes(consultaExistente.getDuracaoMinutos());

                    // Lógica de sobreposição de intervalos: (InícioA < FimB) E (FimA > InícioB)
                    if (horarioTentativa.isBefore(fimExistente) && horarioFimProposto.isAfter(inicioExistente)) {
                        horarioLivreParaAgendamento = false; // Há conflito
                        // Se houver conflito, o próximo horário de tentativa deve ser APÓS o fim da consulta existente que causa o conflito.
                        horarioTentativa = fimExistente;
                        break; // Sai do loop de consultas existentes
                    }
                }

                if (horarioLivreParaAgendamento) {
                    horarioEncontrado = horarioTentativa;
                    medicoSelecionado = medico;
                    break; // Sai do loop while (horarioTentativa...)
                } else {
                    // Se não estava livre (por conflito), horarioTentativa já foi ajustado para depois do conflito.
                    // Se não houve conflito mas não estava livre (ex: excedeu o dia, já tratado com continue),
                    // ou se simplesmente o slot atual não funcionou, avançar para o próximo slot
                    if(horarioEncontrado == null) { // Só avança se um horário ainda não foi encontrado
                        horarioTentativa = horarioTentativa.plusMinutes(input.getDuracaoConsultaMinutos());
                    }
                }
            }

            if (horarioEncontrado != null) {
                break; // Sai do loop for (MedicoModel medico...)
            }
        }

        if (horarioEncontrado == null || medicoSelecionado == null) {
            throw new BusinessRuleException("Não foi possível encontrar horário disponível para a especialidade ou médico(s) selecionado(s) no período informado.");
        }

        float valorConsulta = medicoSelecionado.getValorConsultaReferencia();
        if (input.getCovenioID() > 0){
            valorConsulta *= 0.5F; // Desconto de 50%
        }

        ConsultaModel novaConsulta = new ConsultaModel();
        novaConsulta.setDataHoraConsulta(horarioEncontrado);
        novaConsulta.setPacienteID(paciente.getId());
        novaConsulta.setMedicoID(medicoSelecionado.getId());
        novaConsulta.setValor(valorConsulta);
        novaConsulta.setStatus("AGENDADA"); // Novo status para a consulta agendada
        novaConsulta.setFormaPagamentoID(input.getFormaPagamentoID());
        novaConsulta.setCovenioID(input.getCovenioID());
        novaConsulta.setDuracaoMinutos(input.getDuracaoConsultaMinutos());
        novaConsulta.setObservacoes("Agendamento automático via sistema.");
        novaConsulta.setRecepcionistaID(1); // Assumindo recepcionista ID 1 do data.sql

        ConsultaModel consultaSalva = consultaRepository.save(novaConsulta);

        AgendamentoAutomaticoOutputDTO output = modelMapper.map(consultaSalva, AgendamentoAutomaticoOutputDTO.class);
        output.setNomeMedico(medicoSelecionado.getNome());
        output.setNomePaciente(paciente.getNome());

        return output;
    }
}
