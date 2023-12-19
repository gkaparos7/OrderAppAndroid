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

import gr.aueb.cf4.orderappandroid.models.Wishlist;
import gr.aueb.cf4.orderappandroid.models.WishlistItem;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class WishlistRequest {

    private static final String URL_WISHLIST = "http://192.168.2.8:8080/api/wishlists/current-user";

    public static void fetchWishlistForCurrentUser(Context context, Response.Listener<Wishlist> successListener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_WISHLIST, null,
                response -> {
                    Log.d("WishlistRequest", "Response received: " + response.toString());
                    Wishlist wishlist = parseWishlist(response.toString());
                    if (wishlist != null) {
                        successListener.onResponse(wishlist);
                    } else {
                        String errorMessage = "Failed to parse wishlist";
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


    private static Wishlist parseWishlist(String response) {
        try {
            Gson gson = new Gson();
            Wishlist wishlist = gson.fromJson(response, Wishlist.class);

            // Log the Size enum values for debugging
            if (wishlist != null && wishlist.getWishlistItems() != null) {
                for (WishlistItem item : wishlist.getWishlistItems()) {
                    if (item.getSize() != null) {
                        Log.d("WishlistRequest", "Size Enum Value: " + item.getSize().name());
                    }
                }
            }

            return wishlist;
        } catch (JsonSyntaxException e) {
            Log.e("WishlistRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}

