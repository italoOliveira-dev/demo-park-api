package br.com.projeto.demoparkapi.web.dto.usuarioDto;

import br.com.projeto.demoparkapi.models.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

    @Email(message = "Campo Obrigatório!")
    @NotBlank(message = "Campo Obrigatório!")
    String username,
    @NotBlank(message = "Campo Obrigatório!")
    @Size(min = 6, max = 6, message = "Senha deve possuir 6 digitos!")
    String password) {
    
    public Usuario toEntity() {
        return new Usuario(username, password);
    }
}
