package br.com.projeto.demoparkapi.models.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projeto.demoparkapi.exception.EntityNotFoundException;
import br.com.projeto.demoparkapi.exception.PasswordException;
import br.com.projeto.demoparkapi.exception.UsernameUniqueViolationException;
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
        try{
            return UsuarioResponseDTO.fromUsuario(usuarioRepository.save(usuarioDto.toEntity()));
        } catch(DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' já foi cadastrado", usuarioDto.username()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario obterPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id=%d não encontrado.", id)));
    }

    @Transactional
    public void editarPassword(Long id, UsuarioPasswordDto passwordDto) {
        Usuario user = obterPorId(id);

        if (!passwordDto.newPassword().equals(passwordDto.confirmPassword())) {
            throw new PasswordException("Nova senha não confere com a comfirmação de senha.");
        }

        if (!user.getPassword().equals(passwordDto.currentPassword())) {
            throw new PasswordException("Sua senha não confere.");
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
