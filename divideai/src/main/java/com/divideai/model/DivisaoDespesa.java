package com.divideai.model;
import java.math.BigDecimal;

public class DivisaoDespesa {
    private Long id;
    private Long despesaId;
    private Long usuarioId;
    private BigDecimal valorDevido;
    private boolean quitada;
    
    // construtores

    public DivisaoDespesa(Long id, Long despesaId, Long usuarioId, BigDecimal valorDevido){
        this.id = id;
        this.despesaId = despesaId;
        this.usuarioId = usuarioId;
        this.valorDevido = valorDevido;
        this.quitada = false;
    }

    public DivisaoDespesa(Long id, Long despesaId, Long usuarioId, BigDecimal valorDevido, boolean quitada){
        this.id = id;
        this.despesaId = despesaId;
        this.usuarioId = usuarioId;
        this.valorDevido = valorDevido;
        this.quitada = quitada;
    }

    public DivisaoDespesa(Long usuarioId, BigDecimal valorDevido, boolean quitada){
        this.usuarioId = usuarioId;
        this.valorDevido = valorDevido;
        this.quitada = quitada;
    }

    // getters

    public Long getId(){
        return this.id;
    }

    public Long getDespesaId(){
        return this.despesaId;
    }

    public Long getUsuarioId(){
        return this.usuarioId;
    }

    public BigDecimal getValorDevido(){
        return this.valorDevido;
    }

    public boolean getQuitada(){
        return this.quitada;
    }

    // setters

    public void setId(Long id){
        this.id = id;
    }

    public void setDespesaId(Long despesaId){
        this.despesaId = despesaId;
    }

    public void setUsuarioId(Long usuarioId){
        this.usuarioId = usuarioId;
    }

    public void setValorDevido(BigDecimal valorDevido){
        this.valorDevido = valorDevido;
    }

    public void setQuitada(boolean quitada){
        this.quitada = quitada;
    }
}