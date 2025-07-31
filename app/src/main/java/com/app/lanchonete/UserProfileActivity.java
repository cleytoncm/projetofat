package com.app.lanchonete;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.lanchonete.data.remote.UsuarioApiClient;
import com.app.lanchonete.model.Usuario;
import com.app.lanchonete.utils.Constants;

public class UserProfileActivity extends AppCompatActivity {

    private EditText editTextFullName;
    private EditText editTextRegisterEmail;
    private EditText editTextPhone;
    private EditText editTextRegisterPassword;
    private EditText editTextConfirmPassword;
    private Button buttonEditProfile;
    private SharedPreferences preferences;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editTextFullName = findViewById(R.id.edit_text_full_name);
        editTextRegisterEmail = findViewById(R.id.edit_text_register_email);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextRegisterPassword = findViewById(R.id.edit_text_register_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        buttonEditProfile = findViewById(R.id.button_edit_profile);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", null);

        obterUsuarioPorId(idUsuario);

        buttonEditProfile.setOnClickListener(v -> editarPerfil());

    }

    private void obterUsuarioPorId(String id) {
        UsuarioApiClient.getInstance(this).obterUsuarioPorId(id, new UsuarioApiClient.UsuarioPorIdCallback() {
            @Override
            public void onSuccess(Usuario u) {
                usuario = u;
                editTextFullName.setText(usuario.getNome());
                editTextRegisterEmail.setText(usuario.getEmail());
                editTextPhone.setText(usuario.getTelefone());
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("UserProfileActivity", "Erro ao obter usuário: " + errorMessage);
                Toast.makeText(UserProfileActivity.this, "Erro ao obter usuário: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarPerfil() {
        String nome = editTextFullName.getText().toString();
        String email = editTextRegisterEmail.getText().toString();
        String telefone = editTextPhone.getText().toString();
        String senha = editTextRegisterPassword.getText().toString();
        String confirmaSenha = editTextConfirmPassword.getText().toString();

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);

        if (!senha.isEmpty()) {
            if(!senha.equals(confirmaSenha)) {
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                editTextRegisterPassword.setError("As senhas não coincidem.");
                editTextConfirmPassword.setError("As senhas não coincidem.");
                editTextRegisterPassword.requestFocus();

                return;
            }

            usuario.setSenha(senha);
        }

        enviarEditarPerfil();
    }

    private void enviarEditarPerfil() {
        UsuarioApiClient.getInstance(this).alterarUsuario(usuario, new UsuarioApiClient.AlterarUsuarioCallback() {
            @Override
            public void onSuccess(Usuario usuarioAlterado) {
                Toast.makeText(UserProfileActivity.this, "Perfil editado com sucesso!", Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nomeUsuario", usuarioAlterado.getNome());
                editor.putString("emailUsuario", usuarioAlterado.getEmail());
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("UserProfileActivity", "Erro ao editar perfil: " + errorMessage);
                Toast.makeText(UserProfileActivity.this, "Erro ao editar perfil: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}