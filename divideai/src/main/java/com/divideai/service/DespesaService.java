package com.divideai.service;

import com.divideai.repository.DespesaRepository;
import com.divideai.repository.GrupoRepository;

public class DespesaService {
    private final GrupoRepository grupoRepository;
    private final DespesaRepository despesaRepository;

    public DespesaService(GrupoRepository grupoRepository, DespesaRepository despesaRepository) {
        this.grupoRepository = grupoRepository;
        this.despesaRepository = despesaRepository;
    }
}
