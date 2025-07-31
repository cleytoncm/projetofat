package com.app.lanchonete;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.lanchonete.data.CarrinhoSingleton;
import com.app.lanchonete.data.remote.ProdutoApiClient;
import com.app.lanchonete.model.Produto;
import com.app.lanchonete.utils.Constants;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView textViewDetailName;
    private TextView textViewDetailDescription;
    private TextView textViewDetailPrice;
    private TextView textViewDetailCategory;
    private ImageView imageViewDetailProduct;
    private Button buttonIncreaseQuantity;
    private Button buttonDecreaseQuantity;
    private TextView textViewQuantity;
    private Button buttonAddToCart;
    private int quantity = 1;
    private Produto currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        textViewDetailName = findViewById(R.id.text_view_detail_name);
        textViewDetailDescription = findViewById(R.id.text_view_detail_description);
        textViewDetailPrice = findViewById(R.id.text_view_detail_price);
        textViewDetailCategory = findViewById(R.id.text_view_detail_category);
        imageViewDetailProduct = findViewById(R.id.image_view_detail_product);
        buttonIncreaseQuantity = findViewById(R.id.button_increase_quantity);
        buttonDecreaseQuantity = findViewById(R.id.button_decrease_quantity);
        textViewQuantity = findViewById(R.id.text_view_quantity);
        buttonAddToCart = findViewById(R.id.button_add_to_cart);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Constants.INTENT_PRODUTO_ID)) {
            String produtoIdString = intent.getStringExtra(Constants.INTENT_PRODUTO_ID);
            try {
                int produtoId = Integer.parseInt(produtoIdString);
                obterDetalhesProduto(produtoId);
            } catch (NumberFormatException e) {
                Log.e("ProductDetailActivity", "ID do produto inválido: " + produtoIdString);
                Toast.makeText(this, "Erro: ID do produto inválido.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Erro: Produto não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textViewQuantity.setText(String.valueOf(quantity));
        initListeners();
    }

    private void initListeners() {
        buttonIncreaseQuantity.setOnClickListener(v -> {
            if (quantity < 10) {
                quantity++;
                textViewQuantity.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, "Quantidade máxima atingida", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDecreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                textViewQuantity.setText(String.valueOf(quantity));
            }
        });

        buttonAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                CarrinhoSingleton.getInstance(this).adicionarProduto(currentProduct, quantity);
                Toast.makeText(this, currentProduct.getNome() + " (" + quantity + ") adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro: Produto não disponível para adicionar ao carrinho.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obterDetalhesProduto(int produtoId) {
        ProdutoApiClient.getInstance(this).obterProdutoPorId(produtoId, new ProdutoApiClient.ProdutoPorIdCallback() {
            @Override
            public void onSuccess(Produto produto) {
                currentProduct = produto;
                textViewDetailName.setText(produto.getNome());
                textViewDetailDescription.setText(produto.getDescricao());

                NumberFormat precoBr = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                textViewDetailPrice.setText(precoBr.format(produto.getPreco()));

                String categoriaDisplay;
                if (produto.getCategoria().equalsIgnoreCase("L")) {
                    categoriaDisplay = "Lanche";
                } else if (produto.getCategoria().equalsIgnoreCase("B")) {
                    categoriaDisplay = "Bebida";
                } else if (produto.getCategoria().equalsIgnoreCase("P")) {
                    categoriaDisplay = "Porção";
                } else {
                    categoriaDisplay = produto.getCategoria();
                }
                textViewDetailCategory.setText(categoriaDisplay);

                Glide.with(ProductDetailActivity.this)
                        .load(produto.getImagemUrl())
                        .placeholder(R.drawable.product_image_placeholder)
                        .error(R.drawable.product_image_placeholder)
                        .into(imageViewDetailProduct);
            }
            @Override
            public void onError(String errorMessage) {
                Log.e("ProductDetailActivity", "Erro ao obter detalhes do produto: " + errorMessage);
                Toast.makeText(ProductDetailActivity.this, "Erro ao obter detalhes do produto: " + errorMessage, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}