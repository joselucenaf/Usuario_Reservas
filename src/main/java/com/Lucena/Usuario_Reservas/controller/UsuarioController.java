package com.Lucena.Usuario_Reservas.controller;

import com.Lucena.Usuario_Reservas.business.UsuarioService;
import com.Lucena.Usuario_Reservas.business.dto.TelefoneDTO;
import com.Lucena.Usuario_Reservas.business.dto.UsuarioDTO;
import com.Lucena.Usuario_Reservas.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Cadastro e login de usuários na plataforma")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    @Operation(summary = "Salvar Usuários", description = "Cria um novo usuário")
    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Usuário já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor")
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login Usuários", description = "Login do usuário")
    @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais Inválidas")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDTO) {
        String loginFornecido = (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().isBlank())
                ? usuarioDTO.getEmail()
                : usuarioDTO.getCpf();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginFornecido, usuarioDTO.getSenha())
        );

        String token = "Bearer " + jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dados de Usuário por ID", description = "Busca os dados do usuário usando o ID numérico")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @GetMapping
    @Operation(summary = "Buscar dados de Usuários por Login", description = "Buscar dados do usuário")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorLogin(@RequestParam("login") String login) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorLogin(login));
    }

    @DeleteMapping("/{login}")
    @Operation(summary = "Deletar Usuário por Login", description = "Deleta usuário")
    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String login){
        usuarioService.deletarUsuario(login);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(summary = "Atualizar Dados de Usuários", description = "Atualiza dados de usuário")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor")
    public ResponseEntity<UsuarioDTO> atualizaDadoUsuario(@RequestBody UsuarioDTO dto,
                                                          @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Atualizar Telefone de Usuários", description = "Atualiza telefone de usuário")
    @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }
}