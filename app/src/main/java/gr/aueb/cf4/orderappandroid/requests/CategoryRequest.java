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

import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.aueb.cf4.orderappandroid.models.Category;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class CategoryRequest {

    private static final String URL_CATEGORIES = "http://192.168.2.8:8080/api/categories";

    public static void fetchCategories(Context context, Response.Listener<List<Category>> successListener, Response.ErrorListener errorListener) throws AuthFailureError {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_CATEGORIES, null,
                response -> {
                    Log.d("CategoryRequest", "Response received: " + response.toString());
                    List<Category> categories = parseCategories(response.toString());
                    if (categories != null) {
                        successListener.onResponse(categories);
                    } else {
                        String errorMessage = "Failed to parse categories";
                        Log.e("CategoryRequest", errorMessage);
                        errorListener.onErrorResponse(new VolleyError(errorMessage));
                    }
                },
                error -> {
                    Log.e("CategoryRequest", "Volley error: " + error.toString());
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

    private static List<Category> parseCategories(String response) {
        try {
            Gson gson = new Gson();
            Type categoryListType = new TypeToken<List<Category>>() {}.getType();
            return gson.fromJson(response, categoryListType);
        } catch (JsonSyntaxException e) {
            Log.e("CategoryRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}



