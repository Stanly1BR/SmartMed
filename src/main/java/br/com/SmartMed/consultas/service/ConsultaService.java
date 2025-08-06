package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.model.PacienteModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.repository.PacienteRepository;
import br.com.SmartMed.consultas.rest.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ConsultaDTO buscaPorID(int id) {
        ConsultaModel consulta = consultaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Consulta com o " + id + " Não encpntrado"));
        return modelMapper.map(consulta, ConsultaDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ConsultaDTO> buscarTodos() {
        List<ConsultaModel> consultas = consultaRepository.findAll();
        return consultas.stream().map(consulta -> modelMapper.map(consulta, ConsultaDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public ConsultaDTO atualizar(ConsultaModel consulta) {
        try {

            if (!consultaRepository.existsById(consulta.getId())) {
                throw new ObjectNotFoundException("Consulta  Não encontrado");
            }
            return modelMapper.map(consultaRepository.save(consulta), ConsultaDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar a consulta " + consulta.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar a consulta " + consulta.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao atualizar a consulta " + consulta.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar a consulta " + consulta.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao atualizar a consulta " + consulta.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public ConsultaDTO salvar(ConsultaModel consulta) {
        try {

            return modelMapper.map(consultaRepository.save(consulta), ConsultaDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a consulta " + consulta.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar a consulta " + consulta.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao salvar a consulta " + consulta.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar a consulta " + consulta.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao salvar a consulta " + consulta.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public void deletar(ConsultaModel consulta) {
        try {
            if (!consultaRepository.existsById(consulta.getId())) {
                throw new ConstraintException("Consulta não existente");
            }
            consultaRepository.delete(consulta);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a consulta " + consulta.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a consulta " + consulta.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao deletar a consulta " + consulta.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar a consulta " + consulta.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao deletar a consulta " + consulta.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional(readOnly = true)
    public List<HistoricoPacienteOutputDTO> buscarHistoricoDoPaciente(HistoricoPacienteInputDTO input){

        if (!pacienteRepository.existsById(input.getPacienteID())) {
            throw new ObjectNotFoundException("Paciente com o ID ("+input.getPacienteID()+") não constar no banco de dados.");
        }

        List<HistoricoPacienteOutputDTO> historico = consultaRepository.ConsultasPaciente(
                input.getPacienteID(),
                input.getDatInicio(),
                input.getDatFim(),
                input.getMedicoID(),
                input.getStatus()
        );
        if (historico.isEmpty()) {
            throw new ObjectNotFoundException("Nenhuma consulta encontrada para os critérios informados");
        }

        return historico;
    }

    @Transactional
    public AgendamentoAutomaticoOutputDTO agendarConsultaAutomatico(AgendamentoAutomaticoInputDTO input){
        try {
            validarDados(input);

            //Esta buscando medicos disponiveis
            List<MedicoModel> medicosDisponiveis;
            if(input.getEspecialidadeID() != null){
                medicosDisponiveis = consultaRepository.BuscaMedicosAtivosPorEspecialidade(input.getEspecialidadeID());
            }else{
                medicosDisponiveis = consultaRepository.BuscaMedicosAtivos();
            }

            if(medicosDisponiveis.isEmpty()){
                throw new BusinessRuleException("Nenhum médico disponível para os critérios informados");
            }

            MedicoModel medicoDisponiveil = null;
            LocalDateTime horarioDisponivel = null;

            for(MedicoModel medico : medicosDisponiveis){
                // Verifica se o horário inicial está dentro do horário de atendimento do médico
                LocalDateTime horarioInicial = ajustarHorarioInicial(input.getDataHoraInicial(), medico);

                // Busca consultas do médico para o dia
                List<ConsultaModel> consultasDoDia = consultaRepository.buscarConsultasPorMedicoEData(
                        medico.getId(),
                        horarioInicial.withHour(0).withMinute(0).withSecond(0)
                );

                // Tenta encontrar horário livre
                LocalDateTime horarioLivre = encontrarHorarioLivre(horarioInicial, medico, consultasDoDia);
                if(horarioLivre != null){
                    medicoDisponiveil = medico;
                    horarioDisponivel = horarioLivre;
                    break;
                }
            }
            if (medicoDisponiveil == null || horarioDisponivel == null){
                throw new BusinessRuleException("Nenhum horário disponível encontrado");
            }

            float valorConsulta = medicoDisponiveil.getValorConsultaReferencia();
            if (input.getCovenioID() != null) {
                valorConsulta *= 0.5f; // 50% do valor para consultas com convênio
            }

            ConsultaModel novaConsulta = new ConsultaModel();
            novaConsulta.setDataHoraConsulta(horarioDisponivel);
            novaConsulta.setPacienteID(input.getPacienteID());
            novaConsulta.setMedicoID(medicoDisponiveil.getId());
            novaConsulta.setStatus("AGENDADA");
            novaConsulta.setCovenioID(input.getCovenioID());
            novaConsulta.setFormaPagamentoID(input.getFormaPagamentoID());
            novaConsulta.setDuracaoMinutos(LocalTime.of(0, 30));
            novaConsulta.setValor(valorConsulta);
            novaConsulta.setObservacoes("Consulta agendada automaticamente para " +
                    horarioDisponivel.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) +
                    (input.getCovenioID() != null ? " com convênio" : " particular"));


            ConsultaModel consultaSalva = consultaRepository.save(novaConsulta);

            return new AgendamentoAutomaticoOutputDTO(
                    consultaSalva.getId(),
                    consultaSalva.getDataHoraConsulta(),
                    medicoDisponiveil.getNome(),
                    buscarNomePaciente(input.getPacienteID()),
                    valorConsulta
            );

        } catch (Exception e){
            throw new BusinessRuleException("Erro ao agendar consulta automatica: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public CancelamentoConsultaOutputDTO CancelarConsulta(CancelamentoConsultaInputDTO input){
        Optional<ConsultaModel> consulta = consultaRepository.buscarConsultasAgendadas(input.getConsultaID());

        if(consulta.isEmpty()){
            throw new ObjectNotFoundException("Não tem consultas agendadas para o ID informado");
        }

        ConsultaModel consultaAtual = consulta.get();

        if( consultaAtual.getDataHoraConsulta().isBefore(LocalDateTime.now()) ){
            throw new BusinessRuleException("Apenas consultas futuras podem ser canceladas");
        }
        consultaAtual.setStatus("CANCELADA");
        consultaAtual.setObservacoes(input.getMotivo());



        return new CancelamentoConsultaOutputDTO(
                input.getMotivo(),
                consultaAtual.getStatus().equals("CANCELADA")
        );
    }

    private void validarDados(AgendamentoAutomaticoInputDTO input) {
        if (input.getDataHoraInicial() == null){
            throw new BusinessRuleException("Data/hora inicial é obrigatória");

        }
        if (input.getFormaPagamentoID() == null){
            throw new BusinessRuleException("Forma de pagamento é obrigatória");

        }
        if(input.getPacienteID() == null){
            throw new BusinessRuleException("ID do paciente é obrigatório");

        }
    }

    private LocalDateTime ajustarHorarioInicial(LocalDateTime dataHora, MedicoModel medico) {
        LocalTime horaInicial = dataHora.toLocalTime();
        if (horaInicial.isBefore(medico.getHorarioInicioAtendimento())) {
            return dataHora.with(medico.getHorarioInicioAtendimento());
        }
        return dataHora;
    }

    private LocalDateTime encontrarHorarioLivre(
            LocalDateTime horarioInicial,
            MedicoModel medico,
            List<ConsultaModel> consultasExistentes) {

        LocalDateTime horarioFinal = horarioInicial.toLocalDate()
                .atTime(medico.getHorarioFimAtendimento());

        // Cria conjunto de horários ocupados
        Set<LocalDateTime> horariosOcupados = new HashSet<>();
        for (ConsultaModel consulta : consultasExistentes) {
            LocalDateTime h = consulta.getDataHoraConsulta();
            horariosOcupados.add(h);
            horariosOcupados.add(h.plusMinutes(15));
            horariosOcupados.add(h.plusMinutes(30));
        }

        // Procura próximo horário livre
        LocalDateTime horarioAtual = horarioInicial;
        while (horarioAtual.plusMinutes(30).isBefore(horarioFinal) ||
                horarioAtual.plusMinutes(30).equals(horarioFinal)) {

            boolean horarioLivre = true;
            LocalDateTime verificar = horarioAtual;

            for (int i = 0; i <= 30; i += 15) {
                if (horariosOcupados.contains(verificar.plusMinutes(i))) {
                    horarioLivre = false;
                    break;
                }
            }

            if (horarioLivre) {
                return horarioAtual;
            }

            horarioAtual = horarioAtual.plusMinutes(30);
        }
        return null;
    }

    private String buscarNomePaciente (Integer pacienteId){
        return pacienteRepository.findById(pacienteId)
                .map(PacienteModel::getNome)
                .orElse("Paciente não encontrado");
    }
}


