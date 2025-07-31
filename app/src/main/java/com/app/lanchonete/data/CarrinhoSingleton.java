package com.app.lanchonete.data;

import android.content.Context;
import android.widget.Toast;

import com.app.lanchonete.model.ItemCarrinho;
import com.app.lanchonete.model.Produto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarrinhoSingleton {
    private static CarrinhoSingleton instance;
    private List<ItemCarrinho> itens;
    private List<OnCartChangeListener> listeners;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private CarrinhoSingleton() {
        itens = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public static synchronized CarrinhoSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new CarrinhoSingleton();
        }
        return instance;
    }

    public void registerListener(OnCartChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(OnCartChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners() {
        for (OnCartChangeListener listener : listeners) {
            listener.onCartChanged();
        }
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().equals(produto)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                notifyListeners();
                return;
            }
        }
        itens.add(new ItemCarrinho(produto, quantidade));
        notifyListeners();
    }

    public void adicionarProduto(Produto produto) {
        adicionarProduto(produto, 1);
    }

    public void removerQuantidade(Produto produto, int quantidade) {
        Iterator<ItemCarrinho> iterator = itens.iterator();
        while (iterator.hasNext()) {
            ItemCarrinho item = iterator.next();
            if (item.getProduto().equals(produto)) {
                item.setQuantidade(item.getQuantidade() - quantidade);
                if (item.getQuantidade() <= 0) {
                    iterator.remove();
                }
                notifyListeners();
                return;
            }
        }
    }

    public void removerProduto(Produto produto) {
        Iterator<ItemCarrinho> iterator = itens.iterator();
        while (iterator.hasNext()) {
            ItemCarrinho item = iterator.next();
            if (item.getProduto().equals(produto)) {
                iterator.remove();
                notifyListeners();
                return;
            }
        }
    }

    public void limparCarrinho() {
        itens.clear();
        notifyListeners();
    }

    public List<ItemCarrinho> getItens() {
        return new ArrayList<>(itens);
    }

    public double calcularTotal() {
        double total = 0.0;
        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }
}
