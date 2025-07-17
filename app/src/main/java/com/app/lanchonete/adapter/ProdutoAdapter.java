package com.app.lanchonete.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.R;
import com.app.lanchonete.model.Produto;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdutoAdapter  extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
    private List<Produto> produtos;

    public ProdutoAdapter(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public class ProdutoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagemProduto;
        public TextView nomeProduto;
        public TextView descricaoProduto;
        public TextView precoProduto;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            imagemProduto = itemView.findViewById(R.id.product_image);
            nomeProduto = itemView.findViewById(R.id.product_name);
            descricaoProduto = itemView.findViewById(R.id.product_description_short);
            precoProduto = itemView.findViewById(R.id.product_price);

        }
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_card, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = produtos.get(position);

        holder.imagemProduto.setImageResource(produto.getImagemUrl());
        holder.nomeProduto.setText(produto.getNome());
        holder.descricaoProduto.setText(produto.getDescricao());

        NumberFormat precoBr = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.precoProduto.setText(precoBr.format(produto.getPreco()));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}
