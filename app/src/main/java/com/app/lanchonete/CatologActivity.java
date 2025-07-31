package com.app.lanchonete;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.adapter.ProdutoAdapter;
import com.app.lanchonete.data.remote.ProdutoApiClient;
import com.app.lanchonete.model.Produto;
import com.app.lanchonete.utils.Constants;

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
    private LinearLayout linearButtonTodos;
    private TextView textViewTitleListProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catolog);

        editTextBusca = findViewById(R.id.edit_text_search);
        listagemProdutoDestaque = findViewById(R.id.recycler_view_all_products);
        listagemProdutoDestaque.setLayoutManager(new LinearLayoutManager(this));
        textViewTitleListProducts = findViewById(R.id.text_view_title_list_products);

        obterProdutos();
        setupSearchListener();
        setupCategoryListeners();
    }

    private void setupSearchListener() {
        editTextBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String palavraBusca = s.toString().trim();

                if (palavraBusca.isEmpty()) {
                    produtoAdapter = new ProdutoAdapter(produtosList, CatologActivity.this);
                    listagemProdutoDestaque.setAdapter(produtoAdapter);
                    textViewTitleListProducts.setText("Todos os Produtos");
                } else if (palavraBusca.length() >= 3) {
                    List<Produto> produtosFiltrados = filtrarProdutosNome(palavraBusca);
                    produtoAdapter = new ProdutoAdapter(produtosFiltrados, CatologActivity.this);
                    listagemProdutoDestaque.setAdapter(produtoAdapter);
                    textViewTitleListProducts.setText("Resultados da Busca");
                }
            }
        });
    }

    private void setupCategoryListeners() {
        linearButtonLanches = findViewById(R.id.category_lanches);
        linearButtonLanches.setOnClickListener(v -> {
            filtrarEExibirProdutos("Lanches", "L");
        });

        linearButtonPorcoes = findViewById(R.id.category_porcoes);
        linearButtonPorcoes.setOnClickListener(v -> {
            filtrarEExibirProdutos("Porções", "P");
        });

        linearButtonBebidas = findViewById(R.id.category_bebidas);
        linearButtonBebidas.setOnClickListener(v -> {
            filtrarEExibirProdutos("Bebidas", "B");
        });

        linearButtonTodos = findViewById(R.id.category_all);
        linearButtonTodos.setOnClickListener(v -> {
            produtoAdapter = new ProdutoAdapter(produtosList, CatologActivity.this);
            listagemProdutoDestaque.setAdapter(produtoAdapter);
            textViewTitleListProducts.setText("Todos os Produtos");
        });
    }

    private void filtrarEExibirProdutos(String titulo, String categoriaCodigo) {
        List<Produto> produtosFiltrados = filtrarProdutosCategoria(categoriaCodigo);
        produtoAdapter = new ProdutoAdapter(produtosFiltrados, CatologActivity.this);
        listagemProdutoDestaque.setAdapter(produtoAdapter);
        textViewTitleListProducts.setText(titulo);
    }

    private void obterProdutos() {
        ProdutoApiClient.getInstance(this).obterProdutos(new ProdutoApiClient.ProdutoCallback() {
            @Override
            public void onSuccess(List<Produto> produtos) {
                produtosList = produtos;
                List<Produto> produtosParaExibir = produtos;

                Intent intent = getIntent();
                if (intent != null) {
                    String categoriaPassada = intent.getStringExtra(Constants.INTENT_CATEGORIA);
                    if (categoriaPassada != null && !categoriaPassada.isEmpty()) {
                        String categoriaCodigo = "";
                        String tituloDisplay = "";

                        switch (categoriaPassada) {
                            case "Lanches":
                                categoriaCodigo = "L";
                                tituloDisplay = "Lanches";
                                break;
                            case "Porções":
                                categoriaCodigo = "P";
                                tituloDisplay = "Porções";
                                break;
                            case "Bebidas":
                                categoriaCodigo = "B";
                                tituloDisplay = "Bebidas";
                                break;
                            case "Todos":
                            default:
                                categoriaCodigo = "";
                                tituloDisplay = "Todos os Produtos";
                                break;
                        }

                        if (!categoriaCodigo.isEmpty()) {
                            produtosParaExibir = filtrarProdutosCategoria(categoriaCodigo);
                        }
                        textViewTitleListProducts.setText(tituloDisplay);
                    } else {
                        textViewTitleListProducts.setText("Todos os Produtos");
                    }
                } else {
                    textViewTitleListProducts.setText("Todos os Produtos");
                }

                produtoAdapter = new ProdutoAdapter(produtosParaExibir, CatologActivity.this);
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
        return produtosList.stream()
                .filter(produto -> produto.getNome().toLowerCase().contains(termoBusca))
                .collect(Collectors.toList());
    }

    private List<Produto> filtrarProdutosCategoria(String categoria) {
        return produtosList.stream()
                .filter(produto -> produto.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }
}