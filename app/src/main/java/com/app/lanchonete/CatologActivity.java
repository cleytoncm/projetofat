package com.app.lanchonete;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.List;
import java.util.stream.Collectors;

public class CatologActivity extends AppCompatActivity {

    private RecyclerView listagemProdutoDestaque;
    private List<Produto> produtosList;
    private EditText editTextBusca;
    ProdutoAdapter produtoAdapter;
    private LinearLayout linearButtonLanches;
    private LinearLayout linearButtonPorcoes;
    private LinearLayout linearButtonBebidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catolog);

        editTextBusca = findViewById(R.id.edit_text_search);
        listagemProdutoDestaque = findViewById(R.id.recycler_view_all_products);
        listagemProdutoDestaque.setLayoutManager(new LinearLayoutManager(this));

        obterProdutos();

        editTextBusca.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String palavraBusca = s.toString();

                if (palavraBusca.isBlank()) {
                    produtoAdapter = new ProdutoAdapter(produtosList);
                    listagemProdutoDestaque.setAdapter(produtoAdapter);
                    return;
                }

                if (palavraBusca.length() >= 3) {
                    List<Produto> produtosFiltrados = filtrarProdutosNome(palavraBusca);
                    produtoAdapter = new ProdutoAdapter(produtosFiltrados);
                    listagemProdutoDestaque.setAdapter(produtoAdapter);
                }

            }
        });

        linearButtonLanches = findViewById(R.id.category_lanches);
        linearButtonLanches.setOnClickListener(v -> {
            List<Produto> produtosFiltrados = filtrarProdutosCategoria("L");
            produtoAdapter = new ProdutoAdapter(produtosFiltrados);
            listagemProdutoDestaque.setAdapter(produtoAdapter);
        });

        linearButtonPorcoes = findViewById(R.id.category_porcoes);
        linearButtonPorcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Produto> produtosFiltrados = filtrarProdutosCategoria("P");
                produtoAdapter = new ProdutoAdapter(produtosFiltrados);
                listagemProdutoDestaque.setAdapter(produtoAdapter);
            }
        });

        linearButtonBebidas = findViewById(R.id.category_bebidas);
        linearButtonBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Produto> produtosFiltrados = filtrarProdutosCategoria("B");
                produtoAdapter = new ProdutoAdapter(produtosFiltrados);
                listagemProdutoDestaque.setAdapter(produtoAdapter);
            }
        });
    }

    private void obterProdutos() {
        ProdutoApiClient.getInstance(this).obterProdutos(new ProdutoApiClient.ProdutoCallback() {
            @Override
            public void onSuccess(List<Produto> produtos) {
                produtosList = produtos;
                produtoAdapter = new ProdutoAdapter(produtosList);
                listagemProdutoDestaque.setAdapter(produtoAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(CatologActivity.this, "Erro ao listar produtos: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Produto> filtrarProdutosNome(String palavraBusca) {
        String termoBusca = palavraBusca.toLowerCase();
        return  produtosList.stream()
                .filter(produto -> produto.getNome().toLowerCase().contains(termoBusca))
                .collect(Collectors.toList());

    }

    private List<Produto> filtrarProdutosCategoria(String categoria) {
        return produtosList.stream()
                .filter(produto -> produto.getCategoria().equals(categoria))
                .collect(Collectors.toList());
    }

}