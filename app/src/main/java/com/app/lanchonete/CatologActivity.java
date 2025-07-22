package com.app.lanchonete;

import android.os.Bundle;

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

public class CatologActivity extends AppCompatActivity {

    private RecyclerView listagemProdutoDestaque;

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


        listagemProdutoDestaque = findViewById(R.id.recycler_view_all_products);
        listagemProdutoDestaque.setLayoutManager(new LinearLayoutManager(this));

        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("Produto 1", "Descrição do Produto 1", 10.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 2", "Descrição do Produto 2", 15.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 3", "Descrição do Produto 3", 8.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 1", "Descrição do Produto 1", 10.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 2", "Descrição do Produto 2", 15.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 3", "Descrição do Produto 3", 8.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 1", "Descrição do Produto 1", 10.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 2", "Descrição do Produto 2", 15.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 3", "Descrição do Produto 3", 8.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 1", "Descrição do Produto 1", 10.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 2", "Descrição do Produto 2", 15.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 3", "Descrição do Produto 3", 8.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 1", "Descrição do Produto 1", 10.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 2", "Descrição do Produto 2", 15.99, R.drawable.product_image_placeholder));
        produtos.add(new Produto("Produto 3", "Descrição do Produto 3", 8.99, R.drawable.product_image_placeholder));

        ProdutoAdapter adapter = new ProdutoAdapter(produtos);
        listagemProdutoDestaque.setAdapter(adapter);
    }
}