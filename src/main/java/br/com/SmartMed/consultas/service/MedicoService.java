package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.ConsultaModel;
import br.com.SmartMed.consultas.model.MedicoModel;
import br.com.SmartMed.consultas.repository.ConsultaRepository;
import br.com.SmartMed.consultas.repository.MedicoRepository;
import br.com.SmartMed.consultas.rest.dto.AgendaMEdicoOutputDTO;
import br.com.SmartMed.consultas.rest.dto.AgendaMedicoInputDTO;
import br.com.SmartMed.consultas.rest.dto.MedicoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicoService  {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public MedicoDTO obterporId(int id){
        MedicoModel medico = medicoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Medico com ID " + id+ " não encontrado."));
        return modelMapper.map(medico, MedicoDTO.class);
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> obterTodos(){
        List<MedicoModel> medicos = medicoRepository.findAll();
        return medicos.stream().map(medico -> modelMapper.map(medico, MedicoDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public MedicoDTO salvar(MedicoModel medico){
        try {

            if (medicoRepository.existsByCrm(medico.getCrm())) {
                throw new ObjectNotFoundException("medico Não encpntrado");
            }
            return modelMapper.map(medicoRepository.save(medico), MedicoDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a medico " + medico.getCrm() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar a medico " + medico.getCrm() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao salvar a medico " + medico.getCrm() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar a medico " + medico.getCrm() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao salvar a medico " + medico.getCrm() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public MedicoDTO atualizar(MedicoModel medico){
        try {

            if (!medicoRepository.existsById(medico.getId())) {
                throw new ObjectNotFoundException("medico Não encpntrado");
            }
            return modelMapper.map(medicoRepository.save(medico), MedicoDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar a medico " + medico.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar a medico " + medico.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao atualizar a medico " + medico.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar a medico " + medico.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao atualizar a medico " + medico.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional
    public void deletar(MedicoModel medico){
        try {
            if (!medicoRepository.existsById(medico.getId())) {
                throw new ObjectNotFoundException("medico Não encpntrado");
            }
            medicoRepository.delete(medico);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a medico " + medico.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a medico " + medico.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao deletar a medico " + medico.getId() + "violação da regra de negocio");
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar a medico " + medico.getId() + "Falha na conexão com o  banco de dados");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro ao deletar a medico " + medico.getId() + "Não encontrado no bando de dados");
        }
    }

    @Transactional(readOnly = true)
    public AgendaMEdicoOutputDTO consultarAgendaMedico(AgendaMedicoInputDTO input) {

        MedicoModel medico = consultaRepository.buscarMedico(input.getMedicoID())
                .orElseThrow(() -> new ObjectNotFoundException("Médico não encontrado ou inativo"));

        LocalDateTime inicioDia = input.getData()
                .withHour(medico.getHorarioInicioAtendimento().getHour())
                .withMinute(medico.getHorarioInicioAtendimento().getMinute())
                .withSecond(0);

        LocalDateTime fimDia = input.getData()
                .withHour(medico.getHorarioFimAtendimento().getHour())
                .withMinute(medico.getHorarioFimAtendimento().getMinute())
                .withSecond(0);

        List<ConsultaModel> consultasDoDia = consultaRepository.buscarConsultasPorMedicoEData(
                input.getMedicoID(),
                inicioDia
        );

        // Cria conjunto de horários ocupados
        Set<String> horariosOcupados = consultasDoDia.stream()
                .map(consulta -> consulta.getDataHoraConsulta().toLocalTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toSet());

        // Faz horários disponíveis
        List<String> horariosDisponiveis = new ArrayList<>();
        LocalDateTime horarioAtual = inicioDia;
        LocalDateTime agora = LocalDateTime.now();

        while (!horarioAtual.isAfter(fimDia.minusMinutes(30))) {
            String horarioFormatado = horarioAtual.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

            if (!horariosOcupados.contains(horarioFormatado) && horarioAtual.isAfter(agora)) {
                horariosDisponiveis.add(horarioFormatado);
            }

            horarioAtual = horarioAtual.plusMinutes(30);
        }

        return new AgendaMEdicoOutputDTO(
                medico.getNome(),
                input.getData(),
                new ArrayList<>(horariosOcupados),
                horariosDisponiveis
        );
    }
}
