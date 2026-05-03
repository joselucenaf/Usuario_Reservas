package com.Lucena.Usuario_Reservas.controller;

import com.Lucena.Usuario_Reservas.business.UsuarioService;
import com.Lucena.Usuario_Reservas.business.dto.UsuarioDTO;
import com.Lucena.Usuario_Reservas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        String loginFornecido = (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().isBlank())
                ? usuarioDTO.getEmail()
                : usuarioDTO.getCpf();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginFornecido, usuarioDTO.getSenha())
        );

        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorLogin(@RequestParam("login") String login) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorLoginDTO(login));
    }


    @DeleteMapping("/{login}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String login){
        usuarioService.deletarUsuario(login);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizaDadoUsuario(@RequestBody UsuarioDTO dto,
                                                          @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }
}