package br.com.projeto.demoparkapi.web.dto.usuarioDto;

import br.com.projeto.demoparkapi.models.entity.Usuario;

public record UsuarioRequestDTO(String username, String password, String role) {
    
    public Usuario toEntity() {
        return new Usuario(username, password, role);
    }
}
