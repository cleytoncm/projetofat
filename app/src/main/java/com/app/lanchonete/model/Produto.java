package com.app.lanchonete.model;

public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private String imagemUrl;

    public Produto(int id, String nome, String descricao, double preco, String imagemUrl) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagemUrl = imagemUrl;
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
}
