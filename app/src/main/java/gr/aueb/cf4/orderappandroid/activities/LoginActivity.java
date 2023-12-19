package gr.aueb.cf4.orderappandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import gr.aueb.cf4.orderappandroid.MainActivity;
import gr.aueb.cf4.orderappandroid.R;


public class LoginActivity extends AppCompatActivity {

    private Context context;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private MaterialTextView tvRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle login button click
                loginUser();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser() {
        // Get entered username and password
        String username = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // Validate email
        if (!isValidEmail(username)) {
            // Display an error message for invalid email
            Toast.makeText(LoginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.2.8:8080/api/auth/login";

        // Create JSON request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful login response
                        try {
                            // Extract authentication token or other relevant data from the response
                            String authToken = response.getString("token");

                            // Store the authentication token in SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("authToken", authToken);
                            editor.apply();

                            // Navigate to the next screen
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle login error
                        if (error instanceof NoConnectionError) {
                            // Handle network error
                            Toast.makeText(LoginActivity.this,"No internet connection", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            // Handle timeout error
                            Toast.makeText(LoginActivity.this,"Request timed out", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            // Handle authentication failure (e.g., invalid credentials)
                            Toast.makeText(LoginActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            // Handle server error (e.g., 5xx status codes)
                            Toast.makeText(LoginActivity.this,"Server error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            // Handle general network error
                            Toast.makeText(LoginActivity.this,"Network error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            // Handle parsing response error
                            Toast.makeText(LoginActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle other types of errors
                            Toast.makeText(LoginActivity.this,"An error occurred", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
        // Add the request to the RequestQueue
        queue.add(request);
    }

    // Method to validate email using a simple regex
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}


