public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senhaHash;
    //constutores:

    public Usuario(){};

    //construtor de criacao, ele ainda não possui id pois quem gera é o banco.
    public Usuario(String nome, String email, String senhaHash){
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }
    //Usuario Existente
    public Usuario(Long id, String nome, String email, String senhaHash){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    // setters & getters

    //usa pra retornar o valor do banco de volta pra estrutura java
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

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
  
    public void setSenhaHash(String senhaHash){
        this.senhaHash = senhaHash;
    }

    public String getSenhaHash(){
        return senhaHash;
    }

}


