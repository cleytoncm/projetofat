package com.app.lanchonete;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.adapter.ProdutoAdapter;
import com.app.lanchonete.data.remote.ProdutoApiClient;
import com.app.lanchonete.model.Produto;
import com.app.lanchonete.utils.Constants;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listagemProdutoDestaque;
    SharedPreferences preferences;
    LinearLayout linearButtonLanches;
    LinearLayout linearButtonPorcoes;
    LinearLayout linearButtonBebidas;

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

        linearButtonLanches = findViewById(R.id.category_lanches);
        linearButtonLanches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irCatalogo(v, "L");
            }
        });

        linearButtonPorcoes = findViewById(R.id.category_porcoes);
        linearButtonPorcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irCatalogo(v, "P");
            }
        });

        linearButtonBebidas = findViewById(R.id.category_bebidas);
        linearButtonBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irCatalogo(v, "B");
            }
        });

    }

    private void obterProdutosDestaques() {
        ProdutoApiClient.getInstance(this).obterProdutos(new ProdutoApiClient.ProdutoCallback() {
            @Override
            public void onSuccess(List<Produto> produtos) {
                ProdutoAdapter adapter = new ProdutoAdapter(produtos, MainActivity.this);
                listagemProdutoDestaque.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Erro ao listar produtos: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void irCatalogo(View view, String categoria) {
        Intent intent = new Intent(MainActivity.this, CatologActivity.class);
        intent.putExtra(Constants.INTENT_CATEGORIA, categoria);
        startActivity(intent);
    }

}