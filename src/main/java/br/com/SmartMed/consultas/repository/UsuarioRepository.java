package br.com.SmartMed.consultas.repository;

import br.com.SmartMed.consultas.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    @Query("SELECT u FROM UsuarioModel u WHERE u.email = :pEmail")
    Optional<UsuarioModel> BuscarUsuarioPorEmail(String pEmail);
}
