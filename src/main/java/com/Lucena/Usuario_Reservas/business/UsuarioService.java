package com.Lucena.Usuario_Reservas.business;


import com.Lucena.Usuario_Reservas.business.converter.UsuarioConverter;
import com.Lucena.Usuario_Reservas.business.dto.UsuarioDTO;
import com.Lucena.Usuario_Reservas.infrastructure.entity.Usuario;
import com.Lucena.Usuario_Reservas.infrastructure.repository.UsuarioRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }


}
