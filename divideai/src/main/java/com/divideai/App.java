package com.divideai;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class App {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        Handler teste = ctx -> ctx.result("ok");

        Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });
        })
        .get("/teste", teste)
        .start(port);
    }
}
