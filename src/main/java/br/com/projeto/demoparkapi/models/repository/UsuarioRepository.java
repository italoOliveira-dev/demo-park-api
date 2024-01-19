package br.com.projeto.demoparkapi.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.demoparkapi.models.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
}
