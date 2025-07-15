package com.app.lanchonete;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FormularioActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etSenha;
    private Button btnLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogar = findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();

                if (email.isEmpty()) {
                    etEmail.setError("Preencha o email");
                    etEmail.requestFocus();
                }

                if (senha.isEmpty()) {
                    etSenha.setError("Preencha a senha");
                    etSenha.requestFocus();
                }


                Toast.makeText(FormularioActivity.this, "Logando...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}