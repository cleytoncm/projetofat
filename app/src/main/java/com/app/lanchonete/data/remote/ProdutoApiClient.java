package com.app.lanchonete.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.lanchonete.model.Produto;
import com.app.lanchonete.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProdutoApiClient extends BaseApiClient {

    private static ProdutoApiClient instance;

    private ProdutoApiClient(Context ctx) {
        super(ctx);
    }

    public static synchronized ProdutoApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ProdutoApiClient(context);
        }
        return instance;
    }

    public interface ProdutoCallback {
        void onSuccess(List<Produto> produtos);

        void onError(String errorMessage);
    }

    public interface ProdutoPorIdCallback {
        void onSuccess(Produto produto);
        void onError(String errorMessage);
    }

    public void obterProdutos(final ProdutoCallback callback) {
        String url = Constants.BASE_URL + "produtos";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Produto> produtos = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject produtoJson = response.getJSONObject(i);

                                int id = produtoJson.getInt("id");
                                String nome = produtoJson.getString("nome");
                                double preco = produtoJson.getDouble("preco");
                                String descricao = produtoJson.getString("descricao");
                                String imagemUrl = produtoJson.getString("imagemUrl");
                                String categoria = produtoJson.getString("categoria");

                                Produto produto = new Produto(id, nome, descricao, preco, imagemUrl, categoria);

                                produtos.add(produto);
                            } catch (JSONException e) {
                                Log.e("ProdutoApiClient", e.getMessage(), e);
                                callback.onError(e.getMessage());
                            }
                        }

                        callback.onSuccess(produtos);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Erro ao obter produtos.";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        callback.onError(errorMessage);
                    }
                }
        );

        addToRequestQueue(jsonArrayRequest);
    }

    public void obterProdutoPorId(int id, final ProdutoPorIdCallback callback) {
        String url = Constants.BASE_URL + "produtos/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null && response.length() > 0) {
                            try {
                                JSONObject produtoJson = response.getJSONObject(0);
                                int id = produtoJson.getInt("id");
                                String nome = produtoJson.getString("nome");
                                double preco = produtoJson.getDouble("preco");
                                String descricao = produtoJson.getString("descricao");
                                String imagemUrl = produtoJson.getString("imagemUrl");
                                String categoria = produtoJson.getString("categoria");

                                Produto produto = new Produto(id, nome, descricao, preco, imagemUrl, categoria);

                                callback.onSuccess(produto);
                            } catch (JSONException e) {
                                callback.onError(e.getMessage());
                            }
                        } else {
                            callback.onError("Produto não encontrado.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Erro ao obter produto.";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        callback.onError(errorMessage);
                    }
                }
        );

        addToRequestQueue(jsonArrayRequest);
    }
}
