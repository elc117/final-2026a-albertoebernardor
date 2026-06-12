package com.divideai.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ResumoGrupo(
    Long grupoId, String nomeGrupo, BigDecimal totalGasto,
    Map<String, BigDecimal> saldoPorUsuario,
    List<Debito> debitosPendentes){}