package br.com.SmartMed.consultas.service;

import br.com.SmartMed.consultas.exception.BusinessRuleException;
import br.com.SmartMed.consultas.exception.ConstraintException;
import br.com.SmartMed.consultas.exception.ObjectNotFoundException;
import br.com.SmartMed.consultas.model.UsuarioModel;
import br.com.SmartMed.consultas.repository.UsuarioRepository;
import br.com.SmartMed.consultas.rest.dto.LoginResponseDTO;
import br.com.SmartMed.consultas.rest.dto.UsuarioInputDTO;
import br.com.SmartMed.consultas.rest.dto.UsuarioLoginOutputDTO;
import br.com.SmartMed.consultas.rest.dto.UsuarioOutputDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioOutputDTO salvar(UsuarioInputDTO novoUsuarioDTO) {
        Optional<UsuarioModel> usuarioExistente = usuarioRepository.BuscarUsuarioPorEmail(novoUsuarioDTO.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new ConstraintException("Já existe um usuário com o e-mail " + novoUsuarioDTO.getEmail() + "!");
        }

        try {
            String senhaCriptografada = passwordEncoder.encode(novoUsuarioDTO.getSenha());
            novoUsuarioDTO.setSenha(senhaCriptografada);

            UsuarioModel novoUsuario = modelMapper.map(novoUsuarioDTO, UsuarioModel.class);
            UsuarioModel usuarioSalvo = usuarioRepository.save(novoUsuario);

            UsuarioOutputDTO outputDTO = new UsuarioOutputDTO();
            outputDTO.setMensagem("Usuário cadastrado com sucesso");
            outputDTO.setUsuarioID(usuarioSalvo.getId());

            return outputDTO;
        } catch (Exception e) {
            throw new BusinessRuleException("Erro ao salvar o usuário. Verifique os dados fornecidos.");
        }
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(String email, String senha) {
        UsuarioModel usuario = usuarioRepository.BuscarUsuarioPorEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Erro de autenticação."));

        boolean senhaCorreta = passwordEncoder.matches(senha, usuario.getSenha());

        if (!senhaCorreta) {
            throw new BusinessRuleException("Erro de autenticação.");
        }

        UsuarioLoginOutputDTO usuarioLoginResponseDTO = modelMapper.map(usuario, UsuarioLoginOutputDTO.class);

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setMensagem("Login realizado com sucesso");
        responseDTO.setUsuario(usuarioLoginResponseDTO);
        responseDTO.setToken("eyJhbGci0iJIUzI1NiIsInR5cCI6Ikp...");

        return responseDTO;
    }
}