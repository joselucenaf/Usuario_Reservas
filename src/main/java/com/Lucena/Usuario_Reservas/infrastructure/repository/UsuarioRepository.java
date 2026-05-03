package com.Lucena.Usuario_Reservas.infrastructure.repository;


import com.Lucena.Usuario_Reservas.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailOrCpf(String email, String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    @Transactional
    void deleteByEmail(String email);
}
