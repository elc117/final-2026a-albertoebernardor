package com.divideai.service;

import org.mindrot.jbcrypt.BCrypt;

import com.divideai.dto.CadastroRequest;
import com.divideai.dto.LoginRequest;
import com.divideai.dto.UsuarioResponse;
import com.divideai.model.Usuario;
import com.divideai.repository.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    private String hash(String senha){
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public UsuarioResponse cadastrar(CadastroRequest request){
        if (request.nome() == null || request.nome().isBlank()) 
            throw new IllegalArgumentException("Nome não pode ser vazio");

        if (request.email() == null || request.email().isBlank() || !request.email().contains("@")) 
            throw new IllegalArgumentException("Email invalido");

        if (request.senha() == null || request.senha().isBlank()) 
            throw new IllegalArgumentException("Senha não pode ser vazia");

        if (repository.buscarPorEmail(request.email()).isPresent()) 
            throw new IllegalArgumentException("Email ja cadastrado");

        String senhaHash = hash(request.senha());
        Usuario usuario = new Usuario(request.nome(), request.email(), senhaHash);
        Long id = repository.criar(usuario);
        return new UsuarioResponse(id, usuario.getNome(), usuario.getEmail(), usuario.getChavePix());
    }

    public UsuarioResponse login(LoginRequest request){
        if (request.email() == null || request.email().isBlank() || !request.email().contains("@")) 
            throw new IllegalArgumentException("Email invalido");

        if (request.senha() == null || request.senha().isBlank()) 
            throw new IllegalArgumentException("Senha não pode ser vazia");

        Usuario usuario = repository.buscarPorEmail(request.email())
            .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos"));

        if(!BCrypt.checkpw(request.senha(), usuario.getSenhaHash()))
            throw new IllegalArgumentException("Email ou senha inválidos");

        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getChavePix());
    }

    public UsuarioResponse buscarPorId(Long id){
        Usuario usuario = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Id Inválido"));

        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getChavePix());
    }

    public UsuarioResponse atualizarChavePix(Long id, String chavePix){
        repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Id Inválido"));

        repository.atualizarChavePix(id, chavePix);
        Usuario usuario = repository.buscarPorId(id).get();
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getChavePix());
    }
}
