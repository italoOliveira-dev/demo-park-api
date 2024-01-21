package br.com.projeto.demoparkapi.web.dto.usuarioDto;

import br.com.projeto.demoparkapi.models.entity.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String username,
        String role) {

    public static UsuarioResponseDTO fromUsuario(Usuario usuario) {
        String role = usuario.getRole().name().substring("ROLE_".length());

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                role);
    }
}
