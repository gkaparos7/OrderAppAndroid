package gr.aueb.cf4.orderappandroid.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.aueb.cf4.orderappandroid.models.OrderItem;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class OrderItemsRequest {

    private static final String BASE_URL_ORDER_ITEMS = "http://192.168.2.8:8080/api/order-items/order/";

    public static void fetchOrderItemsByOrderId(Context context, long orderId, Response.Listener<List<OrderItem>> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL_ORDER_ITEMS + orderId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<OrderItem> orderItems = parseOrderItems(response.toString());
                    if (orderItems != null) {
                        successListener.onResponse(orderItems);
                    } else {
                        String errorMessage = "Failed to parse order items";
                        Log.e("OrderItemRequest", errorMessage);
                        errorListener.onErrorResponse(new VolleyError(errorMessage));
                    }
                },
                error -> {
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

    private static List<OrderItem> parseOrderItems(String response) {
        try {
            Gson gson = new Gson();
            // Use TypeToken to specify the generic type for List<OrderItem>
            List<OrderItem> orderItems = gson.fromJson(response, new TypeToken<List<OrderItem>>(){}.getType());
            return orderItems;
        } catch (JsonSyntaxException e) {
            Log.e("OrderItemRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e("OrderItemRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

}
