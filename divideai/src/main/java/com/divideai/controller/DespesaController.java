package com.divideai.controller;

import java.util.List;

import com.divideai.model.Despesa;
import com.divideai.service.DespesaService;
import com.divideai.service.SaldoService;
import com.divideai.dto.DespesaRequest;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class DespesaController {
    private final DespesaService despesaService;
    private final SaldoService saldoService;

    public DespesaController(DespesaService despesaService, SaldoService saldoService) {
        this.despesaService = despesaService;
        this.saldoService = saldoService;
    }

    public void registrarRotas(Javalin app) {
        app.post("/grupos/{id}/despesas", this::registrar);
        app.get("/grupos/{id}/despesas", this::historico);
        app.get("/grupos/{id}/saldos", this::saldos);
        app.get("/grupos/{id}/resumo", this::resumo);
    }

    private void registrar(Context ctx) {
        Long grupoId = Long.parseLong(ctx.pathParam("id"));
        DespesaRequest request = ctx.bodyAsClass(DespesaRequest.class);
        Despesa despesa = despesaService.registrar(grupoId, request);
        ctx.status(201).json(despesa);
    }
    private void historico(Context ctx) {
        Long grupoId = Long.parseLong(ctx.pathParam("id"));
        List<Despesa> despesas = despesaService.historico(grupoId);
        ctx.json(despesas);
    }
    private void saldos(Context ctx) {
        Long grupoId = Long.parseLong(ctx.pathParam("id"));
        ctx.json(saldoService.calcularSaldos(grupoId));
    }
    private void resumo(Context ctx) {
        Long grupoId = Long.parseLong(ctx.pathParam("id"));
        ctx.json(saldoService.montarResumo(grupoId));
    }
}
