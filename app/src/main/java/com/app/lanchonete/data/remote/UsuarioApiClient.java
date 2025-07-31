package com.app.lanchonete.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.lanchonete.model.Usuario;
import com.app.lanchonete.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioApiClient extends BaseApiClient {

    private static UsuarioApiClient instance;

    private UsuarioApiClient(Context ctx) {
        super(ctx);
    }

    public static synchronized UsuarioApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new UsuarioApiClient(context);
        }
        return instance;
    }

    public interface EmailCheckCallback {
        void onSuccess(boolean isUnique);
        void onError(String errorMessage);
    }

    public interface RegisterUserCallback {
        void onSuccess(Usuario registeredUser);
        void onError(String errorMessage);
    }

    public interface AlterarUsuarioCallback {
        void onSuccess(Usuario usuarioAlterado);
        void onError(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess(Usuario usuarioLogado);
        void onError(String errorMessage);
        void onCredenciaisInvalidas();
    }

    public interface UsuarioPorIdCallback {
        void onSuccess(Usuario usuario);
        void onError(String errorMessage);
    }

    public void verificarEmailUnico(String email, final EmailCheckCallback callback) {
        String url = Constants.BASE_URL + "usuarios?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response.length() == 0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Log.i("UsuarioApiClient", "Email não encontrado (404), considerado único.");
                            callback.onSuccess(true);
                            return;
                        }

                        String errorMessage = "Erro ao verificar unicidade do e-mail.";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("UsuarioApiClient", errorMessage, error);
                        callback.onError(errorMessage);
                    }
                }
        );
        addToRequestQueue(jsonArrayRequest);
    }

    public void cadastrarUsuario(Usuario usuario, final RegisterUserCallback callback) {
        String url = Constants.BASE_URL + "usuarios";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("nome", usuario.getNome());
            requestBody.put("email", usuario.getEmail());
            requestBody.put("telefone", usuario.getTelefone());
            requestBody.put("senha", usuario.getSenha());
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError("Erro ao preparar dados do usuário para cadastro.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String id = response.getString("id");
                            usuario.setId(id);
                            callback.onSuccess(usuario);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Erro ao processar resposta de cadastro.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Erro ao cadastrar usuário.";
                        if (error != null && error.networkResponse != null) {
                            errorMessage += " Status: " + error.networkResponse.statusCode;
                        } else if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("UsuarioApiClient", errorMessage, error);
                        callback.onError(errorMessage);
                    }
                }
        );
        addToRequestQueue(jsonObjectRequest);
    }

    public void alterarUsuario(Usuario usuario, final AlterarUsuarioCallback callback) {
        String url = Constants.BASE_URL + "usuarios/" + usuario.getId();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("nome", usuario.getNome());
            requestBody.put("email", usuario.getEmail());
            requestBody.put("telefone", usuario.getTelefone());
            requestBody.put("senha", usuario.getSenha());
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError("Erro ao preparar dados do usuário para alteração.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(usuario);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Erro ao alterar usuário.";
                        Log.e("UsuarioApiClient", errorMessage, error);
                        callback.onError(errorMessage);
                    }
                }
        );

        addToRequestQueue(jsonObjectRequest);
    }

    public void autenticarUsuario(String email, String senha, final LoginCallback callback) {
        String url = Constants.BASE_URL + "usuarios?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            try {
                                JSONObject usuarioJson = response.getJSONObject(0);
                                String id = usuarioJson.getString("id");
                                String nome = usuarioJson.getString("nome");
                                String emailApi = usuarioJson.getString("email");
                                String telefone = usuarioJson.getString("telefone");
                                String senhaApi = usuarioJson.getString("senha");

                                if (senhaApi.equals(senha)) {
                                    Usuario usuarioAutenticado = new Usuario(nome, emailApi, telefone, senhaApi);
                                    usuarioAutenticado.setId(id);
                                    callback.onSuccess(usuarioAutenticado);
                                } else {
                                    callback.onCredenciaisInvalidas();
                                }
                            } catch (JSONException e) {
                                Log.e("UsuarioApiClient", "Erro ao parsear JSON de usuário para login: " + e.getMessage(), e);
                                callback.onError("Erro ao processar dados do usuário.");
                            }
                        } else {
                            callback.onCredenciaisInvalidas();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                callback.onCredenciaisInvalidas();
                                return;
                            }
                        }
                        String errorMessage = "Erro de rede ao tentar fazer login.";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("UsuarioApiClient", errorMessage, error);
                        callback.onError(errorMessage);
                    }
                }
        );
        addToRequestQueue(jsonArrayRequest);
    }

    public void obterUsuarioPorId(String id, final UsuarioPorIdCallback callback) {
        String url = Constants.BASE_URL + "usuarios/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                String nome = response.getString("nome");
                                String email = response.getString("email");
                                String telefone = response.getString("telefone");
                                String senha = response.getString("senha");

                                Usuario usuario = new Usuario(nome, email, telefone, senha);
                                usuario.setId(id);
                                callback.onSuccess(usuario);

                            } catch (JSONException e) {
                                callback.onError("Erro de parseamento do JSON: " + e.getMessage());
                            }
                        } else {
                            callback.onError("Usuário não encontrado ou resposta vazia.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Erro ao obter usuário.";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        callback.onError(errorMessage);
                    }

                }
        );

        addToRequestQueue(jsonObjectRequest);
    }
}