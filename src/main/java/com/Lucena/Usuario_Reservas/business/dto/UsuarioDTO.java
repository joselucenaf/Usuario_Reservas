package com.Lucena.Usuario_Reservas.business.dto;


import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private List<TelefoneDTO> telefones;

}
