package br.com.projeto.demoparkapi.web.dto.usuarioDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioPasswordDto(

        @NotBlank(message = "Campo não deve está vazio") 
        @Size(min = 6, max = 6) 
        String currentPassword,

        @NotBlank(message = "Campo não deve está vazio") 
        @Size(min = 6, max = 6)
        String newPassword,
        
        @NotBlank(message = "Campo não deve está vazio") 
        @Size(min = 6, max = 6)
        String confirmPassword) {

}
