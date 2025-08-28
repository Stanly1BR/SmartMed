package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.*;
import br.com.SmartMed.consultas.model.RecepcionistaModel;
import br.com.SmartMed.consultas.repository.RecepcionistaRepository;
import br.com.SmartMed.consultas.rest.dto.ListagemRecepcionistasOutputDTO;
import br.com.SmartMed.consultas.rest.dto.ListagemRecepcionistasInputDTO;
import br.com.SmartMed.consultas.rest.dto.RecepcionistaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class RecepcionistaService {

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public RecepcionistaDTO obterPorId(int id){
        RecepcionistaModel recepcionista = recepcionistaRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Recepcionista com o "+id+" não encontrado"));
        return modelMapper.map(recepcionista, RecepcionistaDTO.class);
    }

    @Transactional(readOnly = true)
    public List<RecepcionistaDTO> obterTodos(){
        List<RecepcionistaModel> recepcionistas = recepcionistaRepository.findAll();
        return recepcionistas.stream().map(recepcionista -> modelMapper.map(recepcionista, RecepcionistaDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public RecepcionistaDTO salvar(RecepcionistaModel recepcionista){
        try {

            if (recepcionistaRepository.existsByCpf(recepcionista.getCpf())) {
                throw new ConstraintException("O recepcionista com esse CPF " + recepcionista.getCpf() + " não existe na base de dados!");
            }

            return modelMapper.map(recepcionistaRepository.save(recepcionista), RecepcionistaDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar o recepcionista " + recepcionista.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao salvar o recepcionista " + recepcionista.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar o recepcionista " + recepcionista.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar o recepcionista " + recepcionista.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível salvar o recepcionista" + recepcionista.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public RecepcionistaDTO atualizar(RecepcionistaModel recepcionista){
        try {

            if (!recepcionistaRepository.existsById(recepcionista.getId())) {
                throw new ConstraintException("O recepcionista com esse CPF " + recepcionista.getId() + " não existe na base de dados!");
            }

            return modelMapper.map(recepcionistaRepository.save(recepcionista), RecepcionistaDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar o recepcionista " + recepcionista.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar o recepcionista " + recepcionista.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar o recepcionista " + recepcionista.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o recepcionista " + recepcionista.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar o recepcionista" + recepcionista.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public void deletar(RecepcionistaModel recepcionista){
        try {

            if (!recepcionistaRepository.existsByCpf(recepcionista.getCpf())) {
                throw new ConstraintException("O recepcionista com esse CPF " + recepcionista.getCpf() + " não existe na base de dados!");
            }

            recepcionistaRepository.delete(recepcionista);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar o recepcionista " + recepcionista.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar o recepcionista " + recepcionista.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar o recepcionista " + recepcionista.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível deletar o recepcionista " + recepcionista.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o recepcionista" + recepcionista.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional(readOnly = true)
    public ListagemRecepcionistasOutputDTO<RecepcionistaDTO> listarRecepcionistas(ListagemRecepcionistasInputDTO input) {
        boolean ativo = parseStatus(input.getStatus());

        Sort sort = buildSort(input.getOrdenarPordataAdmissao(), input.getDirecao());
        Pageable pageable = PageRequest.of(
                Math.max(0, input.getPagina()),
                Math.max(1, input.getTamanhoPagina()),
                sort
        );

        Page<RecepcionistaModel> page = recepcionistaRepository.listagemRecepcionistas(
                input.getDataInicio(),
                input.getDataFim(),
                ativo,
                pageable
        );

        List<RecepcionistaDTO> conteudo = page.getContent().stream()
                .map(this::toDto)
                .toList();

        return new ListagemRecepcionistasOutputDTO<>(
                conteudo,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }

    private boolean parseStatus(String status) {
        if (status == null) return true;
        String s = status.trim().toUpperCase(Locale.ROOT);
        return switch (s) {
            case "ATIVO", "TRUE", "1", "SIM" -> true;
            case "INATIVO", "FALSE", "0", "NAO", "NÃO" -> false;
            default -> true;
        };
    }

    private Sort buildSort(String ordenarPor, String direcao) {
        String property = mapOrdenarPorToEntityProperty(ordenarPor);
        Sort.Direction dir = "DESC".equalsIgnoreCase(direcao) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, property);
    }

    private String mapOrdenarPorToEntityProperty(String ordenarPor) {
        if (ordenarPor == null) return "dataAdmissao";
        String p = ordenarPor.trim().toLowerCase(Locale.ROOT);
        return switch (p) {
            case "nome" -> "nome";
            case "dataadmissao", "data_admissao" -> "dataAdmissao";
            case "email" -> "email";
            case "cpf" -> "cpf";
            default -> "dataAdmissao";
        };
    }

    private RecepcionistaDTO toDto(RecepcionistaModel m) {
        RecepcionistaDTO dto = new RecepcionistaDTO();
        dto.setNome(m.getNome());
        dto.setCpf(m.getCpf());
        dto.setEmail(m.getEmail());
        dto.setDataAdmissao(m.getDataAdmissao());
        dto.setAtivo(m.isAtivo());
        return dto;
    }
}
