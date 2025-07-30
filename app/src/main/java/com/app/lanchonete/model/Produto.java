package com.app.lanchonete.model;

public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private String imagemUrl;
    private String categoria;

    public Produto(int id, String nome, String descricao, double preco, String imagemUrl, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagemUrl = imagemUrl;
        this.id = id;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getId() {
        return id;
    }
}
