package com.Lucena.Usuario_Reservas.infrastructure.security;


import com.Lucena.Usuario_Reservas.infrastructure.entity.Usuario;
import com.Lucena.Usuario_Reservas.infrastructure.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositório para acessar dados de usuário no banco de dados
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Implementação do método para carregar detalhes do usuário pelo e-mail
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        String loginLimpo = login.replaceAll("[^a-zA-Z0-9@.]", "");
        // Busca o usuário no banco de dados pelo e-mail
        Usuario usuario = usuarioRepository.findByEmailOrCpf(login, login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com: " + login));//excessao

        // Cria e retorna um objeto UserDetails com base no usuário encontrado
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail()) // Define o nome de usuário como o e-mail
                .password(usuario.getSenha()) // Define a senha do usuário
                .build(); // Constrói o objeto UserDetails
    }
}
