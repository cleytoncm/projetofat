package com.app.lanchonete;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lanchonete.adapter.PedidosAdapter;
import com.app.lanchonete.data.remote.UsuarioApiClient;
import com.app.lanchonete.model.Pedido;
import com.app.lanchonete.model.Usuario;
import com.app.lanchonete.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMyOrders;
    private PedidosAdapter pedidosAdapter;
    private UsuarioApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        apiClient = UsuarioApiClient.getInstance(this);

        recyclerViewMyOrders = findViewById(R.id.recycler_view_my_orders);
        recyclerViewMyOrders.setLayoutManager(new LinearLayoutManager(this));

        pedidosAdapter = new PedidosAdapter();
        recyclerViewMyOrders.setAdapter(pedidosAdapter);

        loadUserOrders();
    }

    private void loadUserOrders() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, Context.MODE_PRIVATE);
        String userId = sharedPref.getString("idUsuario", null);

        if (userId != null) {
            apiClient.obterUsuarioCompletoPorId(userId, new UsuarioApiClient.UsuarioCompletoCallback() {
                @Override
                public void onSuccess(Usuario usuarioCompleto) {
                    Log.d("MyOrdersActivity", "Pedidos carregados com sucesso para o usuário: " + usuarioCompleto.getNome());

                    if (usuarioCompleto.getPedidos() != null && !usuarioCompleto.getPedidos().isEmpty()) {
                        List<Pedido> pedidos = new ArrayList<>();
                        for (Object pedidoObj : usuarioCompleto.getPedidos()) {
                            if (pedidoObj instanceof Pedido) {
                                pedidos.add((Pedido) pedidoObj);
                            }
                        }

                        Collections.sort(pedidos, new Comparator<Pedido>() {
                            @Override
                            public int compare(Pedido p1, Pedido p2) {
                                return p2.getDataDoPedido().compareTo(p1.getDataDoPedido());
                            }
                        });
                        pedidosAdapter.setPedidos(pedidos);
                    } else {
                        Log.d("MyOrdersActivity", "Nenhum pedido encontrado para o usuário.");
                        pedidosAdapter.setPedidos(new ArrayList<>());
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("MyOrdersActivity", "Erro ao carregar pedidos: " + errorMessage);
                }
            });
        } else {
            Log.e("MyOrdersActivity", "Nenhum ID de usuário salvo. Redirecionando para login.");
        }
    }
}
