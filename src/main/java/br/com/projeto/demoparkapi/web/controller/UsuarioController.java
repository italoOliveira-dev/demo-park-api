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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO usuarioDto, UriComponentsBuilder uBuilder) {

        UsuarioResponseDTO user = usuarioService.salvar(usuarioDto);
        URI uri = uBuilder.path("api/v1/usuarios/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obter(@PathVariable Long id) {
        UsuarioResponseDTO user = usuarioService.obter(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> user = usuarioService.obterTodos();

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modificarPassword(@PathVariable Long id, @RequestBody @Valid UsuarioPasswordDto passwordDto) {
        usuarioService.editarPassword(id, passwordDto);
        return ResponseEntity.noContent().build();
    }
}
