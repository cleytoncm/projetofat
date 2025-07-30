package com.app.lanchonete.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.ProductDetailActivity;
import com.app.lanchonete.R;
import com.app.lanchonete.model.Produto;
import com.app.lanchonete.utils.Constants;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
    private List<Produto> produtos;
    private Context context;

    public ProdutoAdapter(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
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

        Glide.with(holder.itemView.getContext())
                .load(produto.getImagemUrl())
                .placeholder(R.drawable.product_image_placeholder)
                .into(holder.imagemProduto);

        holder.nomeProduto.setText(produto.getNome());
        holder.descricaoProduto.setText(produto.getDescricao());

        NumberFormat precoBr = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.precoProduto.setText(precoBr.format(produto.getPreco()));

        holder.nomeProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(Constants.INTENT_PRODUTO_ID, String.valueOf(produto.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}
