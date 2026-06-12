package com.divideai.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record DespesaRequest(
    Long pagadorId, String descricao, BigDecimal valor,
    LocalDate data, String categoria, String tipoDivisao,
    Map<Long, BigDecimal> divisoes){}

