package com.Lucena.Usuario_Reservas.business;

import com.Lucena.Usuario_Reservas.business.converter.UsuarioConverter;
import com.Lucena.Usuario_Reservas.business.dto.TelefoneDTO;
import com.Lucena.Usuario_Reservas.business.dto.UsuarioDTO;
import com.Lucena.Usuario_Reservas.infrastructure.entity.Telefone;
import com.Lucena.Usuario_Reservas.infrastructure.entity.Usuario;
import com.Lucena.Usuario_Reservas.infrastructure.exceptions.ConflictException;
import com.Lucena.Usuario_Reservas.infrastructure.exceptions.ResourceNotFoundException;
import com.Lucena.Usuario_Reservas.infrastructure.repository.TelefoneRepository;
import com.Lucena.Usuario_Reservas.infrastructure.repository.UsuarioRepository;
import com.Lucena.Usuario_Reservas.infrastructure.security.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        cpfExiste(usuarioDTO.getCpf());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        if (verificaEmailExistente(email)) {
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void cpfExiste(String cpf) {
        if (verificaCpfExistente(cpf)) {
            throw new ConflictException("CPF já cadastrado: " + cpf);
        }
    }

    public boolean verificaCpfExistente(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    public UsuarioDTO buscarUsuarioPorLogin(String login) {
        Usuario usuario = usuarioRepository.findByEmailOrCpf(login, login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + login));
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void deletarUsuario(String login) {
        Usuario usuario = usuarioRepository.findByEmailOrCpf(login, login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + login));

        usuarioRepository.delete(usuario);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        // Busca o identificador (email ou cpf) do usuário através do Token
        String login = jwtUtil.extrairEmailToken(token.substring(7));

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Usuario usuarioEntity = usuarioRepository.findByEmailOrCpf(login, login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + login));
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto) {
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado " + idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}