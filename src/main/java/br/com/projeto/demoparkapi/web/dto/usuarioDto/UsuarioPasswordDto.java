package br.com.projeto.demoparkapi.web.dto.usuarioDto;

public record UsuarioPasswordDto(String currentPassword, String newPassword, String confirmPassword) {
    
}
