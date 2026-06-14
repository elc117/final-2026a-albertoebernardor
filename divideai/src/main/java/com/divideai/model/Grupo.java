package com.divideai.model;

import java.time.LocalDate;

public class Grupo {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataCriacao;

    public Grupo(){}

    //construtor de criacao, ele ainda não possui id pois quem gera é o banco.      
    public Grupo(String nome, String descricao, LocalDate dataCriacao){
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
    }

    //Grupo Existente
    public Grupo(Long id, String nome, String descricao, LocalDate dataCriacao){
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao; 
    }
    
    //setters & getters

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return nome;
    }
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
    public void setDataCriacao(LocalDate dataCriacao){
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataCriacao(){
        return dataCriacao;
    }


}


