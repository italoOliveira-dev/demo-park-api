package br.com.projeto.demoparkapi.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.demoparkapi.models.service.UsuarioService;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioPasswordDto;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioRequestDTO;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioResponseDTO;
import br.com.projeto.demoparkapi.web.exceptionHandler.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Usuarios", 
    description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usário. "
    )
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @Operation(
        summary = "Criar um novo usuário",
        description = "Recurso para criar um novo usuário",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Recurso criado com sucesso", 
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Username 'email' já foi cadastrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Recurso não processado por dados de entrada inválidos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO usuarioDto, UriComponentsBuilder uBuilder) {

        UsuarioResponseDTO user = usuarioService.salvar(usuarioDto);
        URI uri = uBuilder.path("api/v1/usuarios/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @Operation(
        summary = "Obter um usuário pelo id",
        description = "Recurso onde realiza uma busca de usuário com o id",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário encontrado com sucesso", 
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado", 
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obter(@PathVariable Long id) {
        UsuarioResponseDTO user = usuarioService.obter(id);

        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Listar todos usuários",
        description = "Recurso onde realiza uma Listagem de usuários",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de usuários", 
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> user = usuarioService.obterTodos();

        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Atualizar Senha",
        description = "Recurso onde realiza atualização de senha",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Senha atualizada com sucesso", 
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado", 
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Senha não confere", 
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Recurso não processado por dados de entrada inválidos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)
                )
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> modificarPassword(@PathVariable Long id, @RequestBody @Valid UsuarioPasswordDto passwordDto) {
        usuarioService.editarPassword(id, passwordDto);
        return ResponseEntity.noContent().build();
    }
}
