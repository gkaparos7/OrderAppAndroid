package gr.aueb.cf4.orderappandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AuthManager {

    private final Context context;

    public AuthManager(Context context) {
        this.context = context;
    }

    public String getAuthToken() {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String authToken = preferences.getString("authToken", null);

        // Check if the authToken is not null and does not start with "Bearer "
        if (authToken != null && !authToken.startsWith("Bearer ")) {
            // Prepend "Bearer " to the token
            authToken = "Bearer " + authToken;
        }

        Log.d("AuthManager", "AuthToken: " + authToken);
        return authToken;
    }
    public String getLoggedInUsername() {
        String authToken = getAuthToken();
        return JwtUtils.extractUserNameFromToken(authToken);
    }
}
