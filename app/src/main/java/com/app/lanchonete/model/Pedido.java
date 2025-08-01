package com.app.lanchonete.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.Date;

public class Pedido implements Serializable {

    private String id;
    private List<ItemCarrinho> itens;
    private double total;
    private String metodoPagamento;
    private Date dataDoPedido;

    public Pedido() {
    }

    public Pedido(List<ItemCarrinho> itens, double total, String metodoPagamento) {
        this.id = UUID.randomUUID().toString();
        this.itens = itens;
        this.total = total;
        this.metodoPagamento = metodoPagamento;
        this.dataDoPedido = new Date();
    }

    public String getId() {
        return id;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public double getTotal() {
        return total;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public Date getDataDoPedido() {
        return dataDoPedido;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public void setDataDoPedido(Date dataDoPedido) {
        this.dataDoPedido = dataDoPedido;
    }
}
