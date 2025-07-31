package com.app.lanchonete.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private List<Object> endereco;
    private List<Object> pedidos;

    public Usuario(String nome, String email, String telefone, String senha) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.endereco = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public Usuario(String id, String nome, String email, String telefone, String senha) {
        this(nome, email, telefone, senha);
        this.id = id;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getSenha() { return senha; }
    public List<Object> getEndereco() { return endereco; }
    public List<Object> getPedidos() { return pedidos; }

    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setEndereco(List<Object> endereco) { this.endereco = endereco; }
    public void setPedidos(List<Object> pedidos) { this.pedidos = pedidos; }
}