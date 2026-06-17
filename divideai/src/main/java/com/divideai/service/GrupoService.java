package com.divideai.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.divideai.dto.GrupoRequest;
import com.divideai.dto.GrupoResponse;
import com.divideai.dto.UsuarioResponse;
import com.divideai.model.Grupo;
import com.divideai.model.Usuario;
import com.divideai.repository.GrupoRepository;

public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public GrupoResponse criarGrupo(GrupoRequest request) {
        if (request.nome() == null || request.nome().isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio");

        String codigoPublico = Integer.toHexString((request.nome() + LocalDate.now()).hashCode());
        Grupo grupo = new Grupo(request.nome(), request.descricao(), LocalDate.now(), codigoPublico);
        Long id = grupoRepository.criar(grupo);
        grupoRepository.adicionarMembro(id, request.criadorId());
        List<Usuario> membros = grupoRepository.listarMembros(id);
        List<UsuarioResponse> participantes = new ArrayList<>();

        for (Usuario u : membros)
            participantes.add(new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getChavePix()));

        return new GrupoResponse(id, grupo.getNome(), grupo.getDescricao(), grupo.getDataCriacao(), participantes, codigoPublico);
    }

    public GrupoResponse buscarPorCodigo(String codigo) {
        Grupo grupo = grupoRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        List<Usuario> membros = grupoRepository.listarMembros(grupo.getId());
        List<UsuarioResponse> participantes = new ArrayList<>();

        for (Usuario u : membros)
            participantes.add(new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getChavePix()));

        return new GrupoResponse(grupo.getId(), grupo.getNome(), grupo.getDescricao(), grupo.getDataCriacao(), participantes, grupo.getCodigoPublico());
    }

    public GrupoResponse buscarPorId(Long id) {
        Grupo grupo = grupoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        List<Usuario> membros = grupoRepository.listarMembros(id);
        List<UsuarioResponse> participantes = new ArrayList<>();

        for (Usuario u : membros)
            participantes.add(new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getChavePix()));

        return new GrupoResponse(grupo.getId(), grupo.getNome(), grupo.getDescricao(), grupo.getDataCriacao(), participantes, grupo.getCodigoPublico());
    }

    public List<GrupoResponse> listar(){
        List<Grupo> grupos = grupoRepository.listar();
        List<GrupoResponse> resposta = new ArrayList<>();

        for (Grupo g : grupos) {
            List<Usuario> membros = grupoRepository.listarMembros(g.getId());
            List<UsuarioResponse> participantes = new ArrayList<>();

            for (Usuario u : membros)
                participantes.add(new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getChavePix()));

            resposta.add(new GrupoResponse(g.getId(), g.getNome(), g.getDescricao(), g.getDataCriacao(), participantes, g.getCodigoPublico()));
        }

        return resposta;
    }

    public List<GrupoResponse> listarPorUsuario(Long usuarioId){
        List<Grupo> grupos = grupoRepository.listarPorUsuario(usuarioId);
        List<GrupoResponse> resposta = new ArrayList<>();

        for (Grupo g : grupos) {
            List<Usuario> membros = grupoRepository.listarMembros(g.getId());
            List<UsuarioResponse> participantes = new ArrayList<>();

            for (Usuario u : membros)
                participantes.add(new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getChavePix()));

            resposta.add(new GrupoResponse(g.getId(), g.getNome(), g.getDescricao(), g.getDataCriacao(), participantes, g.getCodigoPublico()));
        }

        return resposta;
    }

    public GrupoResponse atualizar(Long id, GrupoRequest request){
        Grupo grupo = grupoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        if (request.nome() == null || request.nome().isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio");

        grupo.setNome(request.nome());
        grupo.setDescricao(request.descricao());
        grupoRepository.atualizar(grupo);

        List<Usuario> membros = grupoRepository.listarMembros(id);
        List<UsuarioResponse> participantes = new ArrayList<>();

        for (Usuario u : membros)
            participantes.add(new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getChavePix()));

        return new GrupoResponse(grupo.getId(), grupo.getNome(), grupo.getDescricao(), grupo.getDataCriacao(), participantes, grupo.getCodigoPublico());
    }

    public void deletar(Long id) {
        grupoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        grupoRepository.deletar(id);
    }

    public void adicionarMembro(Long grupoId, Long usuarioId) {
        grupoRepository.buscarPorId(grupoId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        grupoRepository.adicionarMembro(grupoId, usuarioId);
    }

    public void removerMembro(Long grupoId, Long usuarioId) {
        grupoRepository.buscarPorId(grupoId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        grupoRepository.removerMembro(grupoId, usuarioId);
    }

}