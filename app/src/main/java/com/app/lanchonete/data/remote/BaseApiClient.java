package com.app.lanchonete.data.remote;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class BaseApiClient {

    protected RequestQueue requestQueue;
    protected static Context context;

    protected BaseApiClient(Context ctx) {
        context = ctx.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}