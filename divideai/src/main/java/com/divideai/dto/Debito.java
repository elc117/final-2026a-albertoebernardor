package com.divideai.dto;
import java.math.BigDecimal;

public record Debito(
    Long devedorId,
    String DevedorNome,
    Long credorId,
    String credorNome,
    BigDecimal valor
){}