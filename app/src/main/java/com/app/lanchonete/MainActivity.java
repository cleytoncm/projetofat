package com.app.lanchonete;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.adapter.ProdutoAdapter;
import com.app.lanchonete.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listagemProdutoDestaque;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("lanchonete.autenticacao", MODE_PRIVATE);
        boolean estaLogado = preferences.getBoolean("estaLogado", false);

        if (!estaLogado) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        listagemProdutoDestaque = findViewById(R.id.recycler_view_featured_products);
        listagemProdutoDestaque.setLayoutManager(new LinearLayoutManager(this));

        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("Produto 1", "Descrição do Produto 1", 10.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 2", "Descrição do Produto 2", 15.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 3", "Descrição do Produto 3", 8.99, R.drawable.product_image_placeholder));

        ProdutoAdapter adapter = new ProdutoAdapter(produtos);
        listagemProdutoDestaque.setAdapter(adapter);
    }

    public void irCatalogo(View view) {
        Intent intent = new Intent(MainActivity.this, CatologActivity.class);
        startActivity(intent);
    }

    public void sair(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estaLogado", false);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}