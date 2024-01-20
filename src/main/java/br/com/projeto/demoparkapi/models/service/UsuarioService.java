package br.com.projeto.demoparkapi.models.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projeto.demoparkapi.models.entity.Usuario;
import br.com.projeto.demoparkapi.models.repository.UsuarioRepository;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioPasswordDto;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioRequestDTO;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO usuarioDto) {
        return UsuarioResponseDTO.fromUsuario(usuarioRepository.save(usuarioDto.toEntity()));
    }

    @Transactional(readOnly = true)
    public Usuario obterPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado!"));
    }

    @Transactional
    public void editarPassword(Long id, UsuarioPasswordDto passwordDto) {
        Usuario user = obterPorId(id);

        if (!passwordDto.newPassword().equals(passwordDto.confirmPassword())) {
            throw new RuntimeException("Nova senha não confere com a comfirmação de senha.");
        }

        if (!user.getPassword().equals(passwordDto.currentPassword())) {
            throw new RuntimeException("Sua senha não confere.");
        }

        user.setPassword(passwordDto.newPassword());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obterTodos() {
        return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::fromUsuario).toList();
    }

    public UsuarioResponseDTO obter(Long id) {
        Usuario user = obterPorId(id);
        return UsuarioResponseDTO.fromUsuario(user);
    }

}
