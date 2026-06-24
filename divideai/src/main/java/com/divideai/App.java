package com.divideai;

import com.divideai.controller.DespesaController;
import com.divideai.controller.GrupoController;
import com.divideai.controller.UsuarioController;
import com.divideai.repository.DespesaRepository;
import com.divideai.repository.GrupoRepository;
import com.divideai.repository.UsuarioRepository;
import com.divideai.service.DespesaService;
import com.divideai.service.GrupoService;
import com.divideai.service.SaldoService;
import com.divideai.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class App {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        UsuarioRepository usuarioRepository = new UsuarioRepository();
        GrupoRepository grupoRepository = new GrupoRepository();

        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        GrupoService grupoService = new GrupoService(grupoRepository);

        DespesaRepository despesaRepository = new DespesaRepository();

        DespesaService despesaService = new DespesaService(grupoRepository, despesaRepository, usuarioRepository);
        SaldoService saldoService = new SaldoService(grupoRepository, despesaRepository);

        UsuarioController usuarioController = new UsuarioController(usuarioService);
        GrupoController grupoController = new GrupoController(grupoService, usuarioService);
        DespesaController despesaController = new DespesaController(despesaService, saldoService);

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });
            config.jsonMapper(new JavalinJackson(mapper, false));
        });

        usuarioController.registrarRotas(app);
        grupoController.registarRotas(app);
        despesaController.registrarRotas(app);

        app.start(port);
    }
}
