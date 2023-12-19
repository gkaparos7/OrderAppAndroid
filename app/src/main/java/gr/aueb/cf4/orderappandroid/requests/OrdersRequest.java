package gr.aueb.cf4.orderappandroid.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

import gr.aueb.cf4.orderappandroid.models.Order;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class OrdersRequest {

    private static final String URL_ORDERS = "http://192.168.2.8:8080/api/orders/current-user";

    public static void fetchOrdersForCurrentUser(Context context, Response.Listener<Order[]> successListener, Response.ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_ORDERS, null,
                response -> {
                    Log.d("OrderRequest", "Response received: " + response.toString());
                    Order[] orders = parseOrders(response.toString());
                    if (orders != null) {
                        successListener.onResponse(orders);
                    } else {
                        String errorMessage = "Failed to parse orders";
                        Log.e("OrderRequest", errorMessage);
                        errorListener.onErrorResponse(new VolleyError(errorMessage));
                    }
                },
                error -> {
                    Log.e("OrderRequest", "Volley error: " + error.toString());
                    VolleySingleton.handleNetworkError(context, error);
                    errorListener.onErrorResponse(error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String authToken = new AuthManager(context).getAuthToken();
                if (authToken != null) {
                    headers.put("Authorization", authToken);
                }
                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    private static Order[] parseOrders(String response) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(response, Order[].class);
        } catch (JsonSyntaxException e) {
            Log.e("OrderRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}

