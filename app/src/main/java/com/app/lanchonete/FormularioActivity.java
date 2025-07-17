package com.app.lanchonete;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView tvMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogar = findViewById(R.id.btnLogar);
        tvMensagem = findViewById(R.id.tvMensagem);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String senha = etSenha.getText().toString().trim();
                View errorFocusView = null;
                boolean withError = false;

                if (email.isEmpty()) {
                    etEmail.setError("Preencha o e-mail");
                    if (errorFocusView == null) {
                        errorFocusView = etEmail;
                    }
                    withError = true;
                } else if (email.length() < 5) {
                    etEmail.setError("O e-mail precisa conter no mínimo 5 caracteres");
                    if (errorFocusView == null) {
                        errorFocusView = etEmail;
                    }
                    withError = true;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (errorFocusView == null) {
                        etEmail.setError("Informe um e-mail válido");
                        errorFocusView = etEmail;
                    }
                    withError = true;
                }

                if (senha.isEmpty()) {
                    etSenha.setError("Preencha a senha");
                    if (errorFocusView == null) {
                        errorFocusView = etSenha;
                    }
                    withError = true;
                }

                if (etSenha.length() < 4) {
                    etSenha.setError("A senha precisa conter no mínimo 4 caracteres");
                    if (errorFocusView == null) {
                        errorFocusView = etSenha;
                    }
                    withError = true;
                }

                if (withError) {
                    errorFocusView.requestFocus();
                    Toast.makeText(FormularioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FormularioActivity.this, "Logado com sucesso...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormularioActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}