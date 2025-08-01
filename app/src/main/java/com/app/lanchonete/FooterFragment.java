package com.app.lanchonete;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.app.lanchonete.data.CarrinhoSingleton;

public class FooterFragment extends Fragment implements CarrinhoSingleton.OnCartChangeListener {

    private ImageButton cartIconButton;
    private ImageButton profileIconButton;
    private ImageButton logoutIconButton;
    private ImageButton homeIconButton;
    private ImageButton catalogIconButton;
    private TextView cartBadgeTextView;

    public FooterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_footer, container, false);
        cartIconButton = view.findViewById(R.id.button_shopping);
        profileIconButton = view.findViewById(R.id.button_profile);
        logoutIconButton = view.findViewById(R.id.button_logout_header);
        homeIconButton = view.findViewById(R.id.button_home);
        catalogIconButton = view.findViewById(R.id.button_catalog);
        cartBadgeTextView = view.findViewById(R.id.cart_badge);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });
        profileIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
        });
        logoutIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        homeIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        catalogIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CatologActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartBadge();
    }

    @Override
    public void onStart() {
        super.onStart();
        CarrinhoSingleton.getInstance(getContext()).registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        CarrinhoSingleton.getInstance(getContext()).unregisterListener(this);
    }

    private void updateCartBadge() {
        if (getContext() != null) {
            int itemCount = CarrinhoSingleton.getInstance(getContext()).getItens().size();
            if (itemCount > 0) {
                cartBadgeTextView.setText(String.valueOf(itemCount));
                cartBadgeTextView.setVisibility(View.VISIBLE);
            } else {
                cartBadgeTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCartChanged() {
        if (isAdded()) {
            updateCartBadge();
        }
    }
}
