package gr.aueb.cf4.orderappandroid.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleySingleton(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public static void handleNetworkError(Context context, VolleyError error) {
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            String errorMessage = new String(error.networkResponse.data);
            Toast.makeText(context, "Network error occurred: " + statusCode + "\n" + errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Network error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void addToRequestQueue(Request<?> req) {
        getRequestQueue().add(req);
    }
}

