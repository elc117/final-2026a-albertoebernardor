package com.divideai.controller;

import com.divideai.dto.CadastroRequest;
import com.divideai.dto.LoginRequest;
import com.divideai.dto.UsuarioResponse;
import com.divideai.service.UsuarioService;

import io.javalin.Javalin;

public class UsuarioController{
    private final UsuarioService service;

    public UsuarioController(UsuarioService service){
        this.service = service;
    }

    public void registrarRotas(Javalin app) {
        app.post("/usuarios/cadastro", ctx -> {
            CadastroRequest request = ctx.bodyAsClass(CadastroRequest.class);
            UsuarioResponse response = service.cadastrar(request);
            ctx.json(response);
            ctx.status(201);
        });

        app.post("/usuarios/login", ctx -> {
            LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
            UsuarioResponse response = service.login(request);
            ctx.json(response);
        });

        app.get("/usuarios/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            UsuarioResponse response = service.buscarPorId(id);
            ctx.json(response);
        });

        app.put("/usuarios/{id}/pix", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            String chavePix = ctx.body();
            UsuarioResponse response = service.atualizarChavePix(id, chavePix);
            ctx.json(response);
        });
    }
}
