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

    private static final LocalTime HORARIO_INICIO_PADRAO = LocalTime.of(8, 0); //
    private static final LocalTime HORARIO_FIM_PADRAO = LocalTime.of(18, 0); //


    @Transactional
    public AgendamentoAutomaticoOutputDTO agendarConsultaAutomaticamente(AgendamentoAutomaticoInputDTO input) {
        // Buscar paciente
        PacienteModel paciente = pacienteRepository.findById(input.getPacienteID()) //
                .orElseThrow(() -> new ObjectNotFoundException("Paciente não encontrado"));

        // Buscar médicos ativos pela especialidade
        List<MedicoModel> medicosDisponiveis = medicoRepository.findByEspecialidadeIDAndAtivoIsTrue(input.getEspecialidadeID()); //
        if (medicosDisponiveis.isEmpty()) {
            throw new BusinessRuleException("Nenhum médico ativo encontrado para a especialidade informada.");
        }

        // Para cada médico, tentar encontrar o primeiro horário disponível
        LocalDateTime horarioEncontrado = null;
        MedicoModel medicoSelecionado = null;

        // Definir um limite superior para a busca para evitar loops infinitos (ex: buscar por no máximo 60 dias)
        LocalDateTime limiteBusca = input.getDataHoraInicial().plusDays(60);

        for (MedicoModel medico : medicosDisponiveis) {
            // Buscamos um período um pouco maior para garantir que consultas que terminam após o início do período de busca sejam consideradas
            // E também que comecem antes e terminem dentro do período.
            // A query findConsultasByMedicoAndPeriodo é mais robusta para isso.
            List<ConsultaModel> consultasExistentes = consultaRepository
                    .findConsultasByMedicoAndPeriodo(
                            medico.getId(),
                            input.getDataHoraInicial().minusDays(2), // Começa a busca 2 dias antes para garantir todas as sobreposições
                            limiteBusca.plusDays(2) // Estende a busca 2 dias depois
                    );
            // Ordena as consultas existentes por data/hora para facilitar a checagem
            consultasExistentes.sort(Comparator.comparing(ConsultaModel::getDataHoraConsulta)); //


            LocalDateTime horarioTentativa = input.getDataHoraInicial();

            // Loop para encontrar o primeiro horário livre
            while (horarioTentativa.isBefore(limiteBusca)) {
                // Pular fins de semana (sábado e domingo)
                if (horarioTentativa.getDayOfWeek() == DayOfWeek.SATURDAY || horarioTentativa.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    horarioTentativa = horarioTentativa.plusDays(1).with(medico.getHorarioInicioAtendimento()); //
                    // Se a data alterou, ajusta o horário para o início do expediente
                    if (horarioTentativa.toLocalDate().isAfter(horarioTentativa.toLocalDate())) {
                        horarioTentativa = LocalDateTime.of(horarioTentativa.toLocalDate(), medico.getHorarioInicioAtendimento()); //
                    }
                    continue;
                }

                // Ajustar para o horário de início do médico se a tentativa for antes do horário de expediente
                if (horarioTentativa.toLocalTime().isBefore(medico.getHorarioInicioAtendimento())) { //
                    horarioTentativa = LocalDateTime.of(horarioTentativa.toLocalDate(), medico.getHorarioInicioAtendimento()); //
                }

                boolean horarioLivreParaAgendamento = true;
                LocalDateTime horarioFimProposto = horarioTentativa.plusMinutes(input.getDuracaoConsultaMinutos()); //

                // Verificar se o horário de término proposto excede o horário de atendimento do médico para o dia
                if (horarioFimProposto.toLocalTime().isAfter(medico.getHorarioFimAtendimento()) && !horarioTentativa.toLocalTime().equals(medico.getHorarioFimAtendimento())) { //
                    // Se o slot proposto excede o fim do expediente, pule para o próximo dia.
                    // Exceto se o horário de tentativa JÁ for o horário final (caso raro de duração 0 ou ajuste).
                    horarioTentativa = horarioTentativa.plusDays(1).with(medico.getHorarioInicioAtendimento()); //
                    continue; // Pula para a próxima iteração do loop principal
                }

                // Verificar conflitos com consultas existentes
                for (ConsultaModel consultaExistente : consultasExistentes) {
                    // Pular a própria consulta se for uma atualização (não aplicável para o seu caso de "agendar" um novo)
                    // if (consultaExistente.getId() == idDaConsultaSendoAtualizada) continue;

                    LocalDateTime inicioExistente = consultaExistente.getDataHoraConsulta(); //
                    LocalDateTime fimExistente = inicioExistente.plusMinutes(consultaExistente.getDuracaoMinutos()); //

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
                    break; // Sai do loop while (horarioTentativa...) pois um horário foi encontrado
                } else {
                    // Se não estava livre (conflito ou excedeu horário), avança para o próximo slot
                    // Se o `break` acima for acionado (conflito), o `horarioTentativa` já foi ajustado.
                    // Se não houver conflito mas `horarioLivreParaAgendamento` é false por outro motivo,
                    // precisamos avançar o `horarioTentativa` manualmente.
                    // A linha `horarioTentativa = fimExistente;` já cuida do avanço se houve conflito.
                    // Se não houve conflito e `horarioLivreParaAgendamento` é false, deve ser pelo `if (horarioFimProposto...)`
                    // Nesse caso, o `continue` já pula para o próximo dia.
                    // Se o loop interno terminar sem conflito e `horarioLivreParaAgendamento` é true, ele entra no `if (horarioLivreParaAgendamento)`
                    // e quebra o loop `while`.
                    // A única forma de chegar aqui seria se o `horarioTentativa` não foi ajustado pelo conflito.
                    // Para garantir que sempre avance, mesmo que a iteração atual não encontre nada:
                    if(horarioTentativa.isBefore(limiteBusca) && horarioEncontrado == null) {
                        horarioTentativa = horarioTentativa.plusMinutes(input.getDuracaoConsultaMinutos()); //
                    }
                }
            }

            if (horarioEncontrado != null) {
                break; // Sai do loop for (MedicoModel medico...) pois um médico e horário foram encontrados
            }
        }


        if (horarioEncontrado == null || medicoSelecionado == null) {
            throw new BusinessRuleException("Não foi possível encontrar horário disponível para a especialidade ou médico(s) selecionado(s) no período informado.");
        }

        float valorConsulta = medicoSelecionado.getValorConsultaReferencia(); //
        if (input.getCovenioID() > 0){ //
            valorConsulta *= 0.5F; // Desconto de 50%
        }

        // Criar e salvar a consulta
        ConsultaModel novaConsulta = new ConsultaModel(); //
        novaConsulta.setDataHoraConsulta(horarioEncontrado); //
        novaConsulta.setPacienteID(paciente.getId()); //
        novaConsulta.setMedicoID(medicoSelecionado.getId()); //
        novaConsulta.setValor(valorConsulta); //
        novaConsulta.setStatus("AGENDADA"); //
        novaConsulta.setFormaPagamentoID(input.getFormaPagamentoID()); //
        novaConsulta.setCovenioID(input.getCovenioID()); //
        novaConsulta.setDuracaoMinutos(input.getDuracaoConsultaMinutos()); //
        novaConsulta.setObservacoes(""); // Você pode adicionar uma observação padrão ou permitir no input
        novaConsulta.setRecepcionistaID(0); // Definir conforme a regra de negócio (se houver recepcionista)


        ConsultaModel consultaSalva = consultaRepository.save(novaConsulta); //

        // Faz o DTO de saída
        AgendamentoAutomaticoOutputDTO output = modelMapper.map(consultaSalva, AgendamentoAutomaticoOutputDTO.class); //
        output.setNomeMedico(medicoSelecionado.getNome()); //
        output.setNomePaciente(paciente.getNome()); //

        return output;
    }

}