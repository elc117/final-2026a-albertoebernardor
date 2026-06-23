package com.divideai;

import com.divideai.controller.GrupoController;
import com.divideai.controller.UsuarioController;
import com.divideai.repository.GrupoRepository;
import com.divideai.repository.UsuarioRepository;
import com.divideai.service.GrupoService;
import com.divideai.service.UsuarioService;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        UsuarioRepository usuarioRepository = new UsuarioRepository();
        GrupoRepository grupoRepository = new GrupoRepository();

        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        GrupoService grupoService = new GrupoService(grupoRepository);

        UsuarioController usuarioController = new UsuarioController(usuarioService);
        GrupoController grupoController = new GrupoController(grupoService);

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });
        });

        usuarioController.registrarRotas(app);
        grupoController.registarRotas(app);

        app.start(port);
    }
}
