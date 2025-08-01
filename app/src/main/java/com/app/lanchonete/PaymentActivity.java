package com.app.lanchonete;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.lanchonete.data.CarrinhoSingleton;
import com.app.lanchonete.data.remote.UsuarioApiClient;
import com.app.lanchonete.model.ItemCarrinho;
import com.app.lanchonete.model.Pedido;
import com.app.lanchonete.model.Usuario;
import com.app.lanchonete.utils.Constants;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView textViewSubtotalSummaryValue;
    private TextView textViewDeliveryFeeValue;
    private TextView textViewTotalValue;
    private Button buttonFinalizeOrder;
    private UsuarioApiClient usuarioApiClient;
    private double pedidoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        usuarioApiClient = UsuarioApiClient.getInstance(this);

        textViewSubtotalSummaryValue = findViewById(R.id.text_view_subtotal_summary_value);
        textViewDeliveryFeeValue = findViewById(R.id.text_view_delivery_fee_value);
        textViewTotalValue = findViewById(R.id.text_view_total_value);
        buttonFinalizeOrder = findViewById(R.id.button_finalize_order);

        updateOrderSummary();

        buttonFinalizeOrder.setOnClickListener(v -> {
            fazerPagamento();
        });
    }

    private void updateOrderSummary() {
        CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance(this);
        double subtotal = carrinho.calcularTotal();
        double deliveryFee = 10.00;
        this.pedidoTotal = subtotal + deliveryFee;

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        textViewSubtotalSummaryValue.setText(currencyFormat.format(subtotal));
        textViewDeliveryFeeValue.setText(currencyFormat.format(deliveryFee));
        textViewTotalValue.setText(currencyFormat.format(pedidoTotal));
    }

    private void fazerPagamento() {
        Usuario usuarioLogado = usuarioApiClient.getUsuarioLogado();

        if (usuarioLogado == null) {
            SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE);
            String idUsuario = preferences.getString("idUsuario", null);

            if (idUsuario != null) {
                usuarioApiClient.obterUsuarioCompletoPorId(idUsuario, new UsuarioApiClient.UsuarioCompletoCallback() {
                    @Override
                    public void onSuccess(Usuario usuarioCompleto) {
                        usuarioApiClient.setUsuarioLogado(usuarioCompleto);
                        salvarPedido();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("PaymentActivity", "Erro ao obter dados do usuário: " + errorMessage);
                        Toast.makeText(PaymentActivity.this, "Erro ao carregar seus dados. Tente novamente.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Erro: Nenhum usuário logado. Faça o login novamente.", Toast.LENGTH_LONG).show();
            }
        } else {
            salvarPedido();
        }
    }

    private void salvarPedido() {
        CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance(this);
        List<ItemCarrinho> itensCarrinho = carrinho.getItens();
        double total = carrinho.calcularTotal() + 10.00;
        String metodoPagamento = "Cartão de Crédito";

        Pedido novoPedido = new Pedido(
                itensCarrinho,
                total,
                metodoPagamento
        );

        usuarioApiClient.salvarNovoPedido(novoPedido, new UsuarioApiClient.SalvarPedidoCallback() {
            @Override
            public void onSuccess(Usuario usuarioComPedidoSalvo) {
                Log.d("PaymentActivity", "Pedido salvo com sucesso! ID do usuário: " + usuarioComPedidoSalvo.getId());
                Toast.makeText(PaymentActivity.this, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show();

                carrinho.limparCarrinho();

                Intent intent = new Intent(PaymentActivity.this, MyOrdersActivity.class);
                intent.putExtra("pedido", novoPedido);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("PaymentActivity", "Erro ao salvar o pedido: " + errorMessage);
                Toast.makeText(PaymentActivity.this, "Erro ao salvar o pedido: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
