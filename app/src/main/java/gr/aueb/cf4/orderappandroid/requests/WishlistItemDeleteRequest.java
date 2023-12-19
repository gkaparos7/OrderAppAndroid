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

import gr.aueb.cf4.orderappandroid.models.WishlistItem;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class WishlistItemDeleteRequest {

    private static final String URL_DELETE_WISHLIST_ITEM = "http://192.168.2.8:8080/api/wishlist-items/";

    public static void deleteWishlistItem(Context context, WishlistItem wishlistItem,
                                          Response.Listener<WishlistItem> successListener,
                                          Response.ErrorListener errorListener) {
        try {
            String url = URL_DELETE_WISHLIST_ITEM + wishlistItem.getId();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                    response -> {
                        Log.d("WishlistItemDeleteRequest", "Response received: " + response.toString());
                        WishlistItem deletedWishlistItem = parseWishlistItem(response.toString());
                        if (deletedWishlistItem != null) {
                            successListener.onResponse(deletedWishlistItem);
                        } else {
                            String errorMessage = "Failed to parse deleted WishlistItem";
                            Log.e("WishlistItemDeleteRequest", errorMessage);
                            errorListener.onErrorResponse(new VolleyError(errorMessage));
                        }
                    },
                    error -> {
                        Log.e("WishlistItemDeleteRequest", "Volley error: " + error.toString());
                        VolleySingleton.handleNetworkError(context, error);
                        errorListener.onErrorResponse(error);
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    try {
                        String authToken = new AuthManager(context).getAuthToken();
                        if (authToken != null) {
                            headers.put("Authorization", authToken);
                        } else throw new AuthFailureError("Not Authenticated");
                    } catch (AuthFailureError authFailureError) {
                        // Handle AuthFailureError here, e.g., log the error or trigger an error callback
                        Log.e("WishlistItemDeleteRequest", "Failed to get auth token: " + authFailureError.toString());
                        errorListener.onErrorResponse(new VolleyError("Failed to get auth token"));
                    }
                    return headers;
                }
            };

            VolleySingleton.getInstance(context).addToRequestQueue(request);
        }  catch (Exception e) {
            // Handle other exceptions if needed
            Log.e("WishlistItemDeleteRequest", "Error: " + e.toString());
            e.printStackTrace();
            errorListener.onErrorResponse(new VolleyError("Request error"));
        }
    }




    private static WishlistItem parseWishlistItem(String response) {
        try {
            Gson gson = new Gson();
            WishlistItem wishlistItem = gson.fromJson(response, WishlistItem.class);

            // Log the Size enum value for debugging
            if (wishlistItem != null && wishlistItem.getSize() != null) {
                Log.d("WishlistItemParser", "Size Enum Value: " + wishlistItem.getSize().name());
            }

            return wishlistItem;
        } catch (JsonSyntaxException e) {
            Log.e("WishlistItemParser", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}

