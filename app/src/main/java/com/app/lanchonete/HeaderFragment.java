package com.app.lanchonete;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.lanchonete.utils.Constants;

public class HeaderFragment extends Fragment {

    private TextView textViewLoggedInUser;
    private ImageView imageViewLogoHeader;
    private ImageButton buttonLogoutHeader;
    private ImageButton buttonHamburguerMenu;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_header, container, false);
        textViewLoggedInUser = view.findViewById(R.id.text_view_logged_in_user);
        imageViewLogoHeader = view.findViewById(R.id.image_view_logo_header);
        buttonLogoutHeader = view.findViewById(R.id.button_logout_header);
        buttonHamburguerMenu = view.findViewById(R.id.button_hamburguer_menu);

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

        buttonHamburguerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        return view;
    }

    private void showMenu(View v) {
        PopupMenu menu = new PopupMenu(getActivity(), v);
        menu.getMenuInflater().inflate(R.menu.layout_menu_options, menu.getMenu());

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;

                if (id == R.id.item_home) {
                    intent = new Intent(getActivity(), MainActivity.class);
                }

                if (id == R.id.item_catalog) {
                    intent = new Intent(getActivity(), CatologActivity.class);
                }

                if (id == R.id.item_profile) {
                    intent = new Intent(getActivity(), UserProfileActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);

                    if (!(getActivity() instanceof MainActivity)) {
                        getActivity().finish();
                    }
                }

                return true;
            }
        });

        menu.show();
    }

    private void loadUserName() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE);
        String userName = preferences.getString("nomeUsuario", "");
        textViewLoggedInUser.setText("Olá, " + userName + "!");
    }

    public void logout() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
