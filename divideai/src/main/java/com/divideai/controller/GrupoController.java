package com.divideai.controller;

import java.util.List;

import com.divideai.dto.GrupoRequest;
import com.divideai.dto.GrupoResponse;
import com.divideai.dto.UsuarioResponse;
import com.divideai.service.GrupoService;
import com.divideai.service.UsuarioService;

import io.javalin.Javalin;

public class GrupoController{
    private final GrupoService service;
    private final UsuarioService usuarioService;

    public GrupoController(GrupoService service, UsuarioService usuarioService){
        this.service = service;
        this.usuarioService = usuarioService;
    }

    public void registarRotas(Javalin app) {        //cria grupo
        app.post("/grupo/criar", ctx -> {
            GrupoRequest request = ctx.bodyAsClass(GrupoRequest.class);
            GrupoResponse  response = service.criarGrupo(request);
            ctx.json(response);
            ctx.status(201); //status criacao
        });

        app.get("/usuarios/{id}/grupos", ctx ->{      //lista grupos de um usuario
            Long id = Long.parseLong(ctx.pathParam("id"));
            List<GrupoResponse> response = service.listarPorUsuario(id);
            ctx.json(response);
            ctx.status(200); //status padrao ok
        });

        app.get("/grupos/{id}/participantes", ctx ->{     //Lista Participantes de um grupo
            Long id = Long.parseLong(ctx.pathParam("id"));
            GrupoResponse grupo = service.buscarPorId(id);
            List<UsuarioResponse> response = grupo.participantes();
            ctx.json(response);
            ctx.status(200); //
        });
        app.get("/grupos/{id}", ctx -> {        // retorna dados grupo                  
            Long id = Long.parseLong(ctx.pathParam("id"));
            GrupoResponse response = service.buscarPorId(id);
            ctx.json(response);
        });


        app.put("/grupos/{id}", ctx ->{        // atualiza  nome e descrição do grupo
            Long id = Long.parseLong(ctx.pathParam("id"));
            GrupoRequest request = ctx.bodyAsClass(GrupoRequest.class);
            GrupoResponse response = service.atualizar(id, request);
            ctx.json(response);
        });

        app.delete("/grupos/{id}", ctx ->{     // deleta grupo
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.deletar(id);
            ctx.status(204);
        });

        app.post("/grupos/{grupoId}/membros/email", ctx -> {   // adiciona membro por email
            Long grupoId = Long.parseLong(ctx.pathParam("grupoId"));
            String email = ctx.body();
            Long usuarioId = usuarioService.buscarPorEmail(email).id();
            service.adicionarMembro(grupoId, usuarioId);
            ctx.status(201);
        });

        app.post("/grupos/{grupoId}/membros/{usuarioId}", ctx -> {   //adiciona usuario em um grupo
            Long grupoId = Long.parseLong(ctx.pathParam("grupoId"));
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            service.adicionarMembro(grupoId, usuarioId);
            ctx.status(201);
        });

        app.delete("/grupos/{grupoId}/membros/{usuarioId}", ctx -> {     // remove um usuario de um grupo
            Long grupoId = Long.parseLong(ctx.pathParam("grupoId"));
            Long usuarioId = Long.parseLong(ctx.pathParam("usuarioId"));
            service.removerMembro(grupoId, usuarioId);
            ctx.status(204);
        });

        app.get("/public/{codigo}", ctx -> {      // rota publica do grupo
            String codigo = ctx.pathParam("codigo");
            GrupoResponse response = service.buscarPorCodigo(codigo);
            ctx.json(response);
        });

    }

}