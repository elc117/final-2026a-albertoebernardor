import java.math.BigDecimal;
import java.time.LocalDate;


//falta ver a questão dos tads

public class Despesa {
    private Long id;
    private Long grupoId;
    private Long pagadorId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private Categoria categoria;
    private TipoDivisao tipoDivisao;

    public Despesa(){}

    //Cria sem id.
    public Despesa(Long grupoId, Long pagadorId, String descricao, BigDecimal valor, LocalDate data, Categoria categoria, TipoDivisao tipoDivisao){
        this.grupoId = grupoId;
        this.pagadorId = pagadorId;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.tipoDivisao = tipoDivisao;
    }

    public Despesa(Long id, Long grupoId, Long pagadorId, String descricao, BigDecimal valor, LocalDate data, Categoria categoria, TipoDivisao tipoDivisao){
        this.id = id;
        this.grupoId = grupoId;
        this.pagadorId = pagadorId;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.tipoDivisao = tipoDivisao;
    }


    //setters & getters

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setGrupoId(Long grupoId){
        this.grupoId = grupoId;
    }

    public Long getGrupoId(){
        return grupoId;
    }

    public void setPagadorId(Long pagadorId){
        this.pagadorId = pagadorId;
    }

    public Long getPagadorId(){
        return pagadorId;
    }   

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setValor(BigDecimal valor){
        this.valor = valor;
    }

    public BigDecimal getValor(){
        return valor;
    }

    public void setData(LocalDate data){
        this.data = data;
    }

    public LocalDate getData(){
        return data;
    }

    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    public Categoria getCategoria(){
        return categoria;
    }

    public void setTipoDivisao(TipoDivisao tipoDivisao){
        this.tipoDivisao = tipoDivisao;
    }

    public TipoDivisao getTipoDivisao(){
        return tipoDivisao;
    }
    
}

