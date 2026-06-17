package com.divideai.dto;
import java.time.LocalDate;
import java.util.List;

public record GrupoResponse(Long id, String nome, String descricao,
    LocalDate dataCriacao, List<UsuarioResponse> participantes, String codigoPublico){}
