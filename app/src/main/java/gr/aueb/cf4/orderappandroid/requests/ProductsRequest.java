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

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.aueb.cf4.orderappandroid.models.Product;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;
import gr.aueb.cf4.orderappandroid.utils.VolleySingleton;

public class ProductsRequest {

    private static final String BASE_URL = "http://192.168.2.8:8080/api/products/by-subcategory/";
    private static final String URL_PRODUCTS = BASE_URL + "%d"; // %d will be replaced with the subcategory ID

    public static void fetchProducts(Context context, long subcategoryId,
                                     Response.Listener<List<Product>> successListener,
                                     Response.ErrorListener errorListener) {
        String url = String.format(URL_PRODUCTS, subcategoryId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("ProductsRequest", "Response received: " + response.toString());
                    List<Product> products = parseProducts(response);
                    if (products != null) {
                        successListener.onResponse(products);
                    } else {
                        String errorMessage = "Failed to parse products";
                        Log.e("ProductsRequest", errorMessage);
                        errorListener.onErrorResponse(new VolleyError(errorMessage));
                    }
                },
                error -> {
                    Log.e("ProductsRequest", "Volley error: " + error.toString());
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

    private static List<Product> parseProducts(JSONArray response) {
        try {
            Gson gson = new Gson();
            // Use TypeToken to specify the generic type for List<Product>
            List<Product> products = gson.fromJson(response.toString(), new TypeToken<List<Product>>(){}.getType());
            return products;
        } catch (JsonSyntaxException e) {
            Log.e("ProductsRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e("ProductsRequest", "Error parsing JSON: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
