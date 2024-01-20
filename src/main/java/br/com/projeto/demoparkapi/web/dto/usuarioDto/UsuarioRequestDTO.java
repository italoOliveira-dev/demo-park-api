package br.com.projeto.demoparkapi.web.dto.usuarioDto;

import br.com.projeto.demoparkapi.models.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

    @Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "Formato de email está inválido.")
    @NotBlank(message = "Campo não deve está vazio")
    String username,
    @NotBlank(message = "Campo não deve está vazio")
    @Size(min = 6, max = 6)
    String password, 
    String role) {
    
    public Usuario toEntity() {
        return new Usuario(username, password, role);
    }
}
