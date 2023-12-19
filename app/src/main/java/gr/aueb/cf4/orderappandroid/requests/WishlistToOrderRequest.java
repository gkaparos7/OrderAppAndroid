package gr.aueb.cf4.orderappandroid.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.aueb.cf4.orderappandroid.models.WishlistItem;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class WishlistToOrderRequest {

    private static final String URL_CREATE_ORDER_FROM_WISHLIST = "http://192.168.2.8:8080/api/orders/wishlist";

    public static void createOrderFromWishlist(Context context, List<WishlistItem> wishlistItems,
                                               Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        // Convert WishlistItems to JSON Array
        Gson gson = new Gson();
        String jsonArray = gson.toJson(wishlistItems);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_CREATE_ORDER_FROM_WISHLIST, null,
                response -> {
                    Log.d("WishlistToOrderRequest", "Response received: " + response.toString());
                    successListener.onResponse(response.toString());
                },
                error -> {
                    Log.e("WishlistToOrderRequest", "Volley error: " + error.toString());
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
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                return jsonArray.getBytes();
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}

