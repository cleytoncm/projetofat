package com.app.lanchonete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.R;
import com.app.lanchonete.data.CarrinhoSingleton;
import com.app.lanchonete.model.ItemCarrinho;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolder> {

    private List<ItemCarrinho> itensCarrinho;
    private Context context;
    private OnItemChangeListener itemChangeListener;

    public interface OnItemChangeListener {
        void onItemQuantityChanged();
    }

    public CarrinhoAdapter(List<ItemCarrinho> itensCarrinho, Context context, OnItemChangeListener listener) {
        this.itensCarrinho = itensCarrinho;
        this.context = context;
        this.itemChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCarrinho item = itensCarrinho.get(position);

        holder.textViewProductName.setText(item.getProduto().getNome());
        holder.textViewQuantity.setText(String.valueOf(item.getQuantidade()));

        NumberFormat precoBr = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.textViewProductPrice.setText(precoBr.format(item.getProduto().getPreco()));

        holder.textViewProductSubtotal.setText(precoBr.format(item.getSubtotal()));

        Glide.with(context)
                .load(item.getProduto().getImagemUrl())
                .placeholder(R.drawable.product_image_placeholder)
                .error(R.drawable.product_image_placeholder)
                .into(holder.imageViewProduct);

        holder.buttonIncreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantidade() < 10) {
                CarrinhoSingleton.getInstance(context).adicionarProduto(item.getProduto());
                itemChangeListener.onItemQuantityChanged();
            } else {
                Toast.makeText(context, "Quantidade máxima atingida para " + item.getProduto().getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDecreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantidade() > 1) {
                CarrinhoSingleton.getInstance(context).removerQuantidade(item.getProduto(), 1);
                itemChangeListener.onItemQuantityChanged();
            } else {
                Toast.makeText(context, "Quantidade mínima atingida para " + item.getProduto().getNome() + ". Use o botão de remover para excluí-lo.", Toast.LENGTH_LONG).show();
            }
        });

        holder.buttonRemoveItem.setOnClickListener(v -> {
            CarrinhoSingleton.getInstance(context).removerProduto(item.getProduto());
            itemChangeListener.onItemQuantityChanged();
        });
    }

    @Override
    public int getItemCount() {
        return itensCarrinho.size();
    }

    public void atualizarItens(List<ItemCarrinho> novosItens) {
        this.itensCarrinho = novosItens;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewQuantity;
        TextView textViewProductSubtotal;
        Button buttonDecreaseQuantity;
        Button buttonIncreaseQuantity;
        ImageButton buttonRemoveItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.image_view_cart_product);
            textViewProductName = itemView.findViewById(R.id.text_view_cart_product_name);
            textViewProductPrice = itemView.findViewById(R.id.text_view_cart_product_price);
            textViewQuantity = itemView.findViewById(R.id.text_view_cart_quantity);
            textViewProductSubtotal = itemView.findViewById(R.id.text_view_cart_subtotal);
            buttonDecreaseQuantity = itemView.findViewById(R.id.button_decrease_cart_quantity);
            buttonIncreaseQuantity = itemView.findViewById(R.id.button_increase_cart_quantity);
            buttonRemoveItem = itemView.findViewById(R.id.button_remove_item);
        }
    }
}