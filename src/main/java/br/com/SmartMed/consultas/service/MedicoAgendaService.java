package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.BusinessRuleException;
import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.repository.MedicoRepository;
import br.com.SmartMed.consultas.rest.dto.MedicoAgendaInputDTO;
import br.com.SmartMed.consultas.rest.dto.MedicoAgendaOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicoAgendaService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    private static final int DURACAO_PADRAO_CONSULTA_MINUTOS = 30;

    @Transactional(readOnly = true)
    public MedicoAgendaOutputDTO getAgendaMedico(MedicoAgendaInputDTO input) {
        // 1. Validar se o médico existe e está ativo
        MedicoModel medico = medicoRepository.findById(input.getMedicoID())
                .orElseThrow(() -> new ObjectNotFoundException("Médico não encontrado com ID: " + input.getMedicoID()));

        if (!medico.isAtivo()) {
            throw new BusinessRuleException("Não é possível consultar a agenda de um médico inativo.");
        }

        // 2. Definir o período de busca para o dia
        LocalDateTime inicioDoDia = input.getData().atStartOfDay();
        LocalDateTime fimDoDia = input.getData().atTime(LocalTime.MAX);

        // 3. Buscar consultas existentes do médico para a data informada
        List<ConsultaModel> consultasExistentes = consultaRepository
                .findConsultasByMedicoAndDataRange(
                        input.getMedicoID(),
                        inicioDoDia,
                        fimDoDia
                );

        // 4. Gerar todos os slots de horário possíveis para o dia, dentro do expediente do médico
        List<LocalTime> todosOsSlotsDoDia = gerarSlotsHorario(
                medico.getHorarioInicioAtendimento(),
                medico.getHorarioFimAtendimento(),
                DURACAO_PADRAO_CONSULTA_MINUTOS
        );

        List<String> horariosOcupados = new ArrayList<>();
        List<String> horariosDisponiveis = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime agora = LocalDateTime.now(); // Momento atual para filtrar horários passados

        // 5. Iterar sobre os slots e determinar disponibilidade
        for (LocalTime slotTime : todosOsSlotsDoDia) {
            LocalDateTime slotDateTime = input.getData().atTime(slotTime);

            // Regra de Negócio: Não retornar horários anteriores ao momento atual
            if (slotDateTime.isBefore(agora)) {
                continue; // Pula este slot, pois já passou
            }

            boolean isOcupado = false;
            LocalDateTime slotFim = slotDateTime.plusMinutes(DURACAO_PADRAO_CONSULTA_MINUTOS);

            // Verificar se o slot proposto se sobrepõe a alguma consulta existente
            for (ConsultaModel consulta : consultasExistentes) {
                LocalDateTime inicioConsultaExistente = consulta.getDataHoraConsulta();
                LocalDateTime fimConsultaExistente = inicioConsultaExistente.plusMinutes(consulta.getDuracaoMinutos());


                if (slotDateTime.isBefore(fimConsultaExistente) && slotFim.isAfter(inicioConsultaExistente)) {
                    isOcupado = true;
                    break;
                }
            }

            if (isOcupado) {
                horariosOcupados.add(slotTime.format(formatter));
            } else {
                horariosDisponiveis.add(slotTime.format(formatter));
            }
        }

        // 6. Construir e retornar o DTO de saída
        return new MedicoAgendaOutputDTO(
                medico.getNome(),
                input.getData(),
                horariosOcupados,
                horariosDisponiveis
        );
    }


    private List<LocalTime> gerarSlotsHorario(LocalTime inicio, LocalTime fim, int intervaloMinutos) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime currentSlot = inicio;
        while (currentSlot.isBefore(fim) || currentSlot.equals(fim)) {
            slots.add(currentSlot);
            currentSlot = currentSlot.plusMinutes(intervaloMinutos);
        }
        return slots;
    }
}
