package com.app.lanchonete;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.lanchonete.data.remote.UsuarioApiClient;
import com.app.lanchonete.model.Usuario;
import com.app.lanchonete.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etSenha;
    Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.edit_text_email);
        etSenha = findViewById(R.id.edit_text_password);
        btnEntrar = findViewById(R.id.button_login);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tentarLogin();
            }
        });
    }

    private void tentarLogin() {
        etEmail.setError(null);
        etSenha.setError(null);

        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        boolean cancelar = false;
        View focoView = null;

        if (TextUtils.isEmpty(senha)) {
            etSenha.setError("Campo obrigatório");
            focoView = etSenha;
            cancelar = true;
        } else if (senha.length() < 6) {
            etSenha.setError("A senha deve ter pelo menos 6 caracteres.");
            focoView = etSenha;
            cancelar = true;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Campo obrigatório");
            focoView = etEmail;
            cancelar = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("E-mail inválido");
            focoView = etEmail;
            cancelar = true;
        }

        if (cancelar) {
            focoView.requestFocus();
        } else {
            autenticarComApi(email, senha);
        }
    }

    private void autenticarComApi(String email, String senha) {
        Toast.makeText(LoginActivity.this, "Verificando credenciais...", Toast.LENGTH_SHORT).show();

        UsuarioApiClient.getInstance(this).autenticarUsuario(email, senha, new UsuarioApiClient.LoginCallback() {
            @Override
            public void onSuccess(Usuario usuarioLogado) {
                Toast.makeText(LoginActivity.this, "Login bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + "!", Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("estaLogado", true);
                editor.putString("emailUsuario", usuarioLogado.getEmail());
                editor.putString("nomeUsuario", usuarioLogado.getNome());
                editor.putString("idUsuario", usuarioLogado.getId());
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(LoginActivity.this, "Erro de comunicação: " + errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCredenciaisInvalidas() {
                Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos.", Toast.LENGTH_LONG).show();
                etEmail.setError("Verifique suas credenciais");
                etSenha.setError("Verifique suas credenciais");
                etEmail.requestFocus();
            }
        });
    }

    public void irParaRecuperarSenha (View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void irParaCriarConta (View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}