package com.app.lanchonete;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HeaderFragment extends Fragment {

    private TextView textViewLoggedInUser;
    private ImageView imageViewLogoHeader;
    private ImageButton buttonLogoutHeader;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_header, container, false);
        textViewLoggedInUser = view.findViewById(R.id.text_view_logged_in_user);
        imageViewLogoHeader = view.findViewById(R.id.image_view_logo_header);
        buttonLogoutHeader = view.findViewById(R.id.button_logout_header);

        loadUserName();

        imageViewLogoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        buttonLogoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    private void loadUserName() {
        SharedPreferences preferences = getActivity().getSharedPreferences("lanchonete.autenticacao", MODE_PRIVATE);
        String userName = preferences.getString("nomeUsuario", "");
        textViewLoggedInUser.setText("Olá, " + userName + "!");
    }

    public void logout() {
        SharedPreferences preferences = getActivity().getSharedPreferences("lanchonete.autenticacao", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
