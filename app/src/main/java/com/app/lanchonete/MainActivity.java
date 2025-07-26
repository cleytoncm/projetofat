package com.app.lanchonete;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.adapter.ProdutoAdapter;
import com.app.lanchonete.data.remote.ProdutoApiClient;
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

        obterProdutosDestaques();
    }

    private void obterProdutosDestaques() {
        ProdutoApiClient.getInstance(this).obterProdutos(new ProdutoApiClient.ProdutoCallback() {
            @Override
            public void onSuccess(List<Produto> produtos) {
                ProdutoAdapter adapter = new ProdutoAdapter(produtos);
                listagemProdutoDestaque.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Erro ao listar produtos: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void irCatalogo(View view) {
        Intent intent = new Intent(MainActivity.this, CatologActivity.class);
        startActivity(intent);
    }

}