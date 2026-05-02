package com.Lucena.Usuario_Reservas.infrastructure.security;

import com.Lucena.Usuario_Reservas.infrastructure.entity.Usuario;
import com.Lucena.Usuario_Reservas.infrastructure.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Limpa o login para o caso de CPF com máscara (ex: 000.111.222-33)
        String loginLimpo = login.replaceAll("[^a-zA-Z0-9@.]", "");

        // Busca usando o login original (e-mail) e o login limpo (CPF)
        Usuario usuario = usuarioRepository.findByEmailOrCpf(login, loginLimpo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com: " + login));

        return org.springframework.security.core.userdetails.User
                .withUsername(login) // Mantém o login original para o Token JWT bater com a requisição
                .password(usuario.getSenha())
                .authorities(new ArrayList<>()) // Garante lista de permissões vazia em vez de nula
                .build();
    }
}