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
}