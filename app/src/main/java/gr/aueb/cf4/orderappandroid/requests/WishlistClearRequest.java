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
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class WishlistClearRequest {
    private static final String URL_CLEAR_WISHLIST = "http://192.168.2.8:8080/api/wishlists/clear";

    public static void clearWishlistForCurrentUser(Context context, Response.Listener<Wishlist> successListener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL_CLEAR_WISHLIST, null,
                response -> {
                    Log.d("WishlistRequest", "Clear Wishlist Response: " + response.toString());
                    Wishlist clearedWishlist = parseClearedWishlist(response.toString());
                    if (clearedWishlist != null) {
                        successListener.onResponse(clearedWishlist);
                    } else {
                        String errorMessage = "Failed to parse cleared wishlist";
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

    private static Wishlist parseClearedWishlist(String response) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(response, Wishlist.class);
        } catch (JsonSyntaxException e) {
            Log.e("WishlistRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
