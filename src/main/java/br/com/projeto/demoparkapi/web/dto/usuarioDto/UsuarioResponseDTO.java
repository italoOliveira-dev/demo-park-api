package br.com.projeto.demoparkapi.web.dto.usuarioDto;

import java.time.LocalDateTime;

import br.com.projeto.demoparkapi.models.entity.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String username,
        String role,
        LocalDateTime dataCriacao,
        LocalDateTime dataModificacao,
        String criadoPor,
        String modificadoPor) {

    public static UsuarioResponseDTO fromUsuario(Usuario usuario) {
        String role = usuario.getRole().name().substring("ROLE_".length());

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                role,
                usuario.getDataCriacao(),
                usuario.getDataModificacao(),
                usuario.getCriadoPor(),
                usuario.getModificadoPor());
    }
}
