package com.app.lanchonete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.adapter.CarrinhoAdapter;
import com.app.lanchonete.data.CarrinhoSingleton;
import com.app.lanchonete.model.ItemCarrinho;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCartItems;
    private TextView textViewSubtotalValue;
    private Button buttonContinueToCheckout;
    private CarrinhoAdapter carrinhoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCartItems = findViewById(R.id.recycler_view_cart_items);
        textViewSubtotalValue = findViewById(R.id.text_view_subtotal_value);
        buttonContinueToCheckout = findViewById(R.id.button_continue_to_checkout);

        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        setupListeners();
        updateCartDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartDisplay();
    }

    private void setupListeners() {
        buttonContinueToCheckout.setOnClickListener(v -> {
            if (CarrinhoSingleton.getInstance(this).getItens().isEmpty()) {
                Toast.makeText(this, "Seu carrinho está vazio!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Prosseguindo para o checkout!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateCartDisplay() {
        List<ItemCarrinho> itensCarrinho = CarrinhoSingleton.getInstance(this).getItens();

        if (itensCarrinho.isEmpty()) {
            recyclerViewCartItems.setVisibility(View.GONE);
            textViewSubtotalValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(0.0));
            buttonContinueToCheckout.setEnabled(false);
        } else {
            recyclerViewCartItems.setVisibility(View.VISIBLE);
            buttonContinueToCheckout.setEnabled(true);

            if (carrinhoAdapter == null) {
                carrinhoAdapter = new CarrinhoAdapter(itensCarrinho, this, () -> updateCartDisplay());
                recyclerViewCartItems.setAdapter(carrinhoAdapter);
            } else {
                carrinhoAdapter.atualizarItens(itensCarrinho);
            }

            double total = CarrinhoSingleton.getInstance(this).calcularTotal();
            textViewSubtotalValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(total));
        }
    }
}
