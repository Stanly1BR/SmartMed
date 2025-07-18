package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.PacienteModel;
import br.com.SmartMed.consultas.repository.PacienteRepository;
import br.com.SmartMed.consultas.rest.dto.PacienteDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações relacionadas aos pacientes.
 */
@Service
public class PacienteService {

    /**
     * Instância do repositório de pacientes, responsável por realizar operações de
     * persistência e consulta diretamente no banco de dados.
     */
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * Obtém um paciente pelo ID.
     *
     * @param id ID do paciente.
     * @return pacienteDTO representando o paciente encontrado.
     * @throws ObjectNotFoundException Se o paciente não for encontrado.
     */
    @Transactional(readOnly = true)
    public PacienteDTO obterPorId(int id) {
        PacienteModel paciente = pacienteRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Paciente com ID " + id + " não encontrado."));
        return modelMapper.map(paciente, PacienteDTO.class);
    }

    /**
     * Obtém a lista de todos os pacientes cadastrados.
     *
     * @return Lista de PacienteDTO representando os pacientes cadastrados.
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> obterTodos() {
        List<PacienteModel> pacientes = pacienteRepository.findAll();
        return pacientes.stream()
                .map(paciente -> modelMapper.map(paciente, PacienteDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Salva um novo paciente na base de dados.
     *
     * @param novoPaciente pacienteModel contendo os dados do novo paciente.
     * @return pacienteDTO representando o paciente salvo.
     * @throws ConstraintException    Se o telefone ou e-mail já existirem.
     * @throws DataIntegrityException Se ocorrer violação de integridade.
     * @throws BusinessRuleException  Se houver violação de regra de negócio.
     * @throws SQLException           Se ocorrer falha de conexão com o banco de dados.
     */
    @Transactional
    public PacienteDTO salvar(PacienteModel novoPaciente) {

        try {

            //Caso ocorra uma tentativa de salvar um novo paciente com um cpf já existente.
            if (pacienteRepository.existsByCpf(novoPaciente.getCpf())) {
                throw new ConstraintException("Já existe um paciente com esse CPF " + novoPaciente.getCpf() + " na base de dados!");
            }

            //Salva o novo paciente na base de dados.
            return modelMapper.map(pacienteRepository.save(novoPaciente), PacienteDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar o paciente " + novoPaciente.getNome() + " !");
        } catch (ConstraintException e) {
            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar o paciente " + novoPaciente.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar o paciente " + novoPaciente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar o paciente " + novoPaciente.getNome() + ". Falha na conexão com o banco de dados!");
        }
    }

    /**
     * Atualiza os dados de um paciente existente.
     *
     * @param pacienteExistente pacienteModel contendo os dados atualizados do cliete.
     * @return pacienteDTO representando o paciente atualizado.
     * @throws ConstraintException    Se o telefone ou e-mail não existir.
     * @throws DataIntegrityException Se ocorrer violação de integridade.
     * @throws BusinessRuleException  Se houver violação de regra de negócio.
     * @throws SQLException           Se ocorrer falha de conexão com o banco de dados.
     */
    @Transactional
    public PacienteDTO atualizar(PacienteModel pacienteExistente) {

        try {
            //Caso ocorra uma tentativa de salvar um paciente que não existe utilizando um CPF.
            if (!pacienteRepository.existsByCpf(pacienteExistente.getCpf())) {
                throw new ConstraintException("O paciente com esse CPF " + pacienteExistente.getCpf() + " não existe na base de dados!");
            }

            //Atualiza o paciente na base de dados.
            return modelMapper.map(pacienteRepository.save(pacienteExistente), PacienteDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar o paciente " + pacienteExistente.getNome() + " !");
        } catch (ConstraintException e) {
            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o paciente " + pacienteExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar o paciente " + pacienteExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o paciente " + pacienteExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar o paciente" + pacienteExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }


    /**
     * Deleta um paciente da base de dados.
     *
     * @param pacienteExistente pacienteModel contendo os dados do paciente a ser deletado.
     * @throws ConstraintException    Se o paciente (id) não existir.
     * @throws DataIntegrityException Se ocorrer violação de integridade.
     * @throws BusinessRuleException  Se houver violação de regra de negócio.
     * @throws SQLException           Se ocorrer falha de conexão com o banco de dados.
     */
    @Transactional
    public void deletar(PacienteModel pacienteExistente) {

        try {
            //Caso ocorra uma tentativa de deletar um paciente que não existe utilizando o id.
            if (!pacienteRepository.existsById(pacienteExistente.getId())) {
                throw new ConstraintException("Paciente inexistente na base de dados!");
            }

            //Deletar o paciente na base de dados.
            pacienteRepository.delete(pacienteExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar o paciente " + pacienteExistente.getNome() + " !");
        } catch (ConstraintException e) {
            // Relança a mensagem original ou adiciona contexto
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar o paciente " + pacienteExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar o paciente " + pacienteExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o deletar " + pacienteExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o paciente" + pacienteExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }
}
