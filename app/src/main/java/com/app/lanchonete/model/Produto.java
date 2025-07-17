package com.app.lanchonete.model;

public class Produto {
    private String nome;
    private String descricao;
    private double preco;
    private int imagemUrl;

    public Produto(String nome, String descricao, double preco, int imagemUrl) {
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

    public int getImagemUrl() {
        return imagemUrl;
    }
}
