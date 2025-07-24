package com.app.lanchonete;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.lanchonete.model.Usuario;
import com.app.lanchonete.data.remote.UsuarioApiClient;

public class RegisterActivity extends AppCompatActivity {

    private EditText campoNomeCompleto;
    private EditText campoEmail;
    private EditText campoTelefone;
    private EditText campoSenha;
    private EditText campoConfirmarSenha;
    private Button botaoCadastrar;
    private TextView textoJaTemConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        campoNomeCompleto = findViewById(R.id.edit_text_full_name);
        campoEmail = findViewById(R.id.edit_text_register_email);
        campoTelefone = findViewById(R.id.edit_text_phone);
        campoSenha = findViewById(R.id.edit_text_register_password);
        campoConfirmarSenha = findViewById(R.id.edit_text_confirm_password);
        botaoCadastrar = findViewById(R.id.button_register);
        textoJaTemConta = findViewById(R.id.text_view_already_have_account);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tentarCadastro();
            }
        });

        textoJaTemConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void tentarCadastro() {
        campoNomeCompleto.setError(null);
        campoEmail.setError(null);
        campoTelefone.setError(null);
        campoSenha.setError(null);
        campoConfirmarSenha.setError(null);

        String nomeCompleto = campoNomeCompleto.getText().toString().trim();
        String email = campoEmail.getText().toString().trim();
        String telefone = campoTelefone.getText().toString().trim();
        String senha = campoSenha.getText().toString().trim();
        String confirmarSenha = campoConfirmarSenha.getText().toString().trim();

        boolean cancelar = false;
        View focoView = null;

        if (TextUtils.isEmpty(nomeCompleto)) {
            campoNomeCompleto.setError("Campo obrigatório");
            focoView = campoNomeCompleto;
            cancelar = true;
        }

        if (TextUtils.isEmpty(email)) {
            campoEmail.setError("Campo obrigatório");
            focoView = campoEmail;
            cancelar = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campoEmail.setError("E-mail inválido");
            focoView = campoEmail;
            cancelar = true;
        }

        if (TextUtils.isEmpty(telefone)) {
            campoTelefone.setError("Campo obrigatório");
            focoView = campoTelefone;
            cancelar = true;
        } else if (telefone.length() < 10) {
            campoTelefone.setError("Telefone inválido");
            focoView = campoTelefone;
            cancelar = true;
        }

        if (TextUtils.isEmpty(senha)) {
            campoSenha.setError("Campo obrigatório");
            focoView = campoSenha;
            cancelar = true;
        } else if (senha.length() < 6) {
            campoSenha.setError("Senha muito curta (mínimo 6 caracteres)");
            focoView = campoSenha;
            cancelar = true;
        }

        if (TextUtils.isEmpty(confirmarSenha)) {
            campoConfirmarSenha.setError("Campo obrigatório");
            focoView = campoConfirmarSenha;
            cancelar = true;
        } else if (!senha.equals(confirmarSenha)) {
            campoConfirmarSenha.setError("As senhas não coincidem");
            focoView = campoConfirmarSenha;
            cancelar = true;
        }

        if (cancelar) {
            focoView.requestFocus();
        } else {
            verificarEmailUnicoECadastrar(nomeCompleto, email, telefone, senha);
        }
    }

    private void verificarEmailUnicoECadastrar(final String nomeCompleto, final String email, final String telefone, final String senha) {
        Toast.makeText(this, "Verificando e-mail...", Toast.LENGTH_SHORT).show();

        UsuarioApiClient.getInstance(this).verificarEmailUnico(email, new UsuarioApiClient.EmailCheckCallback() {
            @Override
            public void onSuccess(boolean isUnique) {
                if (isUnique) {
                    cadastrarUsuario(new Usuario(nomeCompleto, email, telefone, senha));
                } else {
                    campoEmail.setError("Este e-mail já está cadastrado.");
                    campoEmail.requestFocus();
                    Toast.makeText(RegisterActivity.this, "E-mail já cadastrado.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(RegisterActivity.this, "Erro ao verificar e-mail: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cadastrarUsuario(Usuario usuario) {
        Toast.makeText(this, "Cadastrando usuário...", Toast.LENGTH_SHORT).show();

        UsuarioApiClient.getInstance(this).cadastrarUsuario(usuario, new UsuarioApiClient.RegisterUserCallback() {
            @Override
            public void onSuccess(Usuario usuarioCadastrado) {
                Toast.makeText(RegisterActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(RegisterActivity.this, "Falha no cadastro: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}