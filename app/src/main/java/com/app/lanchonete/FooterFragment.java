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
    private TextView cartBadgeTextView;

    public FooterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_footer, container, false);
        cartIconButton = view.findViewById(R.id.button_shopping);
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
