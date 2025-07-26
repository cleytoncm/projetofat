package com.app.lanchonete;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
import java.util.stream.Stream;

public class CatologActivity extends AppCompatActivity {

    private RecyclerView listagemProdutoDestaque;
    private List<Produto> produtosList;
    private EditText editTextBusca;
    ProdutoAdapter produtoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catolog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                    List<Produto> produtosFiltrados = filtrarProdutos(palavraBusca);
                    produtoAdapter = new ProdutoAdapter(produtosFiltrados);
                    listagemProdutoDestaque.setAdapter(produtoAdapter);
                }

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

    private List<Produto> filtrarProdutos(String palavraBusca) {
        String termoBusca = palavraBusca.toLowerCase();
        return  produtosList.stream()
                .filter(produto -> produto.getNome().toLowerCase().contains(termoBusca))
                .collect(Collectors.toList());

    }
}