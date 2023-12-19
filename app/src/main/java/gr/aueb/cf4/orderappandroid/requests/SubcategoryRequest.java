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
import java.util.Locale;
import java.util.Map;

import gr.aueb.cf4.orderappandroid.models.Subcategory;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class SubcategoryRequest {

    private static final String BASE_URL = "http://192.168.2.8:8080/api/subcategories/in-category/";
    private static final String URL_SUBCATEGORIES = BASE_URL + "%d";

    public static void fetchSubcategories(Context context, long categoryId,
                                          Response.Listener<List<Subcategory>> successListener,
                                          Response.ErrorListener errorListener) {
        String url = String.format(Locale.US, URL_SUBCATEGORIES, categoryId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("SubcategoryRequest", "Response received: " + response.toString());
                    List<Subcategory> subcategories = parseSubcategories(response.toString());
                    if (subcategories != null) {
                        successListener.onResponse(subcategories);
                    } else {
                        String errorMessage = "Failed to parse subcategories";
                        Log.e("SubcategoryRequest", errorMessage);
                        errorListener.onErrorResponse(new VolleyError(errorMessage));
                    }
                },
                error -> {
                    Log.e("SubcategoryRequest", "Volley error: " + error.toString());
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

    private static List<Subcategory> parseSubcategories(String response) {
        try {
            Gson gson = new Gson();
            Type subcategoryListType = new TypeToken<List<Subcategory>>() {}.getType();
            return gson.fromJson(response, subcategoryListType);
        } catch (JsonSyntaxException e) {
            Log.e("SubcategoryRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
