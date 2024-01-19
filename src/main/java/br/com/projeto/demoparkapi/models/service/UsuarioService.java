package br.com.projeto.demoparkapi.models.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projeto.demoparkapi.models.entity.Usuario;
import br.com.projeto.demoparkapi.models.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario obterPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Usuário não encontrado!")
        );
    }

    @Transactional
    public Usuario editarPassword(Long id, String password) {
        Usuario user = obterPorId(id);
        user.setPassword(password);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> obterTodos() {
        return usuarioRepository.findAll();
    }
    
}
