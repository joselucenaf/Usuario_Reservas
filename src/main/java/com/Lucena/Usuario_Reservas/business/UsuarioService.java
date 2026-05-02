package com.Lucena.Usuario_Reservas.business;

import com.Lucena.Usuario_Reservas.business.converter.UsuarioConverter;
import com.Lucena.Usuario_Reservas.business.dto.UsuarioDTO;
import com.Lucena.Usuario_Reservas.infrastructure.entity.Usuario;
import com.Lucena.Usuario_Reservas.infrastructure.exceptions.ConflictException;
import com.Lucena.Usuario_Reservas.infrastructure.exceptions.ResourceNotFoundException;
import com.Lucena.Usuario_Reservas.infrastructure.repository.UsuarioRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Builder
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    public final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        cpfExiste(usuarioDTO.getCpf());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("Email já cadastrado: " + email);
            }
        }catch (ConflictException e){
            throw new ConflictException("Email já cadastrado: " + email, e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }
    public void cpfExiste(String cpf){
        try{
            boolean existe = verificaCpfExistente(cpf);
            if(existe){
                throw new ConflictException("CPF já cadastrado: " + cpf);
            }
        }catch (ConflictException e){
            throw new ConflictException("CPF já cadastrado: " + cpf, e.getCause());
        }
    }

    public boolean verificaCpfExistente(String cpf){
        return usuarioRepository.existsByCpf(cpf);
    }

    public Usuario buscarUsuarioPorLogin(String login) {
        return usuarioRepository.findByEmailOrCpf(login, login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + login));
    }

    public UsuarioDTO buscarUsuarioPorLoginDTO(String login) {
        Usuario usuario = buscarUsuarioPorLogin(login);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void deletarUsuario(String login) {
        Usuario usuario = usuarioRepository.findByEmailOrCpf(login, login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + login));

        usuarioRepository.delete(usuario);
    }
}