package com.app.lanchonete.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.R;
import com.app.lanchonete.model.ItemCarrinho;
import com.app.lanchonete.model.Pedido;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder> {

    private List<Pedido> pedidos = new ArrayList<>();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));

    {
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

    public void setPedidos(List<Pedido> novosPedidos) {
        this.pedidos = novosPedidos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.textViewOrderId.setText(pedido.getId().substring(0, 8));
        holder.textViewOrderDate.setText(dateFormat.format(pedido.getDataDoPedido()));
        holder.textViewOrderTotal.setText(currencyFormat.format(pedido.getTotal()));
        holder.textViewOrderItems.setText(formatItems(pedido.getItens()));
    }

    private String formatItems(List<ItemCarrinho> itens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itens.size(); i++) {
            ItemCarrinho item = itens.get(i);
            sb.append(item.getQuantidade())
                    .append("x ")
                    .append(item.getProduto().getNome());
            if (i < itens.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewOrderDate, textViewOrderItems, textViewOrderTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.text_view_order_id);
            textViewOrderDate = itemView.findViewById(R.id.text_view_order_date);
            textViewOrderItems = itemView.findViewById(R.id.text_view_order_items);
            textViewOrderTotal = itemView.findViewById(R.id.text_view_order_total);
        }
    }
}
