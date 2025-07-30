package com.app.lanchonete;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.lanchonete.utils.Constants;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView textViewDetailName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        textViewDetailName = findViewById(R.id.text_view_detail_name);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Constants.INTENT_PRODUTO_ID)) {
            String productName = intent.getStringExtra(Constants.INTENT_PRODUTO_ID);
            Log.d("ProductDetailActivity", "Nome do Produto: " + productName);
            textViewDetailName.setText(productName);
        }

    }


}