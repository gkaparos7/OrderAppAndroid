package gr.aueb.cf4.orderappandroid.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

import gr.aueb.cf4.orderappandroid.models.Product;
import gr.aueb.cf4.orderappandroid.models.Size;
import gr.aueb.cf4.orderappandroid.models.WishlistItem;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class ToWishlistRequest {

    private static final String URL_TO_WISHLIST = "http://192.168.2.8:8080/api/wishlist-items";
    public static void createWishlistItem(Context context, Product product, String quantity, String size,
                                          Response.Listener<WishlistItem> successListener,
                                          Response.ErrorListener errorListener) {
        // Build the request body
        WishlistItem wishlistItem = new WishlistItem(
                product,
                Integer.parseInt(quantity),
                Size.fromDisplayName(size)
        );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_TO_WISHLIST, null,
                response -> {
                    Log.d("WishlistRequest", "Response received: " + response.toString());
                    WishlistItem createdWishlistItem = parseWishlistItem(response.toString());
                    if (createdWishlistItem != null) {
                        successListener.onResponse(createdWishlistItem);
                    } else {
                        String errorMessage = "Failed to parse wishlist item";
                        Log.e("WishlistRequest", errorMessage);
                        errorListener.onErrorResponse(new VolleyError(errorMessage));
                    }
                },
                error -> {
                    Log.e("WishlistRequest", "Volley error: " + error.toString());
                    VolleySingleton.handleNetworkError(context, error);
                    errorListener.onErrorResponse(error);
                }) {

            @Override
            public byte[] getBody() {
                Gson gson = new Gson();
                String jsonString = gson.toJson(wishlistItem);
                return jsonString.getBytes();
            }

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

    private static WishlistItem parseWishlistItem(String response) {
        Gson gson = new Gson();
        try {
            WishlistItem wishlistItem = gson.fromJson(response, WishlistItem.class);
            return wishlistItem;
        } catch (JsonSyntaxException e) {
            Log.e("ToWishlistRequest", "Error parsing WishlistItem from JSON: " + e.getMessage());
            return null;
        }
    }
}
